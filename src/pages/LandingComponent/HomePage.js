import React from 'react';
import { useNavigate } from "react-router-dom";
import { makeStyles } from '@mui/styles';
import { Card, CardContent, Button, Typography, Grid, Box } from '@mui/material';

import Image1 from '../../images/HomePageImage1.png';
import Image2 from '../../images/HomePageImage2.png';
import Image3 from '../../images/HomePageImage3.png';
import Image4 from '../../images/HomePageImage4.png';

import AuroLogo from '../../images/AuroLogo.png';
import PlayCircleIcon from '@mui/icons-material/PlayCircle';

const useStyles = makeStyles((theme) => ({
  imageStyle: {
    height: '105%',
    transform: 'rotate(5deg)',
    width: '100px',
    [theme.breakpoints.up('sm')]: {
      width: '150px',
    },
    [theme.breakpoints.up('md')]: {
      width: '200px',
    },
    [theme.breakpoints.up('lg')]: {
      width: '150px',
    },
  },
}));

const HomePage = () => {
  const classes = useStyles();
  const navigate = useNavigate();

  const handleRegisterForm = () => {
    navigate("/registrationForm");
  };

  return (
    <div>
      <Card sx={{ background: 'linear-gradient(to right, #4772D9, #2899DB, #70CCE2)', pb: 0, pt: 0 }}>
        <CardContent sx={{ pb: '0 !important', pt: 0 }}>
          <Grid container alignItems="center" columnSpacing={2}>
            {/* Left side */}
            <Grid item xs={12} sm={6} lg={6} md={6} sx={{ pt: "30px", pb: "30px", pr: "50px" }}>
              <Typography variant="h1">
                A Captivating Heading will be placed here.
              </Typography>
              <Typography variant="body1" gutterBottom sx={{ mt: 3, color: "white" }}>
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium eos fuga laborum doloremque. Modi libero nam, fugit quasi quidem aliquam placeat officia possimus ad accusamus inventore eius, nesciunt iure deserunt.
              </Typography>
              <Button variant="contained" onClick={handleRegisterForm} sx={{ backgroundColor: "white", color: "#2899DB", mt: 3 }}>
                Register Now
              </Button>
              <Button variant="outlined" sx={{ ml: 1, color: "white", mt: 3 }}>
                <PlayCircleIcon sx={{ mr: 1 }} />
                Watch Video
              </Button>
              <Box sx={{ backgroundColor: "white", color: "#20252C", padding: '5px 10px', mt: 1, display: 'flex', alignItems: 'center', borderRadius: '5px', width: 'fit-content' }}>
                <Typography variant="body2" sx={{ fontWeight: 'bold', mr: 1 }}>
                  Powered By
                </Typography>
                <Box
                  component="img"
                  src={AuroLogo}
                  alt="Powered By Logo"
                  sx={{ width: '70px', height: 'auto' }}
                />
              </Box>
            </Grid>
            {/* Right side */}
            <Grid item xs={12} sm={6} lg={6} md={6}>
              <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '12px', height: "400px" }}>
                <Box
                  component="img"
                  src={Image1}
                  alt="Image1"
                  className={classes.imageStyle}
                />
                <Box
                  component="img"
                  src={Image2}
                  alt="Image2"
                  className={classes.imageStyle}
                />
                <Box
                  component="img"
                  src={Image3}
                  alt="Image3"
                  className={classes.imageStyle}
                />
                <Box
                  component="img"
                  src={Image4}
                  alt="Image4"
                  className={classes.imageStyle}
                />
              </Box>
            </Grid>
          </Grid>
        </CardContent>
      </Card>
    </div>
  );
}

export default HomePage;
