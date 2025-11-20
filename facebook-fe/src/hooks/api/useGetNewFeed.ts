import { API_ROUTES } from '@/constants';
import { useFetchMore } from '@/libs/reactQuery';
import { INewFeedResponse } from '@/types';

export const useGetNewFeed = () =>
  useFetchMore<INewFeedResponse>(API_ROUTES.NEW_FEED.LIST);
