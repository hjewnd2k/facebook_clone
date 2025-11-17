/* eslint-disable @typescript-eslint/no-empty-object-type */
/* eslint-disable @typescript-eslint/no-explicit-any */
import {
  useInfiniteQuery,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import type {
  InfiniteData,
  QueryFunctionContext,
  UseInfiniteQueryOptions,
  UseMutationOptions,
  UseQueryOptions,
} from "@tanstack/react-query";
import { AxiosError, type AxiosRequestConfig } from "axios";
import {
  type API_ROUTE_TYPE,
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
} from "@/constants";
import type { IErrorData, IResponse } from "@/types";
import { pathToUrl } from "@/utils";

import api from "./api";
import { useSearchParams } from "next/navigation";

export type QueryKeyT = Readonly<
  | string[]
  | [string, object | null | undefined]
  | [string, ...string[], object | null | undefined]
>;

export interface GetInfinitePagesInterface<T> {
  data: T;
  total: number;
}

export const fetcher = <T>({
  queryKey,
}: QueryFunctionContext<[string, object | null | undefined]>): Promise<T> => {
  const [url, params] = queryKey;
  return api.get<T>(url, params, { paramsSerializer: { indexes: null } });
};

type ConfigQueryResult<T> = Partial<
  Omit<UseQueryOptions<T, AxiosError<IErrorData>, T, QueryKeyT>, "queryFn">
>;

export const useFetch = <T>(
  url: string | null,
  params?: object,
  config?: ConfigQueryResult<IResponse<T>>
) => {
  const context = useQuery<
    IResponse<T>,
    AxiosError<IErrorData>,
    IResponse<T>,
    QueryKeyT
  >({
    queryKey: [url!, params],
    queryFn: ({ ...args }) => fetcher({ ...args, queryKey: [url!, params] }),
    enabled: !!url,
    ...config,
  });

  return context;
};

export const useFetchList = <T>(
  url: string | null,
  params?: object,
  config?: ConfigQueryResult<IResponse<T>>
) => {
  const searchParams = useSearchParams();
  const page = searchParams.get("page") || DEFAULT_PAGE;
  const limit = searchParams.get("pageSize") || DEFAULT_PAGE_SIZE;

  return useFetch<IResponse<T>>(
    url,
    {
      ...params,
      page,
      limit,
    },
    config
  );
};

export const infiniteFetcher = <T>({
  queryKey,
  pageParam,
  pageLimit,
}: QueryFunctionContext<[string, object | null | undefined]> & {
  pageLimit?: number;
}): Promise<T> => {
  const [url, params] = queryKey;
  const isMobile = window.innerWidth < 768;
  let page = (params as any)?.page || pageParam;
  if (isMobile) {
    page = pageParam;
  }

  return api.get<T>(
    url,
    {
      ...params,
      page: page,
      limit: pageLimit || 10,
    },
    { paramsSerializer: { indexes: null } }
  );
};

type ConfigInfiniteQueryResult<T> = Partial<
  Omit<
    UseInfiniteQueryOptions<T, AxiosError<IErrorData>, T, QueryKeyT>,
    "queryFn" | "getNextPageParam" | "getPreviousPageParam" | "initialPageParam"
  >
>;

export const useFetchMore = <T>(
  url: string | null,
  params?: object,
  config?: ConfigInfiniteQueryResult<T>
) => {
  const context = useInfiniteQuery<
    IResponse<T>,
    AxiosError<IErrorData>,
    InfiniteData<IResponse<T>>,
    QueryKeyT
  >({
    queryKey: [url!, params],
    initialPageParam: 1,
    queryFn: ({ ...args }: any) =>
      infiniteFetcher({
        ...args,
        queryKey: [url!, params],
        // pageParam: (params as any)?.page || (args as any)?.pageParam,
        pageLimit: (params as any)?.limit,
      }),
    getNextPageParam: (
      lastPage: any,
      allPages: any,
      lastPageParam: any,
      allPageParams: any
    ) => {
      return Number(lastPage?.total) >
        allPages?.reduce?.((prev: any, acc: any) => {
          return prev + acc?.data?.length;
        }, 0)
        ? lastPageParam + 1
        : null;
    },
    enabled: config?.enabled || !!url,
    ...config,
  } as any);

  return context;
};

export const usePrefetch = <T>(url: string | null, params?: object) => {
  const queryClient = useQueryClient();

  return () => {
    if (!url) {
      return;
    }

    queryClient.prefetchQuery<
      IResponse<T>,
      AxiosError<IErrorData>,
      IResponse<T>,
      QueryKeyT
    >({
      queryKey: [url!, params],
      queryFn: ({ ...args }) => fetcher({ ...args, queryKey: [url!, params] }),
    });
  };
};

type ConfigMutationResult<T, S> = Omit<
  UseMutationOptions<S, AxiosError<IErrorData>, T | S>,
  "mutationFn" | "onMutate"
>;

const useGenericMutation = <T, S>(
  func: (data: T | S) => Promise<S>,
  url: string,
  params?: object,
  updater?: ((newData: T, oldData: S) => S | undefined) | undefined,
  config?: ConfigMutationResult<T, S>
) => {
  const queryClient = useQueryClient();

  return useMutation<S, AxiosError<IErrorData>, T | S>({
    mutationFn: func,
    onMutate: async (data) => {
      await queryClient.cancelQueries({ queryKey: [url!, params] });

      const previousData = queryClient.getQueryData([url!, params]);

      queryClient.setQueryData<S>([url!, params], (oldData) => {
        return updater
          ? updater(data as T, oldData as S)
          : (oldData as S | undefined);
      });

      return previousData;
    },
    onError: (err, _, context) => {
      queryClient.setQueryData([url!, params], context);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [url!, params] });
    },
    ...(config || {}),
  });
};

