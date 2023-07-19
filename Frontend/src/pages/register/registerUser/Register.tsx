import React, { ChangeEvent, useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Button,
  TextField,
  Typography,
  useTheme,
  Autocomplete,
} from "@mui/material";
import SelectField from "../SelectField";
import useAuthValidations from "../../../common/utils";
import { FormValues } from "../../../common/RegisterInterfaces";
import { RegisterUser } from "../../../store/users/interface";
import apiService from "../../../services/apiService";
import { authService } from "../../../services/authService";

const REQUIRED_FIELDS: (keyof FormValues)[] = [
  "firstName",
  "lastName",
  "email",
  "building",
  "unit",
];

interface Unit {
  unitNumber: number;
  id: number;
}

const Register: React.FC = () => {
  const theme = useTheme();
  const [managedBuildings, setManagedBuildings] = useState([]);
  const [units, setUnits] = useState<Unit[]>([]);
  const buildingId = localStorage.getItem("buildingId");

  const {
    formValues,
    setFormValues,
    formErrors,
    validateField,
    setBuilding,
    setUnit,
  } = useAuthValidations();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchManagedBuildings = async () => {
      try {
        const response = await apiService.getManagedBuildings();
        localStorage.setItem("buildingId", response.data[0].buildingId);
        const buildingNames = response.data.map(
          (b: { buildingName: string }) => b.buildingName
        );
        setManagedBuildings(buildingNames);
      } catch (error) {}
    };

    fetchManagedBuildings();
  }, []);

  useEffect(() => {
    if (managedBuildings) {
      const getBuildingUnits = async () => {
        try {
          const response = await apiService.getUnitByBuildingId();

          setUnits(response.data);
        } catch (error) {
          console.error(error);
        }
      };
      getBuildingUnits();
    }
  }, [managedBuildings]);

  const selectUnit = (unitNumber: number) => {
    const selectedUnit = units.find((unit) => unit.unitNumber === unitNumber);
    setFormValues((prevValues) => ({
      ...prevValues,
      unit: selectedUnit ? selectedUnit.id : 0,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    Object.keys(formValues).forEach((field) =>
      validateField(field as keyof FormValues)
    );

    const userValues = {
      unitId: formValues.unit,
      firstName: formValues.firstName,
      lastName: formValues.lastName,
      email: formValues.email,
      buildingId: Number(buildingId),
    };

    const newUser: RegisterUser = {
      ...userValues,
    };
    const registerUser = async () => {
      try {
        console.log(userValues);
        const response = await authService.registerUser(newUser);
      } catch (error) {}
    };
    registerUser();
    navigate("/users");
  };

  const areAllFieldsFilled = (
    requiredFields: (keyof FormValues)[]
  ): boolean => {
    return requiredFields.every((field) => formValues[field] !== "");
  };

  const handleChange = (
    fieldName: keyof FormValues,
    event: ChangeEvent<HTMLInputElement>
  ) => {
    setFormValues({
      ...formValues,
      [fieldName]: event.target.value,
    });
  };

  let selectedUnit = units.find((unit) => unit.id === formValues.unit);

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      height="100vh"
      bgcolor={theme.palette.background.default}
      overflow="auto"
    >
      <Typography
        variant="h4"
        component="h1"
        gutterBottom
        sx={{
          mt: "8%",
          fontFamily: theme.typography.fontFamily,
          fontWeight: 700,
          color: theme.palette.primary.main,
          textDecoration: "none",
        }}
      >
        Add User
      </Typography>
      <Box
        component="form"
        onSubmit={handleSubmit}
        sx={{
          mt: "1%",
          height: "100vh",
          width: "40vw",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: 3,
        }}
      >
        <TextField
          placeholder="First Name"
          variant="outlined"
          sx={{ width: "100%" }}
          value={formValues.firstName}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("firstName", e)
          }
          onBlur={() => validateField("firstName")}
          error={!!formErrors.firstName}
          helperText={formErrors.firstName}
        />
        <TextField
          placeholder="Last Name"
          variant="outlined"
          sx={{ width: "100%" }}
          value={formValues.lastName}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("lastName", e)
          }
          onBlur={() => validateField("lastName")}
          error={!!formErrors.lastName}
          helperText={formErrors.lastName}
        />
        <TextField
          placeholder="Email"
          variant="outlined"
          sx={{ width: "100%" }}
          value={formValues.email}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("email", e)
          }
          onBlur={() => validateField("email")}
          error={!!formErrors.email}
          helperText={formErrors.email}
        />
        <Autocomplete<string | number>
          id="combo-box-demo"
          options={managedBuildings}
          sx={{ width: "100%" }}
          getOptionLabel={(option) => option.toString()}
          renderInput={(params) => (
            <TextField {...params} label="Building" margin="normal" />
          )}
          value={formValues.building}
          onChange={(event, newValue) => setBuilding(newValue || "")}
        />
        <SelectField
          label="Unit"
          items={units.map((unit) => unit.unitNumber.toString())}
          value={selectedUnit ? selectedUnit.unitNumber.toString() : ""}
          onChange={(value) => selectUnit(Number(value))}
        />
        <Button
          type="submit"
          variant="contained"
          color="primary"
          sx={{ width: "100%" }}
          disabled={!areAllFieldsFilled(REQUIRED_FIELDS)}
        >
          Register
        </Button>
      </Box>
    </Box>
  );
};

export default Register;
