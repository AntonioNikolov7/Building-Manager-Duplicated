import { FC, useEffect, useState } from "react";
import {
  CssBaseline,
  Container,
  Typography,
  useTheme,
  Box,
} from "@mui/material";
import NotificationsOffIcon from "@mui/icons-material/NotificationsOff";
import NotificationCard from "../../components/NotificationUI/NotificationCard";
import apiService from "../../services/apiService";
import { NotificationCardProps } from "../../components/NotificationUI/notificationCardProps";

interface NotificationProps {
  currentUser: boolean;
}

const Notifications: FC<NotificationProps> = ({ currentUser }) => {
  const [notifications, setNotifications] = useState<NotificationCardProps[]>(
    []
  );
  const theme = useTheme();

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const response = await apiService.getNotificationsByBuildingId();
        if (response && response.data) {
          setNotifications(response.data);
        } else {
          console.error("No response data");
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchNotifications();
  }, []);

  if (notifications.length === 0) {
    return (
      <Box
        sx={{
          width: "100%",
          height: "100vh",
          bgcolor: theme.palette.background.default,
          color: theme.palette.text.primary,
        }}
      >
        <Container
          maxWidth="sm"
          style={{
            textAlign: "center",
          }}
        >
          <NotificationsOffIcon
            style={{
              marginTop: "200px",
              fontSize: 60,
              color: theme.palette.primary.main,
            }}
          />
          <Typography
            variant="h5"
            style={{ color: theme.palette.primary.main }}
          >
            {!currentUser
              ? " There are no announcements to display"
              : " There are no notifications to display"}
          </Typography>
        </Container>
      </Box>
    );
  }

  return (
    <>
      <CssBaseline />
      <Container maxWidth="sm">
        {notifications
          .slice()
          .reverse()
          .map((notification) => (
            <NotificationCard
              id={notification.id}
              key={notification.id}
              title={notification.title}
              description={notification.description}
              date={notification.date}
            />
          ))}
      </Container>
    </>
  );
};
export default Notifications;
