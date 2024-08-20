import React, { useState } from 'react';
import { Card, CardContent, Typography, Box, Button, Dialog, DialogTitle, DialogContent, TextField, DialogActions } from '@mui/material';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import CryptoJS from 'crypto-js';
import { sha256 } from "js-sha256";
import axios from '../../../utils/axios';
import { useSnackbar } from "../../uiComponents/Snackbar";
import { IconButton, InputAdornment } from '@mui/material';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';

const ProfilePage = () => {
  const user = {
    name: localStorage.getItem('userName'),
    email: localStorage.getItem('email'),
    role: localStorage.getItem('roleName'),
  };

  const { showSnackbar } = useSnackbar();
  const [openDialog, setOpenDialog] = useState(false);
  const [showCurrentPassword, setShowCurrentPassword] = useState(false);
const [showNewPassword, setShowNewPassword] = useState(false);
const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const randomString = () => {
    let length = 100;
    let chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    let result = "";
    for (let i = length; i > 0; --i)
      result += chars[Math.floor(Math.random() * chars.length)];
    return result;
  };

  const toggleShowCurrentPassword = () => setShowCurrentPassword(!showCurrentPassword);
const toggleShowNewPassword = () => setShowNewPassword(!showNewPassword);
const toggleShowConfirmPassword = () => setShowConfirmPassword(!showConfirmPassword);

  const hashPassword = (passwordPlainText) => {
    const generatedSalt = randomString();
    const hash = sha256.hmac(generatedSalt, passwordPlainText);
    return { hash, generatedSalt };
  };

  const encryptPass = (pass) => {
    const key = "498aa575016fedc2";
    const iv = CryptoJS.enc.Utf8.parse("498aa575016fedc2");
    const securePass = CryptoJS.AES.encrypt(pass, CryptoJS.enc.Utf8.parse(key), {
      iv: iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7
    }).toString();
    return securePass;
  };

  const handleOpenDialog = () => {
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    formik.resetForm();
  };

  const formik = useFormik({
    initialValues: {
      currentPassword: '',
      newPassword: '',
      newPassword2: ''
    },
    validationSchema: Yup.object({
      currentPassword: Yup.string().required('Old password is required'),
      newPassword: Yup.string()
        .required('New password is required')
        .min(8, 'Password must be at least 8 characters long'),
      newPassword2: Yup.string()
        .required('Confirm your new password')
        .oneOf([Yup.ref('newPassword'), null], 'Passwords must match')
    }),
    onSubmit: async (values) => {
      const { hash: hashedCurrentPassword, generatedSalt } = hashPassword(values.currentPassword);
      const encryptedNewPassword = encryptPass(values.newPassword);

      let payload = {
        currentPassword: hashedCurrentPassword,
        newPassword: encryptedNewPassword,
        newPassword2: generatedSalt, // Directly use generatedSalt here
        userId: localStorage.getItem('userId'),
      };

      try {
        const response = await axios.post("/user/update-password", payload);
        console.log(response.data);
        if (response.data.status == true && response.data.statusCode == 200) {
          showSnackbar(response.data.message, "success");
        } else {
          showSnackbar(response.data.message, "error");
        }
        handleCloseDialog();
      } catch (error) {
        console.error(error);
        showSnackbar("Oops, something went wrong", "error");
      }
    },
  });

  return (
    <Box>
      <Card>
        <CardContent sx={{ textAlign: 'center' }}>
          <Typography variant="h5" component="div" sx={{ marginTop: 2 }}>
            Name: {user.name}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Email: {user.email}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Role: {user.role}
          </Typography>
          <Button
            variant="contained"
            color="primary"
            sx={{ marginTop: 3 }}
            onClick={handleOpenDialog}
          >
            Change Password
          </Button>
        </CardContent>
      </Card>

      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>Change Password</DialogTitle>
        <DialogContent>
          <form onSubmit={formik.handleSubmit}>
          <TextField
  margin="dense"
  label="Old Password"
  type={showCurrentPassword ? 'text' : 'password'} // Toggle between 'text' and 'password'
  fullWidth
  name="currentPassword"
  value={formik.values.currentPassword}
  onChange={formik.handleChange}
  onBlur={formik.handleBlur}
  error={formik.touched.currentPassword && Boolean(formik.errors.currentPassword)}
  helperText={formik.touched.currentPassword && formik.errors.currentPassword}
  InputProps={{
    endAdornment: (
      <InputAdornment position="end">
        <IconButton onClick={toggleShowCurrentPassword} edge="end">
          {showCurrentPassword ? <VisibilityOff /> : <Visibility />}
        </IconButton>
      </InputAdornment>
    ),
  }}
/>

<TextField
  margin="dense"
  label="New Password"
  type={showNewPassword ? 'text' : 'password'}
  fullWidth
  name="newPassword"
  value={formik.values.newPassword}
  onChange={formik.handleChange}
  onBlur={formik.handleBlur}
  error={formik.touched.newPassword && Boolean(formik.errors.newPassword)}
  helperText={formik.touched.newPassword && formik.errors.newPassword}
  InputProps={{
    endAdornment: (
      <InputAdornment position="end">
        <IconButton onClick={toggleShowNewPassword} edge="end">
          {showNewPassword ? <VisibilityOff /> : <Visibility />}
        </IconButton>
      </InputAdornment>
    ),
  }}
/>

<TextField
  margin="dense"
  label="Confirm New Password"
  type={showConfirmPassword ? 'text' : 'password'}
  fullWidth
  name="newPassword2"
  value={formik.values.newPassword2}
  onChange={formik.handleChange}
  onBlur={formik.handleBlur}
  error={formik.touched.newPassword2 && Boolean(formik.errors.newPassword2)}
  helperText={formik.touched.newPassword2 && formik.errors.newPassword2}
  InputProps={{
    endAdornment: (
      <InputAdornment position="end">
        <IconButton onClick={toggleShowConfirmPassword} edge="end">
          {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
        </IconButton>
      </InputAdornment>
    ),
  }}
/>
            <DialogActions>
              <Button onClick={handleCloseDialog}>Cancel</Button>
              <Button type="submit" color="primary">Save</Button>
            </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </Box>
  );
};

export default ProfilePage;
