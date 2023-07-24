import { fetchWrapper } from "./fetchWrapper";
import { config } from "../config/config";
import { User } from "./loginUserInterface";
import { RegisterUser } from "../store/users/interface";

const login = async (body: User) => {
  try {
    const response = await fetchWrapper.post<User>(
      `${config.baseURL}${config.login}`,
      body
    );
    return response;
  } catch (error) {
    throw error;
  }
};

const getUserRole = async (token: string) => {
  const response = await fetchWrapper.get(
    `${config.baseURL}${config.get_user_role}`,
    token
  );
  return response.data;
};

const registerUser = async (body: RegisterUser) => {
  const response = await fetchWrapper.post<RegisterUser>(
    `${config.baseURL}${config.register_user}`,
    body
  );
  return response.data;
};

export const authService = { login, getUserRole, registerUser };
