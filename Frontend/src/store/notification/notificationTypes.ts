import { Building } from "../../services/buildingRegisterInterface";

export interface Notification {
  title: string;
  description: string;
  assignTo: Building;
}
