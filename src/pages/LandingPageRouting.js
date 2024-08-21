import React from 'react';
import Navbar from './LandingComponent/Navbar';
import Footer from './LandingComponent/Footer';
import { Outlet } from 'react-router-dom';
import { Box } from '@mui/material';

const LandingPageRouting = () => {
  return (
    <Box display="flex" flexDirection="column" minHeight="100vh">
      {/* Navbar */}
      <Navbar />
      
      {/* Main content area */}
      <Box component="main" flexGrow={1}>
        <Outlet />
      </Box>
      
      {/* Footer */}
      <Footer />
    </Box>
  );
};

export default LandingPageRouting;
