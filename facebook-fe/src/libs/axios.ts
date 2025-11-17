/* eslint-disable @typescript-eslint/no-explicit-any */

"use client";

import { useKeycloak } from "@react-keycloak/web";
import axios, { AxiosInstance } from "axios";
import { useEffect, useRef } from "react";

declare module "axios" {
  export interface AxiosRequestConfig {
    retry?: boolean;
    fileFlag?: boolean;
    noTokenFlag?: boolean;
  }
}

export const axiosInstance: AxiosInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL,
  timeout: 10000,
});

let isRefreshing = false;
let failedQueue: Array<{
  resolve: (value: any) => void;
  reject: (reason?: any) => void;
}> = [];

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach((prom) => {
    error ? prom.reject(error) : prom.resolve(token);
  });
  failedQueue = [];
};

export const useAxiosAuth = () => {
  const { keycloak } = useKeycloak();
  const refreshTimeout = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    if (!keycloak.authenticated) return;

    const scheduleRefresh = () => {
      const tokenExp = keycloak.tokenParsed?.exp;
      if (!tokenExp) return;

      const expiresAt = tokenExp * 1000;
      const now = Date.now();
      const refreshThreshold = 30 * 1000;
      const delay = Math.max(0, expiresAt - now - refreshThreshold);

      if (refreshTimeout.current) clearTimeout(refreshTimeout.current);
      refreshTimeout.current = setTimeout(performRefresh, delay);
    };

    const performRefresh = async () => {
      if (isRefreshing) return;
      isRefreshing = true;

      try {
        const refreshed = await keycloak.updateToken(30);

        if (!keycloak.authenticated) {
          console.warn("Session died → force logout");
          processQueue(new Error("Session expired"), null);
          keycloak.logout({ redirectUri: window.location.origin });
          return;
        }

        if (refreshed) {
          console.log("Token refreshed (proactive)");
        }

        processQueue(null, keycloak.token);
        scheduleRefresh();
      } catch (err) {
        console.error("Refresh failed (network?)", err);
        processQueue(err, null);
        keycloak.logout({ redirectUri: window.location.origin });
      } finally {
        isRefreshing = false;
      }
    };

    scheduleRefresh();

    // Check session khi tab được focus lại
    const handleFocus = () => {
      if (keycloak.authenticated) {
        keycloak.updateToken(5).catch(() => {
          keycloak.logout({ redirectUri: window.location.origin });
        });
      }
    };
    window.addEventListener("focus", handleFocus);

    // Request interceptor
    const reqInterceptor = axiosInstance.interceptors.request.use(
      (config) => {
        if (keycloak.token && !config.noTokenFlag) {
          config.headers.Authorization = `Bearer ${keycloak.token}`;
        }
        return config;
      },
      (err) => Promise.reject(err)
    );

    // Response interceptor
    const resInterceptor = axiosInstance.interceptors.response.use(
      (res) => res,
      async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
          if (isRefreshing) {
            return new Promise((resolve, reject) => {
              failedQueue.push({ resolve, reject });
            })
              .then((token) => {
                originalRequest.headers.Authorization = `Bearer ${token}`;
                return axiosInstance(originalRequest);
              })
              .catch((err) => Promise.reject(err));
          }

          originalRequest._retry = true;
          isRefreshing = true;

          try {
            const refreshed = await keycloak.updateToken(30);

            if (!keycloak.authenticated) {
              keycloak.logout({ redirectUri: window.location.origin });
              return Promise.reject(new Error("Session expired"));
            }

            processQueue(null, keycloak.token);
            originalRequest.headers.Authorization = `Bearer ${keycloak.token}`;
            return axiosInstance(originalRequest);
          } catch (err) {
            processQueue(err, null);
            keycloak.logout({ redirectUri: window.location.origin });
            return Promise.reject(err);
          } finally {
            isRefreshing = false;
          }
        }
        return Promise.reject(error);
      }
    );

    return () => {
      axiosInstance.interceptors.request.eject(reqInterceptor);
      axiosInstance.interceptors.response.eject(resInterceptor);
      if (refreshTimeout.current) clearTimeout(refreshTimeout.current);
      window.removeEventListener("focus", handleFocus);
    };
  }, [keycloak]);

  return axiosInstance;
};

export default axiosInstance;
