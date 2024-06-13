import React, { useState } from 'react';
import { TextField, Button, Box, Typography, Container } from '@mui/material';

const ForgotPassword = () => {
    const [step, setStep] = useState(1);
    const [emailOrPhone, setEmailOrPhone] = useState('');
    const [otp, setOtp] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const handleSubmitEmailOrPhone = () => {
        // Implement the API call to send OTP
        console.log('OTP sent to:', emailOrPhone);
        setStep(2);
    };

    const handleSubmitOtp = () => {
        // Implement the API call to verify OTP
        console.log('OTP entered:', otp);
        setStep(3);
    };

    const handleSubmitNewPassword = () => {
        if (newPassword !== confirmPassword) {
            alert('Passwords do not match!');
            return;
        }
        // Implement the API call to update the password
        console.log('New password set:', newPassword);
        setStep(4);
    };

    const renderStep = () => {
        switch (step) {
            case 1:
                return (
                    <Box>
                        <Typography variant="h6">Forgot Password</Typography>
                        <TextField
                            label="Email or Phone Number"
                            value={emailOrPhone}
                            onChange={(e) => setEmailOrPhone(e.target.value)}
                            fullWidth
                            margin="normal"
                        />
                        <Button variant="contained" color="primary" onClick={handleSubmitEmailOrPhone}>
                            Submit
                        </Button>
                    </Box>
                );
            case 2:
                return (
                    <Box>
                        <Typography variant="h6">Enter OTP</Typography>
                        <TextField
                            label="OTP"
                            value={otp}
                            onChange={(e) => setOtp(e.target.value)}
                            fullWidth
                            margin="normal"
                        />
                        <Button variant="contained" color="primary" onClick={handleSubmitOtp}>
                            Verify OTP
                        </Button>
                    </Box>
                );
            case 3:
                return (
                    <Box>
                        <Typography variant="h6">Reset Password</Typography>
                        <TextField
                            label="New Password"
                            type="password"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            fullWidth
                            margin="normal"
                        />
                        <TextField
                            label="Confirm Password"
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            fullWidth
                            margin="normal"
                        />
                        <Button variant="contained" color="primary" onClick={handleSubmitNewPassword}>
                            Reset Password
                        </Button>
                    </Box>
                );
            case 4:
                return (
                    <Box>
                        <Typography variant="h6">Password Reset Successful!</Typography>
                        <Button variant="contained" color="primary" href="/login">
                            Go to Login
                        </Button>
                    </Box>
                );
            default:
                return null;
        }
    };

    return (
        <Container maxWidth="sm">
            <Box mt={5}>
                {renderStep()}
            </Box>
        </Container>
    );
};

export default ForgotPassword;