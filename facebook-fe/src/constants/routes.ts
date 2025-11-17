export const API_ROUTES = {};

type NestedValueOf<T> = T extends object
  ? { [K in keyof T]: NestedValueOf<T[K]> }[keyof T]
  : T;

export type API_ROUTE_TYPE = NestedValueOf<typeof API_ROUTES>;
