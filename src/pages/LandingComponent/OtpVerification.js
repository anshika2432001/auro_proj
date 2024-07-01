import React, { useState } from 'react';
import { TextField, Button, Box, Typography, Link, Card, CardContent, Grid } from '@mui/material';
import { useSnackbar } from "../uiComponents/Snackbar";
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import { useNavigate } from "react-router-dom";
import * as Yup from "yup";
import { useFormik } from "formik";

const OtpVerification = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(1);
  const [open, setOpen] = useState(false);
  const { showSnackbar } = useSnackbar();
  const [mobileNumber, setMobileNumber] = useState('');
  const [otp, setOtp] = useState('');

  const handleClose = () => {
    setOpen(false);
    navigate("/");
  };

  const validationSchemaMobile = Yup.object({
    mobileNumber: Yup.number()
      .required('Required')
      .typeError('Must be a number')
      .test('len', 'Must be exactly 10 digits', val => val && val.toString().length === 10),
  });

  const validationSchemaOtp = Yup.object({
    otp: Yup.string()
      .matches(/^[0-9]{6}$/, 'OTP must be exactly 6 digits')
      .required('OTP is required'),
  });

  const formikMobile = useFormik({
    initialValues: {
      mobileNumber: '',
    },
    validationSchema: validationSchemaMobile,
    onSubmit: (values) => {
      handleSendOtp(values);
    },
  });

  const formikOtp = useFormik({
    initialValues: {
      otp: '',
    },
    validationSchema: validationSchemaOtp,
    onSubmit: (values) => {
      handleVerifyOtp(values);
    },
  });

  const handleSendOtp = (values) => {
    // Simulate sending OTP
    setStep(2);
    showSnackbar("Otp sent successfully", "success");
  };

  const handleVerifyOtp = (values) => {
    // Simulate OTP verification
    if (values.otp === '123456') {
      setOpen(true);
      showSnackbar("Registration Successful", "success");
    } else {
      showSnackbar("Invalid Otp. Please Try Again", "warning");
    }
  };

  const handleResendOtp = () => {
    // Simulate resending OTP
    showSnackbar("OTP resend successfully", "warning");
  };

  return (
    <>
      <Card className='mini-card1'>
        <CardContent>
          <Typography variant="h4" gutterBottom textAlign="center">OTP Verification</Typography>

          <form onSubmit={formikMobile.handleSubmit}>
            <Grid container direction="row" rowSpacing={0} columnSpacing={2}>
              <Grid item xs={12} sm={12} md={12} lg={12}>
                <TextField
                  label="Mobile Number"
                  name="mobileNumber"
                  type="tel"
                  inputProps={{ maxLength: 10 }}
                  value={formikMobile.values.mobileNumber}
                  onChange={formikMobile.handleChange}
                  onBlur={formikMobile.handleBlur}
                  fullWidth
                  margin="normal"
                  error={formikMobile.touched.mobileNumber && Boolean(formikMobile.errors.mobileNumber)}
                  helperText={formikMobile.touched.mobileNumber && formikMobile.errors.mobileNumber}
                  required
                />
              </Grid>
              {step === 1 && (
                <Grid item xs={12} sm={12} md={12} lg={12} textAlign="center">
                  <Button variant="contained" type="submit" sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }}>Send OTP</Button>
                </Grid>
              )}
            </Grid>
          </form>

          {step === 2 && (
            <form onSubmit={formikOtp.handleSubmit}>
              <Grid container direction="row" rowSpacing={0} columnSpacing={2}>
                <Grid item xs={12} sm={12} md={12} lg={12} textAlign="center">
                  <TextField
                    label="Enter OTP"
                    name="otp"
                    value={formikOtp.values.otp}
                    onChange={formikOtp.handleChange}
                    onBlur={formikOtp.handleBlur}
                    fullWidth
                    margin="normal"
                    error={formikOtp.touched.otp && Boolean(formikOtp.errors.otp)}
                    helperText={formikOtp.touched.otp && formikOtp.errors.otp}
                    required
                  />
                </Grid>
                <Grid item xs={12} sm={12} md={12} lg={12} display="flex" justifyContent="flex-end">
                  <Link href="#" onClick={handleResendOtp}>Resend OTP</Link>
                </Grid>
                <Grid item xs={12} sm={12} md={12} lg={12} textAlign="center">
                  <Button variant="contained" type="submit" sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }}>Verify OTP</Button>
                </Grid>
              </Grid>
            </form>
          )}
        </CardContent>
      </Card>

      {open && (
        <Dialog
          open={open}
          onClose={handleClose}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description"
          PaperProps={{ sx: { borderRadius: "20px" } }}
        >
          <DialogContent>
            <DialogContentText id="alert-dialog-description">
              <Typography variant="h4" color="#4772D9">Registration Successful</Typography>
            </DialogContentText>
          </DialogContent>
          <DialogActions sx={{ backgroundColor: "#EFF7FF", alignItems: "center", justifyContent: "center" }}>
            <Button variant="contained" onClick={handleClose}>Go to Login</Button>
          </DialogActions>
        </Dialog>
      )}
    </>
  );
};

export default OtpVerification;
