import React from 'react';
import { Card, CardContent, Avatar, Typography, Box } from '@mui/material';

const ProfilePage = () => {
  const user = {
    name: localStorage.getItem('userName'),
    email: localStorage.getItem('email'),
    role: localStorage.getItem('roleName'),
    profileImage: 'https://via.placeholder.com/150' // Replace with actual image URL
  };

  return (
    
      <Card >
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
        </CardContent>
      </Card>
    
  );
};

export default ProfilePage;
