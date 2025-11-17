export type IResponse<T> = T;

export interface IErrorData {
  statusCode: number;
  message: string;
  code: string;
}
