import React, { useState, useEffect } from 'react';
import { useFormik } from "formik";
import { useNavigate } from "react-router-dom";
import { Autocomplete, Card, TextField, Button, Typography, CardContent, Grid } from '@mui/material';
import * as Yup from "yup";
import { Checkbox, FormControlLabel } from "@mui/material";
import AlertConfirm from "react-alert-confirm";
import { useSnackbar } from "../uiComponents/Snackbar";
import "react-alert-confirm/lib/style.css";

const RegistrationForm = () => {
  const navigate = useNavigate();
  const { showSnackbar } = useSnackbar();
  const [agree, setAgree] = useState(false);
  const [roleTypeId, setRoleTypeId] = useState('');

  const roleTypeDropdown = [
    { value: "Government", id: 1 },
    { value: "Policy Makers", id: 2 },
    { value: "Donor", id: 3 },
    { value: "NGO", id: 4 },
    { value: "Corporate/Startup", id: 5 },
    { value: "Individual", id: 6 },
    { value: "School/Academic Institution", id: 7 },
  ];

  const commonValidationSchema = Yup.object({
    name: Yup.string().required('Required'),
    email: Yup.string().required('Required').email("Invalid email address format"),
    mobileNo: Yup.string().required('Required')
      .matches(/^[6-9]\d{9}$/, "Only 10 digits allowed and should start with 9,8,7 or 6")
      .min(10, 'Must be exactly 10 digits')
      .max(10, 'Must be exactly 10 digits'),
    password: Yup.string().required('Required'),
    roleTypeId: Yup.number().required('Required'),
    designation: Yup.string().required('Required'),
  });

  const validationSchemas = {
    1: commonValidationSchema.shape({
      department: Yup.string().required('Required'),
      ministry: Yup.string().required('Required'),
    }),
    2: commonValidationSchema.shape({
      companyName: Yup.string().required('Required'),
      companyUrl: Yup.string().required('Required'),
    }),
    default: commonValidationSchema.shape({
      companyName: Yup.string().required('Required'),
    }),
  };

  const getValidationSchema = (roleTypeId) => {
    return validationSchemas[roleTypeId] || validationSchemas.default;
  };


  const [validationSchema, setValidationSchema] = useState(getValidationSchema(roleTypeId));

  useEffect(() => {
    setValidationSchema(getValidationSchema(roleTypeId));
  }, [roleTypeId]);

  const formik = useFormik({
    initialValues: {
      name: '',
      email: '',
      mobileNo: '',
      password: '',
      roleTypeId: '',
      companyName: '',
      department: '',
      ministry: '',
      companyUrl: '',
      designation: '',
    },
    validationSchema: validationSchema,
    onSubmit: (values) => {
      console.log("Form submitted with values:", values);
      navigate("/otpVerify");
    },
  });

  const handleCheckboxChange = () => {
    setAgree(true);
  };

  const getValueFromList = (list, value) => {
    return list.find(option => option.id === value) ?? null;
  };

  const callConfirmDialog = async (values) => {
    const [action] = await AlertConfirm({
      title: "Confirm",
      desc: "Are you sure you want to submit the data?",
    });
    AlertConfirm.config({
      okText: "Yes",
      cancelText: "No",
    });
    if (action) {
      submitFormData(values);
    }
  };

  const SaveData = async (e, values) => {
    e.preventDefault();
    const touched = Object.keys(formik.initialValues).reduce((result, item) => {
      result[item] = true;
      return result;
    }, {});

    formik.setTouched(touched, false);
    formik.setSubmitting(true);

    formik.validateForm().then((formErrors) => {
      if (Object.keys(formErrors).length > 0) {
        console.log(formErrors);
        showSnackbar("Please enter all required fields", "error");
      } else {
        callConfirmDialog(values);
      }
    }).catch((err) => {
      formik.setSubmitting(false);
    });
  };

  const submitFormData = async (values) => {
    console.log(values);
    navigate("/otpVerify");
  };

  return (
    <div>
      <form onSubmit={formik.handleSubmit}>
        <Card sx={{ m: "20px 50px", p: 0 }}>
          <CardContent>
            <Typography variant="h3" textAlign="center" color="#4772D9" gutterBottom>
              Registration Form
            </Typography>
            <Grid container direction="row" rowSpacing={0} columnSpacing={2} justify="flex-end" alignItems="center" sx={{ mb: 2 }}>
              <Grid item xs={12} sm={4} md={4} lg={4}>
                <TextField
                  label="Name"
                  name="name"
                  value={formik.values.name}
                  error={formik.touched.name && Boolean(formik.errors.name)}
                  helperText={formik.touched.name && formik.errors.name}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  fullWidth
                  margin="normal"
                  required
                />
              </Grid>
              <Grid item xs={12} sm={4} md={4} lg={4}>
                <TextField
                  label="Email"
                  name="email"
                  value={formik.values.email}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={formik.touched.email && Boolean(formik.errors.email)}
                  helperText={formik.touched.email && formik.errors.email}
                  fullWidth
                  margin="normal"
                  required
                />
              </Grid>
              <Grid item xs={12} sm={4} md={4} lg={4}>
                <TextField
                  label="Mobile Number"
                  name="mobileNo"
                  value={formik.values.mobileNo}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={formik.touched.mobileNo && Boolean(formik.errors.mobileNo)}
                  helperText={formik.touched.mobileNo && formik.errors.mobileNo}
                  fullWidth
                  margin="normal"
                  required
                />
              </Grid>
              <Grid item xs={12} sm={4} md={4} lg={4}>
                <TextField
                  label="Password"
                  name="password"
                  type="password"
                  value={formik.values.password}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={formik.touched.password && Boolean(formik.errors.password)}
                  helperText={formik.touched.password && formik.errors.password}
                  fullWidth
                  margin="normal"
                  required
                />
              </Grid>
              <Grid item xs={12} sm={4} md={4} lg={4}>
                <Autocomplete
                  disablePortal
                  fullWidth
                  sx={{ mt: 1 }}
                  options={roleTypeDropdown}
                  getOptionLabel={(option) => option.value || ""}
                  value={getValueFromList(roleTypeDropdown, formik.values.roleTypeId) || ""}
                  onChange={(event, newValue) => {
                    formik.setFieldValue("roleTypeId", newValue ? newValue.id : '');
                    setRoleTypeId(newValue ? newValue.id : '');
                  }}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      required
                      label="Role Type"
                      error={formik.touched.roleTypeId && Boolean(formik.errors.roleTypeId)}
                      helperText={formik.touched.roleTypeId && formik.errors.roleTypeId}
                      onChange={formik.handleChange}
                      onBlur={formik.handleBlur}
                    />
                  )}
                />
              </Grid>
              {formik.values.roleTypeId === 1 && (
                <>
                  <Grid item xs={12} sm={4} md={4} lg={4}>
                    <TextField
                      label="Department Name"
                      name="department"
                      value={formik.values.department}
                      onChange={formik.handleChange}
                      onBlur={formik.handleBlur}
                      error={formik.touched.department && Boolean(formik.errors.department)}
                      helperText={formik.touched.department && formik.errors.department}
                      fullWidth
                      margin="normal"
                      required
                    />
                  </Grid>
                  <Grid item xs={12} sm={4} md={4} lg={4}>
                    <TextField
                      label="Ministry Name"
                      name="ministry"
                      value={formik.values.ministry}
                      onChange={formik.handleChange}
                      onBlur={formik.handleBlur}
                      error={formik.touched.ministry && Boolean(formik.errors.ministry)}
                      helperText={formik.touched.ministry && formik.errors.ministry}
                      fullWidth
                      margin="normal"
                      required
                    />
                  </Grid>
                </>
              )}
              <Grid item xs={12} sm={4} md={4} lg={4}>
                <TextField
                  label="Designation"
                  name="designation"
                  value={formik.values.designation}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={formik.touched.designation && Boolean(formik.errors.designation)}
                  helperText={formik.touched.designation && formik.errors.designation}
                  fullWidth
                  margin="normal"
                  required
                />
              </Grid>
              {formik.values.roleTypeId !== 1 && (
                <Grid item xs={12} sm={4} md={4} lg={4}>
                  <TextField
                    label="Company/Organization"
                    name="companyName"
                    value={formik.values.companyName}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.companyName && Boolean(formik.errors.companyName)}
                    helperText={formik.touched.companyName && formik.errors.companyName}
                    fullWidth
                    margin="normal"
                    required
                  />
                </Grid>
              )}
              {formik.values.roleTypeId === 2 && (
                <Grid item xs={12} sm={4} md={4} lg={4}>
                  <TextField
                    label="Company Url"
                    name="companyUrl"
                    value={formik.values.companyUrl}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.companyUrl && Boolean(formik.errors.companyUrl)}
                    helperText={formik.touched.companyUrl && formik.errors.companyUrl}
                    fullWidth
                    margin="normal"
                    required
                  />
                </Grid>
              )}
              <Grid item xs={12} sm={12} md={12} lg={12}>
                <FormControlLabel
                  control={
                    <Checkbox
                      name="agree"
                      checked={agree}
                      onChange={handleCheckboxChange}
                    />
                  }
                  label="I agree to that the Public Data Dashboard is for research purposes. I agree to not republish this data or utilized it for commercial use. For intended research, the platform has to be cited and the organization has to be informed"
                />
              </Grid>
            </Grid>
          </CardContent>
        </Card>
        <Grid container alignItems="center" justifyContent="center" marginTop="10px" marginBottom="10px">
          <Grid item>
            <Button
              type="submit"
              variant="contained"
              onClick={(e) => {
                SaveData(e, formik.values);
              }}
              sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)'}}
            >
              Sign Up
            </Button>
          </Grid>
        </Grid>
      </form>
    </div>
  );
};

export default RegistrationForm;
