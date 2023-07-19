export interface RegisterUser {
  unitId: number;
  firstName: string;
  lastName: string;
  email: string;
  buildingId: number;
}

export interface UsersState {
  users: RegisterUser[];
}
