import React, { useState, useEffect } from 'react';
import { useFormik } from "formik";
import { useNavigate } from "react-router-dom";
import { Autocomplete, Card, TextField, Button, Typography, CardContent, Grid } from '@mui/material';
import * as Yup from "yup";
import { Checkbox, FormControlLabel } from "@mui/material";
import AlertConfirm from "react-alert-confirm";
import { useSnackbar } from "../uiComponents/Snackbar";
import "react-alert-confirm/lib/style.css";
import IconButton from '@mui/material/IconButton';
import InputAdornment from '@mui/material/InputAdornment';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import CryptoJS from 'crypto-js';


const RegistrationForm = () => {
  const navigate = useNavigate();
  const { showSnackbar } = useSnackbar();

  const [roleTypeId, setRoleTypeId] = useState('');
  const [showPassword, setShowPassword] = useState(false);

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
    mobileNo: Yup.number()
    .required('Required')
    .typeError('Must be a number')
    .test('len', 'Must be exactly 10 digits', val => val && val.toString().length === 10),
    password: Yup.string().required('Required')
    .min(8, 'Password must be at least 8 characters long'),
    roleTypeId: Yup.number().required('Required'),
    designation: Yup.string().required('Required'),
    agree: Yup.boolean().oneOf([true], 'You must accept the terms and conditions').required('Required'),
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
      agree: false,
    },
    validationSchema: validationSchema,
    onSubmit: (values) => {
      console.log("Form submitted with values:", values);
      // navigate("/otpVerify");
    },
  });

 
 

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

  const encryptPass = (pass)=>{

    const key = "498aa575016fedc2";
    const iv = CryptoJS.enc.Utf8.parse("498aa575016fedc2")
    const securePass = CryptoJS.AES.encrypt(pass,CryptoJS.enc.Utf8.parse(key),{
      iv:iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7
    }).toString();
    return securePass; 

  };
 
  const submitFormData = async (values) => {

    let roleTypeDetails = {};

    switch (values.roleTypeId) {
      case 1: // Government
        roleTypeDetails = {
          department: values.department,
          ministry: values.ministry,
          designation: values.designation,
        };
        break;
      case 2: // Policy Makers
        roleTypeDetails = {
          companyName: values.companyName,
          companyUrl: values.companyUrl,
          designation: values.designation,
        };
        break;
      default: // Other role types
        roleTypeDetails = {
          companyName: values.companyName,
          designation: values.designation,
        };
        break;
    }
  
    let payload = {
      userName: values.name,
      email: values.email,
      password: encryptPass(values.password),
      mobileNo: values.mobileNo,
      roleId: values.roleTypeId,
      roleTypeDetails: roleTypeDetails,
    };
    if(payload){
      navigate("/otpVerify",{
        state:{
          payload:payload
        }
      });
    }
   
  };
  console.log(formik.values.roleTypeId)

  const handleClickShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const handleMouseDownPassword = (event) => {
    event.preventDefault();
  };

  return (
    <div>
      <form onSubmit={formik.handleSubmit}>
        <Card sx={{ m: "40px 200px", p: 0 }}>
          <CardContent>
            <Typography variant="h3" textAlign="center" color="#4772D9" gutterBottom>
              Registration Form
            </Typography>
            <Grid container direction="row" rowSpacing={0} columnSpacing={2} justify="flex-end" alignItems="center" sx={{ mb: 2 }}>
              <Grid item xs={12} sm={6} md={6} lg={6}>
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
              <Grid item xs={12} sm={6} md={6} lg={6}>
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
              <Grid item xs={12} sm={6} md={6} lg={6}>
              <TextField
              label="Mobile Number"
              name="mobileNo"
              type="tel"
              inputProps={{ 
                maxLength: 10, 
                
              }}
              inputMode="numeric" 
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
              <Grid item xs={12} sm={6} md={6} lg={6}>
                <TextField
                  label="Password"
                  name="password"
                  type={showPassword ? "text" : "password"}
                  value={formik.values.password}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={formik.touched.password && Boolean(formik.errors.password)}
                  helperText={formik.touched.password && formik.errors.password}
                  fullWidth
                  margin="normal"
                  required
                  InputProps={{
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          aria-label="toggle password visibility"
                          onClick={handleClickShowPassword}
                          onMouseDown={handleMouseDownPassword}
                          edge="end"
                        >
                          {showPassword ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    )
                  }}
                />
              </Grid>
              <Grid item xs={12} sm={6} md={6} lg={6}>
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
                  <Grid item xs={12} sm={6} md={6} lg={6}>
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
                  <Grid item xs={12} sm={6} md={6} lg={6}>
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
              
              {(formik.values.roleTypeId !== 1 && formik.values.roleTypeId !== "") && (
                <Grid item xs={12} sm={6} md={6} lg={6}>
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
                <Grid item xs={12} sm={6} md={6} lg={6}>
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
               {( formik.values.roleTypeId !== "") && (
              <Grid item xs={12} sm={6} md={6} lg={6}>
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
               )}
              <Grid item xs={12} sm={12} md={12} lg={12}>
              <FormControlLabel
                  control={
                    <Checkbox
                      name="agree"
                      checked={formik.values.agree}
                      onChange={formik.handleChange}
                      onBlur={formik.handleBlur}
                      color="primary"
                    />
                  }
                  label="I understand that the Public Data Dashboard is for research purpose, I agree to not republish this data or utilize it for commercial use. For intended research, the platform has to be cited and the organization has to be informed."
                />
              </Grid>
            </Grid>
            <Grid container alignItems="center" justifyContent="center" marginTop="10px" >
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
          </CardContent>
        </Card>
        
      </form>
    </div>
  );
};

export default RegistrationForm;
