import React, { useState } from 'react';
import { TextField, Button, Box, Typography, Link, Card, CardContent, Grid } from '@mui/material';
import { useSnackbar } from "../uiComponents/Snackbar";
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import { useNavigate } from "react-router-dom";
import {  useLocation } from 'react-router-dom';
import * as Yup from "yup";
import { useFormik } from "formik";
import axios from '../../utils/axios';

const OtpVerification = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(1);
  const location = useLocation();

  const [open, setOpen] = useState(false);
  const { showSnackbar } = useSnackbar();
let regPayload = ""
  console.log(location.state.payload)
  if(location.state.payload){
    regPayload = location.state.payload
  }
  console.log(regPayload)
  const [emailValue, setEmailValue] = useState(regPayload.email);

  const handleClose = () => {
    setOpen(false);
    navigate("/");
  };

  // const validationSchemaEmail = Yup.object({
  //   emailId: Yup.string().required('Required').email("Invalid email address format"),
  // });

  const validationSchemaOtp = Yup.object({
    otp: Yup.string()
      .matches(/^[0-9]{6}$/, 'OTP must be exactly 6 digits')
      .required('OTP is required'),
  });

  // const formikEmail = useFormik({
  //   initialValues: {
  //     emailId: '',
  //   },
  //   validationSchema: validationSchemaEmail,
  //   onSubmit: (values) => {
  //     handleSendOtp(values);
  //   },
  // });

  const formikOtp = useFormik({
    initialValues: {
      otp: '',
    },
    validationSchema: validationSchemaOtp,
    onSubmit: (values) => {
      handleVerifyOtp(values);
    },
  });

  const handleSendOtp = async () => {
   
    const payload = {
      email: emailValue
    };
    try {
      const res = await axios.post("/user/send-otp", payload);
      if (res.data.status === true && res.data.statusCode === 200) {
        showSnackbar(res.data.message, "success");
        setStep(2);
      } else {
        showSnackbar(res.data.message, "warning");
      }
    } catch (error) {
      console.log(error);
      showSnackbar("Error", "error");
    }
  };

  const handleVerifyOtp = async (values) => {
    const payload = {
      otp: values.otp,
      email: emailValue  
    };
    try {
      const res = await axios.post("/user/verify-otp", payload);
      if (res.data.status === true && res.data.statusCode === 200) {
        showSnackbar(res.data.message, "success");
        if(res.data.result == true){
          try{

      const res = await axios.post("/user/new-user-registration", regPayload);
      console.log(res)
      if(res.data.status == true && res.data.statusCode == 200){
        showSnackbar(res.data.message, "success");
        setOpen(true);
      }
      else{
        showSnackbar(res.data.message, "warning");
      }
    }catch(error){
      console.log(error)
      showSnackbar("Error", "error");
    }
        }
        
      } else {
        showSnackbar(res.data.message, "warning");
      }
    } catch (error) {
      console.log(error);
      showSnackbar("Error", "error");
    }
  };

  const handleResendOtp = async () => {
    try {
      const payload = { email: emailValue };
      const res = await axios.post("/user/resend-otp", payload);
      if (res.data.status === true && res.data.statusCode === 200) {
        showSnackbar(res.data.message, "success");
        setStep(2);
      } else {
        showSnackbar(res.data.message, "warning");
      }
    } catch (error) {
      console.log(error);
      showSnackbar("Error", "error");
    }
  };

  return (
    <>
      <Card className='mini-card1'>
        <CardContent>
          <Typography variant="h4" gutterBottom textAlign="center">OTP Verification</Typography>
          {step === 1 && (
          
              <Grid container direction="row" rowSpacing={0} columnSpacing={2}>
                <Grid item xs={12} sm={12} md={12} lg={12}>
                  <TextField
                   label="Email Id"
                   value={emailValue}
                   fullWidth
                   margin="normal"
                   disabled  
                  />
                </Grid>
                <Grid item xs={12} sm={12} md={12} lg={12} textAlign="center">
                  <Button variant="contained" type="submit" sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }} onClick={()=> handleSendOtp()}>Send OTP</Button>
                </Grid>
              </Grid>
            
          )}

          {step === 2 && (
            <form onSubmit={formikOtp.handleSubmit}>
              <Grid container direction="row" rowSpacing={0} columnSpacing={2}>
                <Grid item xs={12} sm={12} md={12} lg={12}>
                  <TextField
                    label="Email Id"
                    value={emailValue}
                    fullWidth
                    margin="normal"
                    disabled  
                  />
                </Grid>
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
