export const API_ROUTES = {
  USERS: {
    ME: '/users/me',
  },
  NEW_FEED: {
    LIST: '/feed',
  },
  INTERACTIONS: {
    REACT: '/interactions/posts/:postId/react',
  },
};

type NestedValueOf<T> = T extends object
  ? { [K in keyof T]: NestedValueOf<T[K]> }[keyof T]
  : T;

export type API_ROUTE_TYPE = NestedValueOf<typeof API_ROUTES>;
