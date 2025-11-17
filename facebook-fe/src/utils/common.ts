/* eslint-disable @typescript-eslint/no-explicit-any */
import { compile, pathToRegexp } from "path-to-regexp";

export const pathToUrl = (url: string, params: any): string => {
  const { keys } = pathToRegexp(url as string);
  // check keys in url has value in params object
  if (keys.every((key: any) => (params as any)[key.name])) {
    // Instead remove unnecessary property in params object, i create a new one and assign the needed key
    const newParams: any = {};
    keys.map((key: any) => {
      const value = (params as any)[key.name];
      newParams[key.name] = typeof value === "number" ? String(value) : value;
    });
    return compile(url as string)(newParams);
  }
  return "";
};
