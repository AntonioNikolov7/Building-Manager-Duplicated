export const config = {
  baseURL: "http://localhost:8080/",
  register_manager: "auth/register/manager",
  add_building: "building/create",
  login: "auth/login",
  api_all_countries: "https://restcountries.com/v3.1/all",
  get_user_role: "user/role",
  register_user: "auth/manager/register/user",
  create_announcement: "notification/send",
  get_notificationsById: (buildingId: string | undefined) =>
    `notification/building/${buildingId}`,
  get_managed_buildings: "user/managed/buildings",
  get_building_units: (buildingId: string | number) =>
    `units/managed/buildings/${buildingId}/units`,
  delete_announcement: (announcementId: number) =>
    `notification/delete/${announcementId}`,
  edit_announcement: (announcementId: number) =>
    `notification/edit/${announcementId}`,
  add_comment: (announcementId: number) => `comment/send/${announcementId}`,
  get_comments: (announcementId: number) =>
    `comment/notification/${announcementId}`,
  get_announcements_comments: (announcementId: number) =>
    `comment/notification/${announcementId}`,
  google_login: "login/oauth2/code/google",
};
