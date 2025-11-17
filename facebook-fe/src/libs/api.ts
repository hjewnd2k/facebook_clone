/* eslint-disable @typescript-eslint/no-empty-object-type */
/* eslint-disable @typescript-eslint/no-explicit-any */
// lib/api.ts
import type { AxiosRequestConfig } from "axios";
import axiosInstance from "./axios"; // ← Dùng instance có Keycloak

const api = {
  get: async <ReturnType, QueryParamType = any>(
    url: string,
    params?: QueryParamType,
    config: AxiosRequestConfig = {}
  ): Promise<ReturnType> =>
    axiosInstance
      .get<ReturnType>(url, { params, ...config })
      .then((res) => res.data),

  post: async <BodyType, ReturnType = {}>(
    url: string,
    body: BodyType,
    fileFlag = false,
    config: AxiosRequestConfig = {}
  ): Promise<ReturnType> =>
    axiosInstance
      .post(url, body, { fileFlag, ...config })
      .then((res) => res.data),

  patch: async <BodyType, ReturnType = {}>(
    url: string,
    body: BodyType,
    fileFlag = false,
    config: AxiosRequestConfig = {}
  ): Promise<ReturnType> =>
    axiosInstance
      .patch(url, body, { fileFlag, ...config })
      .then((res) => res.data),

  put: async <BodyType, ReturnType = {}>(
    url: string,
    body: BodyType,
    fileFlag = false,
    config: AxiosRequestConfig = {}
  ): Promise<ReturnType> =>
    axiosInstance
      .put(url, body, { fileFlag, ...config })
      .then((res) => res.data),

  delete: async <BodyType, ReturnType = {}>(
    url: string,
    body?: BodyType
  ): Promise<ReturnType> =>
    axiosInstance.delete(url, { data: body }).then((res) => res.data),
};

export default api;