export const useDelete = <T extends string | number, S>(
  url: string,
  params?: object,
  updater?: (id: T, oldData: IResponse<S>) => IResponse<S> | undefined,
  mutateConfig?: ConfigMutationResult<T, IResponse<S>>
) => {
  return useGenericMutation<T, IResponse<S>>(
    (id) =>
      api.delete(pathToUrl(url as API_ROUTE_TYPE, { id: String(id) } as any)),
    url,
    params,
    updater,
    mutateConfig
  );
};

export const useDeleteById = <T extends object, S>(
  url: API_ROUTE_TYPE,
  params?: object,
  updater?: (id: T, oldData: IResponse<S>) => IResponse<S> | undefined,
  mutateConfig?: ConfigMutationResult<T, IResponse<S>>
) => {
  return useGenericMutation<T, IResponse<S>>(
    (ids) => api.delete(pathToUrl(url, ids as any)),
    url,
    params,
    updater,
    mutateConfig
  );
};

export const usePost = <T, S = {}>(
  url: string,
  params?: object,
  updater?: (newData: T, oldData: IResponse<S>) => IResponse<S>,
  config?: AxiosRequestConfig,
  mutateConfig?: ConfigMutationResult<T, IResponse<S>>
) => {
  return useGenericMutation<T, IResponse<S>>(
    (data) => api.post(url, data, config?.fileFlag, config),
    url,
    params,
    updater,
    mutateConfig
  );
};

export const usePostById = <T, S = {}>(
  url: API_ROUTE_TYPE,
  params?: object,
  updater?: (newData: T, oldData: IResponse<S>) => IResponse<S>,
  config?: AxiosRequestConfig,
  mutateConfig?: ConfigMutationResult<T, IResponse<S>>
) => {
  return useGenericMutation<T, IResponse<S>>(
    ({ key = "id", ...data }: any) =>
      api.post(
        pathToUrl(url, { [key]: String(data?.[key]) } as any),
        data,
        config?.fileFlag,
        config
      ),
    url,
    params,
    updater,
    mutateConfig
  );
};

export const usePut = <T, S = {}>(
  url: string,
  params?: object,
  updater?: (newData: T, oldData: IResponse<S>) => IResponse<S>,
  config?: AxiosRequestConfig,
  mutateConfig?: ConfigMutationResult<T, IResponse<S>>
) => {
  return useGenericMutation<T, IResponse<S>>(
    (data) => api.put(url, data, config?.fileFlag, config),
    url,
    params,
    updater,
    mutateConfig
  );
};
export const usePutById = <T, S = {}>(
  url: API_ROUTE_TYPE,
  params?: object,
  updater?: (newData: T, oldData: IResponse<S>) => IResponse<S>,
  config?: AxiosRequestConfig,
  mutateConfig?: ConfigMutationResult<T, IResponse<S>>
) => {
  return useGenericMutation<T, IResponse<S>>(
    (data: any) =>
      api.put(pathToUrl(url, data), data, config?.fileFlag, config),
    url,
    params,
    updater,
    mutateConfig
  );
};

export const usePatch = <T, S = {}>(
  url: string,
  params?: object,
  updater?: (newData: T, oldData: IResponse<S>) => IResponse<S>,
  config?: AxiosRequestConfig,
  mutateConfig?: ConfigMutationResult<T, IResponse<S>>
) => {
  return useGenericMutation<T, IResponse<S>>(
    (data) => api.patch(url, data, config?.fileFlag, config),
    url,
    params,
    updater,
    mutateConfig
  );
};
export const usePatchById = <T, S = {}>(
  url: API_ROUTE_TYPE,
  params?: object,
  updater?: (newData: T, oldData: IResponse<S>) => IResponse<S>,
  config?: AxiosRequestConfig,
  mutateConfig?: ConfigMutationResult<T, IResponse<S>>
) => {
  return useGenericMutation<T, IResponse<S>>(
    ({ key = "id", ...data }: any) =>
      api.patch(
        pathToUrl(url, { [key]: String(data?.[key]) } as any),
        data,
        config?.fileFlag,
        config
      ),
    url,
    params,
    updater,
    mutateConfig
  );
};
