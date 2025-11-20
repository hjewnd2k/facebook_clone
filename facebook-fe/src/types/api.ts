import { User } from '@/stores/user.store';

import { ReactionType } from './enums';

export type IResponse<T> = T;

export type IApiResponse<T> = {
  code: string;
  message: string;
  result: T;
};

export type IRequestList = {
  page?: number;
  size?: number;
  search?: string;
};

export interface IErrorData {
  statusCode: number;
  message: string;
  code: string;
}

export type IPagination<T> = {
  currentPage: number;
  totalPages: number;
  pageSize: number;
  totalElements: number;
  data: T[];
};

export type IUserResponse = IApiResponse<User>;

export interface UserResponse {
  id: string;
  userId: string;
  username: string;
  displayName: string;
  profilePictureUrl: any;
  bio: any;
}

export interface NewFeed {
  id: string;
  userResponse: UserResponse;
  content: string;
  media: Media[];
  visibility: string;
  likeCount: number;
  commentCount: number;
}

export interface Media {
  type: string;
  url: string;
}

export type INewFeedResponse = IApiResponse<IPagination<NewFeed>>;

export interface IReactRequest {
  reactionType: ReactionType;
}
