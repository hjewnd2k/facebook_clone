import { API_ROUTES } from '@/constants';
import { usePost } from '@/libs/reactQuery';
import { IReactRequest } from '@/types';
import { pathToUrl } from '@/utils';

export const useInteractionReact = (postId: string) =>
  usePost<IReactRequest, IReactRequest>(
    pathToUrl(API_ROUTES.INTERACTIONS.REACT, { postId }),
  );
