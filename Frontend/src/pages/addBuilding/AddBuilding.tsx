import React, { ChangeEvent, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Button,
  TextField,
  Typography,
  useTheme,
  useMediaQuery,
  Autocomplete,
} from "@mui/material";
import useAuthValidations from "../../common/utils";
import { FormValues } from "../../common/RegisterInterfaces";
import apiService from "../../services/apiService";
import { Building } from "../../services/buildingRegisterInterface";
import { Country } from "../../common/countryInterface";

const REQUIRED_FIELDS: (keyof FormValues)[] = [
  "buildingName",
  "country",
  "region",
  "city",
  "district",
  "postalCode",
  "streetName",
  "streetNumber",
  "entrances",
  "floors",
];

const AddBuilding: React.FC = () => {
  const theme = useTheme();
  const [countryNames, setCountryNames] = useState([]);
  const navigate = useNavigate();
  const matches = useMediaQuery(theme.breakpoints.down("sm"));

  const { formValues, setFormValues, formErrors, validateField } =
    useAuthValidations();

  useEffect(() => {
    const fetchCountryNames = async () => {
      try {
        const response = await apiService.getAllCountries();
        const data = response.data;
        const names = data.map((country: Country) => country.name.common);
        const sortedCountries = names.sort((a: string, b: string) =>
          a.localeCompare(b)
        );
        setCountryNames(sortedCountries);
      } catch (error) {
        throw error;
      }
    };
    fetchCountryNames();
  }, []);

  const handleAutocompleteChange = (
    fieldName: keyof FormValues,
    event: React.ChangeEvent<{}>,
    value: string | null
  ) => {
    setFormValues({
      ...formValues,
      [fieldName]: value,
    });
  };

  const handleSubmit = async (e: React.FormEvent): Promise<void> => {
    e.preventDefault();

    Object.keys(formValues).forEach((field) =>
      validateField(field as keyof FormValues)
    );

    const building: Building = {
      name: formValues.buildingName,
      floors: formValues.floors,
      entrances: formValues.entrances,
      address: {
        country: formValues.country,
        region: formValues.region,
        city: formValues.city,
        district: formValues.district,
        postalCode: formValues.postalCode,
        streetName: formValues.streetName,
        streetNumber: formValues.streetNumber,
      },
    };

    try {
      await apiService.addBuilding(building);
      navigate("/");
    } catch (error) {}
    navigate("/");
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

  const areAllFieldsFilled = (
    requiredFields: (keyof FormValues)[]
  ): boolean => {
    return requiredFields.every((field) => formValues[field] !== "");
  };

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
          mt: matches ? "16%" : "8%",
          fontFamily: theme.typography.fontFamily,
          fontWeight: 700,
          color: theme.palette.primary.main,
          textDecoration: "none",
        }}
      >
        Add Building
      </Typography>
      <Box
        component="form"
        onSubmit={handleSubmit}
        sx={{
          mt: "1%",
          width: matches ? "100%" : "40vw",
          display: "grid",
          gridTemplateColumns: matches ? "1fr" : "1fr 1fr",
          gap: 2,
        }}
      >
        <TextField
          placeholder="Building Name"
          variant="outlined"
          fullWidth
          value={formValues.buildingName}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("buildingName", e)
          }
          onBlur={() => validateField("buildingName")}
          error={!!formErrors.buildingName}
          helperText={formErrors.buildingName}
        />
        <Autocomplete
          options={countryNames}
          getOptionLabel={(option) => option}
          renderInput={(params) => (
            <TextField {...params} variant="outlined" placeholder="Country" />
          )}
          value={formValues.country}
          onChange={(e, value) => handleAutocompleteChange("country", e, value)}
          onBlur={() => validateField("country")}
          fullWidth
        />
        <TextField
          placeholder="Region"
          variant="outlined"
          fullWidth
          value={formValues.region}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("region", e)
          }
          onBlur={() => validateField("region")}
          error={!!formErrors.region}
          helperText={formErrors.region}
        />
        <TextField
          placeholder="City"
          variant="outlined"
          fullWidth
          value={formValues.city}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("city", e)
          }
          onBlur={() => validateField("city")}
          error={!!formErrors.city}
          helperText={formErrors.city}
        />
        <TextField
          placeholder="District"
          variant="outlined"
          fullWidth
          value={formValues.district}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("district", e)
          }
          onBlur={() => validateField("district")}
          error={!!formErrors.district}
          helperText={formErrors.district}
        />
        <TextField
          placeholder="Postal Code"
          variant="outlined"
          fullWidth
          value={formValues.postalCode === 0 ? "" : formValues.postalCode}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("postalCode", e)
          }
          onBlur={() => validateField("postalCode")}
          error={!!formErrors.postalCode}
          helperText={formErrors.postalCode}
        />
        <TextField
          placeholder="Street Name"
          variant="outlined"
          fullWidth
          value={formValues.streetName}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("streetName", e)
          }
          onBlur={() => validateField("streetName")}
          error={!!formErrors.streetName}
          helperText={formErrors.streetName}
        />
        <TextField
          placeholder="Street Number"
          variant="outlined"
          fullWidth
          value={formValues.streetNumber === 0 ? "" : formValues.streetNumber}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("streetNumber", e)
          }
          onBlur={() => validateField("streetNumber")}
          error={!!formErrors.streetNumber}
          helperText={formErrors.streetNumber}
        />
        <TextField
          placeholder="Entrances"
          variant="outlined"
          fullWidth
          value={formValues.entrances === 0 ? "" : formValues.entrances}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("entrances", e)
          }
          onBlur={() => validateField("entrances")}
          error={!!formErrors.entrances}
          helperText={formErrors.entrances}
        />
        <TextField
          placeholder="Floors"
          variant="outlined"
          fullWidth
          value={formValues.floors === 0 ? "" : formValues.floors}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            handleChange("floors", e)
          }
          onBlur={() => validateField("floors")}
          error={!!formErrors.floors}
          helperText={formErrors.floors}
        />
        <Button
          type="submit"
          variant="contained"
          color="primary"
          sx={{
            gridColumn: matches ? "1" : "1 / span 2",
            justifySelf: "center",
            width: "50%",
          }}
          disabled={!areAllFieldsFilled(REQUIRED_FIELDS)}
        >
          Add Building
        </Button>
      </Box>
    </Box>
  );
};

export default AddBuilding;
