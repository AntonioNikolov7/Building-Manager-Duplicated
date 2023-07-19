export interface Address {
  country: string;
  region: string;
  city: string;
  district: string;
  postalCode: number | null;
  streetName: string;
  streetNumber: number | null;
}

export interface BuildingResource {
  name: string;
  address: Address;
  floors: number;
  entrances: number;
}

export interface RegistrationRequest {
  password: string;
  email: string;
  firstName: string;
  lastName: string;
}

export interface CreateUser {
  buildingResource: BuildingResource;
  registrationRequestManager: RegistrationRequest;
}
