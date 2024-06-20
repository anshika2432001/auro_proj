import React from 'react';
import Navbar from './LandingComponent/Navbar';
import Footer from './LandingComponent/Footer';
import { Outlet } from 'react-router-dom';

const LandingPageRouting = () => {
  return (
    <div>
      <Navbar />
      <Outlet />
      <Footer />
    </div>
  );
};

export default LandingPageRouting;