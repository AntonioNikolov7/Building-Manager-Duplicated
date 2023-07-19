import axios, { AxiosResponse } from "axios";
import { config } from "../config/config";

const axiosInstance = axios.create({
  baseURL: config.baseURL,
});

type Methods = "get" | "post" | "delete";
type Headers = { "Content-Type": string; Authorization?: string };
type RequestOptions<T> = {
  method: Methods;
  headers?: Headers;
  data?: T;
};

const get = async (url: string, token?: string | undefined) => {
  const requestOptions: RequestOptions<undefined> = {
    method: "get",
    headers: token
      ? { "Content-Type": "application/json", Authorization: `Bearer ${token}` }
      : { "Content-Type": "application/json" },
  };
  return handleResponse(await axiosInstance(url, requestOptions));
};

const post = async <T>(url: string, data: T) => {
  const requestOptions: RequestOptions<T> = {
    method: "post",
    data,
    headers: { "Content-Type": "application/json" },
  };
  return handleResponse(await axiosInstance(url, requestOptions));
};

const _delete = async (url: string, token: string | undefined) => {
  const requestOptions: RequestOptions<{ token: string | undefined }> = {
    method: "delete",
    headers: { "Content-Type": "application/json" },
    data: { token },
  };
  return handleResponse(await axiosInstance(url, requestOptions));
};

const handleResponse = (response: AxiosResponse) => response;

export const fetchWrapper = {
  get,
  post,
  delete: _delete,
  handleResponse,
};
