import { useState, FC, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Button,
  TextField,
  Typography,
  Modal,
  useTheme,
  Autocomplete,
} from "@mui/material";
import "./CreateAnnouncement.scss";
import { style } from "./ModalStyle";
import apiService from "../../services/apiService";
import { useDispatch } from "react-redux";
import { addNotification } from "../../store/notification/notificationSlice";

interface CreateAnnouncementProps {
  open: boolean;
  setOpen: (open: boolean) => void;
}

interface BuildingReq {
  buildingName: string;
  buildingId: number;
}

const token: string | null = localStorage.getItem("token");

const CreateAnnouncement: FC<CreateAnnouncementProps> = ({ open, setOpen }) => {
  const [managedBuildings, setManagedBuildings] = useState<BuildingReq[]>([]);
  const [formData, setFormData] = useState<{
    title: string;
    description: string;
    buildingId: number | null;
  }>({
    title: "",
    description: "",
    buildingId: null,
  });

  const theme = useTheme();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // const role = localStorage.getItem("role");
  // let user: boolean;
  // if (role == "1") {
  //   user = true;
  // }

  useEffect(() => {
    const fetchManagedBuildings = async () => {
      try {
        const response = await apiService.getManagedBuildings();
        setManagedBuildings(response.data);
      } catch (error) {}
    };
    fetchManagedBuildings();
  }, []);

  const handleClose = () => {
    setOpen(false);
  };

  const handleSubmit = async () => {
    setFormData((prevState) => ({
      ...prevState,
    }));
    try {
      const response = await apiService.createAnnouncement({
        ...formData,
        token,
      });
      dispatch(addNotification(response.data));
      window.location.reload();
      navigate("/announcements");
    } catch (error) {}

    handleClose();
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData((prevState) => ({
      ...prevState,
      [e.target.name]: e.target.value,
    }));
  };

  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={{ ...style, display: "flex", flexDirection: "column" }}>
        <Typography
          id="modal-modal-title"
          variant="h6"
          component="h2"
          sx={{
            color: theme.palette.primary.main,
          }}
        >
          Create Notification
        </Typography>
        <TextField
          required
          margin="normal"
          label="Title"
          name="title"
          value={formData.title}
          onChange={handleChange}
        />
        <TextField
          required
          margin="normal"
          label="Description"
          name="description"
          value={formData.description}
          onChange={handleChange}
        />
        <Autocomplete
          id="combo-box-demo"
          options={managedBuildings}
          getOptionLabel={(option: BuildingReq) => option.buildingName}
          renderInput={(params) => (
            <TextField {...params} label="Assign To" margin="normal" />
          )}
          value={managedBuildings.find(
            (building) => building.buildingId === formData.buildingId
          )}
          onChange={(event, newValue: BuildingReq | null) => {
            setFormData((prevState) => ({
              ...prevState,
              buildingId: newValue ? newValue.buildingId : null,
            }));
          }}
        />
        <Button
          onClick={handleSubmit}
          fullWidth
          variant="contained"
          color="primary"
          className="announcement-submit"
          disabled={
            !formData.title || !formData.description || !formData.buildingId
          }
        >
          Submit
        </Button>
      </Box>
    </Modal>
  );
};

export default CreateAnnouncement;
