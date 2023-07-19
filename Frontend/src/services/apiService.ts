import { fetchWrapper } from "./fetchWrapper";
import { config } from "../config/config";
import { Building } from "./buildingRegisterInterface";
import { CreateUser } from "../pages/register/registerManager/interfaces";
import { Notification } from "../pages/notifications/NotificationInterface";
const token: string | undefined = localStorage.getItem("token") || undefined;
const apiService = {
  getAllCountries: async () => {
    const countries = await fetchWrapper.get(config.api_all_countries);
    return countries;
  },

  registerManager: async (manager: CreateUser) => {
    try {
      const response = await fetchWrapper.post<CreateUser>(
        config.register_manager,
        manager
      );
      return response;
    } catch (error) {
      throw error;
    }
  },
  addBuilding: async (building: Building) => {
    try {
      const response = await fetchWrapper.post<Building>(
        config.add_building,
        building
      );
      return response;
    } catch (error) {
      throw error;
    }
  },

  createAnnouncement: async (notification: Notification) => {
    try {
      const response = await fetchWrapper.post<Notification>(
        config.create_announcement,
        notification
      );
      return response;
    } catch (error) {
      throw error;
    }
  },
  deleteAnnouncement: async (
    announcementId: number,
    token: string | undefined
  ) => {
    try {
      console.log(token);
      const response = await fetchWrapper.delete(
        config.delete_announcement(announcementId),
        token
      );
      return response;
    } catch (error) {
      console.error(error);
    }
  },
  editAnnouncement: async (
    announcementId: number,
    token: string | undefined
  ) => {
    try {
      const response = await fetchWrapper.post(
        config.edit_announcement(announcementId),
        token
      );
      return response;
    } catch (error) {}
  },
  postComment: async (
    announcementId: number,
    comment: string,
    token: string | undefined
  ) => {
    try {
      console.log(token);
      const response = await fetchWrapper.post(
        config.add_comment(announcementId),
        {
          text: comment,
          token: token,
        }
      );
      return response;
    } catch (error) {
      throw error;
    }
  },

  getComments: async (announcementId: number) => {
    try {
      const response = await fetchWrapper.get(
        config.get_comments(announcementId)
      );
      return response;
    } catch (error) {}
  },

  getNotificationsByBuildingId: async () => {
    const buildingId: string | undefined =
      localStorage.getItem("buildingId") || undefined;
    try {
      const response = await fetchWrapper.get(
        config.get_notificationsById(buildingId)
      );
      return response;
    } catch (error) {}
  },

  getManagedBuildings: async () => {
    try {
      const response = await fetchWrapper.get(
        config.get_managed_buildings,
        token
      );
      return response;
    } catch (error) {
      throw error;
    }
  },
  getUnitByBuildingId: async () => {
    const buildingId: string | undefined =
      localStorage.getItem("buildingId") || undefined;
    try {
      if (buildingId) {
        const response = await fetchWrapper.get(
          config.get_building_units(buildingId)
        );
        return response;
      } else {
        throw new Error("Building ID not found");
      }
    } catch (error) {
      console.error(error);
      throw error;
    }
  },
};

export default apiService;
