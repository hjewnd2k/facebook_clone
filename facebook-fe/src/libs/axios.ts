'use client';

import { useKeycloak } from '@react-keycloak/web';
import axios, { AxiosInstance } from 'axios';
import { useEffect, useRef } from 'react';

import keycloak from './keycloak';

declare module 'axios' {
  export interface AxiosRequestConfig {
    retry?: boolean;
    fileFlag?: boolean;
    noTokenFlag?: boolean;
  }
}

export const axiosInstance: AxiosInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL,
  timeout: 60000,
});

axiosInstance.defaults.headers.common.Accept = 'application/json';
axiosInstance.defaults.headers.common['Content-Type'] =
  'application/json; charset=UTF-8';

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

axiosInstance.interceptors.request.use(
  (config) => {
    if (keycloak.authenticated && keycloak.token) {
      config.headers.Authorization = `Bearer ${keycloak.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

axiosInstance.interceptors.response.use(
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
          return Promise.reject(new Error('Session expired'));
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
  },
);

export default axiosInstance;
