import React, { useState } from 'react';
import { Card, CardContent, Avatar, Typography, Box, Button, Dialog, DialogTitle, DialogContent, TextField, DialogActions } from '@mui/material';

const ProfilePage = () => {
  const user = {
    name: localStorage.getItem('userName'),
    email: localStorage.getItem('email'),
    role: localStorage.getItem('roleName'),
    profileImage: 'https://via.placeholder.com/150' // Replace with actual image URL
  };

  const [openDialog, setOpenDialog] = useState(false);
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');

  const handleOpenDialog = () => {
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setOldPassword('');
    setNewPassword('');
    setConfirmNewPassword('');
  };

  const handleChangePassword = () => {
    if (newPassword !== confirmNewPassword) {
      alert("New password and confirmation do not match.");
      return;
    }
    
    // Example: You can send the password change request to your server
    console.log({ oldPassword, newPassword });
    handleCloseDialog();
  };

  return (
    <Box>
      <Card>
        <CardContent sx={{ textAlign: 'center' }}>
          <Avatar
            alt={user.name}
            src={user.profileImage}
            sx={{ width: 100, height: 100, margin: '0 auto' }}
          />
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
          <TextField
            autoFocus
            margin="dense"
            label="Old Password"
            type="password"
            fullWidth
            value={oldPassword}
            onChange={(e) => setOldPassword(e.target.value)}
          />
          <TextField
            margin="dense"
            label="New Password"
            type="password"
            fullWidth
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
          <TextField
            margin="dense"
            label="Confirm New Password"
            type="password"
            fullWidth
            value={confirmNewPassword}
            onChange={(e) => setConfirmNewPassword(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleChangePassword} color="primary">Save</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ProfilePage;
