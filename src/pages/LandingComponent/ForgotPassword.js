import React, { useState } from 'react';
import { TextField, Button, Box, Typography, Container, CardContent, Card, Grid,Link } from '@mui/material';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import Login from './Login';
import { useSnackbar } from "../uiComponents/Snackbar";

const ForgotPassword = () => {
    const [step, setStep] = useState(1);
    const [isLoginDialogVisible, setLoginDialogVisible] = useState(false);
    const { showSnackbar } = useSnackbar();
    const toggleLoginDialog = () => {
        setLoginDialogVisible(!isLoginDialogVisible);
    };

    // Custom validation for email or phone number
    const emailOrPhoneSchema = Yup.string()
        .test(
            'emailOrPhone',
            'Invalid email or phone number',
            value => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value) || /^\d{10}$/.test(value)
        )
        .required('Required');

    const formikEmailOrPhone = useFormik({
        initialValues: { emailOrPhone: '' },
        validationSchema: Yup.object({
            emailOrPhone: emailOrPhoneSchema,
        }),
        onSubmit: (values) => {
            console.log('OTP sent to:', values.emailOrPhone);
            setStep(2);
        },
    });

    const formikOtp = useFormik({
        initialValues: { otp: '' },
        validationSchema: Yup.object({
            otp: Yup.string()
                .required('Required')
                .matches(/^\d{6}$/, 'Invalid OTP'),
        }),
        onSubmit: (values) => {
            console.log('OTP entered:', values.otp);
            // Simulate OTP verification here
            if (values.otp === '123456') {
                setStep(3);
            } else {
                alert('Invalid OTP');
            }
        },
    });

    const formikNewPassword = useFormik({
        initialValues: { newPassword: '', confirmPassword: '' },
        validationSchema: Yup.object({
            newPassword: Yup.string()
                .required('Required')
                .min(8, 'Password must be at least 8 characters long'),
            confirmPassword: Yup.string()
                .required('Required')
                .oneOf([Yup.ref('newPassword'), null], 'Passwords must match'),
        }),
        onSubmit: (values) => {
            console.log('New password set:', values.newPassword);
            setStep(4);
        },
    });

    const handleResendOtp = () => {
 
        showSnackbar("OTP resend successfully", "warning");
      };

    return (
        <Container maxWidth="sm">
            <Box>
               
                        
                        {step < 3 && (
                             <Card className='mini-card1'>
                    <CardContent>
                            <form onSubmit={step === 1 ? formikEmailOrPhone.handleSubmit : formikOtp.handleSubmit}>
                                <Typography variant="h4" gutterBottom textAlign="center">Forgot Password</Typography>
                                <Grid container direction="row" rowSpacing={0} columnSpacing={2}>
                                    <Grid item xs={12} sm={12} md={12} lg={12}>
                                        <TextField
                                            label="Email or Phone Number"
                                            name="emailOrPhone"
                                            value={formikEmailOrPhone.values.emailOrPhone}
                                            onChange={formikEmailOrPhone.handleChange}
                                            onBlur={formikEmailOrPhone.handleBlur}
                                            error={formikEmailOrPhone.touched.emailOrPhone && Boolean(formikEmailOrPhone.errors.emailOrPhone)}
                                            helperText={formikEmailOrPhone.touched.emailOrPhone && formikEmailOrPhone.errors.emailOrPhone}
                                            fullWidth
                                            margin="normal"
                                            disabled={step > 1}
                                        />
                                    </Grid>
                                    {step === 2 && (
                                        <>
                                        <Grid item xs={12} sm={12} md={12} lg={12}>
                                            <TextField
                                                label="Enter OTP"
                                                name="otp"
                                                value={formikOtp.values.otp}
                                                onChange={formikOtp.handleChange}
                                                onBlur={formikOtp.handleBlur}
                                                error={formikOtp.touched.otp && Boolean(formikOtp.errors.otp)}
                                                helperText={formikOtp.touched.otp && formikOtp.errors.otp}
                                                fullWidth
                                                margin="normal"
                                            />
                                        </Grid>
                                        <Grid item xs={12} sm={12} md={12} lg={12} display="flex" justifyContent="flex-end">
                                        <Link href="#" onClick={handleResendOtp}>Resend OTP</Link>
                                      </Grid>
                                      </>
                                    )}
                                    <Grid item xs={12} sm={12} md={12} lg={12} textAlign="center">
                                        <Button
                                            variant="contained"
                                            type="submit"
                                            sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }}
                                        >
                                            {step === 1 ? 'Submit' : 'Verify OTP'}
                                        </Button>
                                    </Grid>
                                    {/* {step === 2 && (
                                        <Grid item xs={12} sm={12} md={12} lg={12} textAlign="center">
                                            <Button
                                                variant="text"
                                                onClick={() => {
                                                    console.log('Resend OTP');
                                                }}
                                            >
                                                Resend OTP
                                            </Button>
                                        </Grid>
                                    )} */}
                                </Grid>
                            </form>
                            </CardContent>
                </Card>

                        )}
                 
                {step === 3 && (
                    <Card className='mini-card1'>
                        <CardContent>
                            <Typography variant="h4" gutterBottom textAlign="center">Reset Password</Typography>
                            <form onSubmit={formikNewPassword.handleSubmit}>
                                <Grid container direction="row" rowSpacing={0} columnSpacing={2}>
                                    <Grid item xs={12} sm={12} md={12} lg={12}>
                                        <TextField
                                            label="New Password"
                                            type="password"
                                            name="newPassword"
                                            value={formikNewPassword.values.newPassword}
                                            onChange={formikNewPassword.handleChange}
                                            onBlur={formikNewPassword.handleBlur}
                                            error={formikNewPassword.touched.newPassword && Boolean(formikNewPassword.errors.newPassword)}
                                            helperText={formikNewPassword.touched.newPassword && formikNewPassword.errors.newPassword}
                                            fullWidth
                                            margin="normal"
                                        />
                                    </Grid>
                                    <Grid item xs={12} sm={12} md={12} lg={12}>
                                        <TextField
                                            label="Confirm Password"
                                            type="password"
                                            name="confirmPassword"
                                            value={formikNewPassword.values.confirmPassword}
                                            onChange={formikNewPassword.handleChange}
                                            onBlur={formikNewPassword.handleBlur}
                                            error={formikNewPassword.touched.confirmPassword && Boolean(formikNewPassword.errors.confirmPassword)}
                                            helperText={formikNewPassword.touched.confirmPassword && formikNewPassword.errors.confirmPassword}
                                            fullWidth
                                            margin="normal"
                                        />
                                    </Grid>
                                    <Grid item xs={12} sm={12} md={12} lg={12} textAlign="center">
                                        <Button
                                            variant="contained"
                                            type="submit"
                                            sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }}
                                        >
                                            Reset Password
                                        </Button>
                                    </Grid>
                                </Grid>
                            </form>
                        </CardContent>
                    </Card>
                )}

                {step === 4 && (
                    <Card className='mini-card1'>
                        <CardContent>
                            <Typography variant="h4" marginBottom="30px" textAlign="center">Password Reset Successful</Typography>
                            <Grid container direction="row" rowSpacing={0} columnSpacing={2}>
                                <Grid item xs={12} sm={12} md={12} lg={12} textAlign="center">
                                    <Button
                                        variant="contained"
                                        type="submit"
                                        sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }}
                                        onClick={toggleLoginDialog}
                                    >
                                        Go to Login
                                    </Button>
                                </Grid>
                            </Grid>
                        </CardContent>
                    </Card>
                )}
            </Box>
            <Login isOpen={isLoginDialogVisible} onClose={toggleLoginDialog} />
        </Container>
    );
};

export default ForgotPassword;
