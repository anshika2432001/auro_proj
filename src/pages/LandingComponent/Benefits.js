import React from 'react';
import { Card, CardContent,Button,Typography,Grid, Box } from '@mui/material';
import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import HandshakeIcon from '@mui/icons-material/Handshake';
import CastForEducationIcon from '@mui/icons-material/CastForEducation';
import VolunteerActivismIcon from '@mui/icons-material/VolunteerActivism';
import AutoStoriesIcon from '@mui/icons-material/AutoStories';
import SchoolIcon from '@mui/icons-material/School';

const Benefits = () => {
  return (
    <div>
        <Card sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)'}}>
      <CardContent>
        <Typography variant="h2" align="center" color="#ffff">
          Benefits
        </Typography>
        <Typography variant="body2" align="center" color="white">
          Some content explaining the benefits goes here.
        </Typography>
        <Grid container spacing={0} sx={{ mt: 3 }}>
          <Grid item xs={12} sm={6} md={6} lg={6} sx={{p:1}}>
            <Box  align="center">
              <AccountBalanceIcon sx={{color:"white",pt:"15px"}}/>
            <Typography variant="h4" align="center" color="white">Policy Makers and Research Organizations</Typography>
            <Typography variant="body2" align="center" color="white">
            To benchmark and compare educational practices and policies and develop evidence-based policies to improve the overall education system.
        </Typography>
          </Box>
          </Grid>
          <Grid item xs={12} sm={6} md={6} lg={6} sx={{ borderLeft: '1px solid #ccc',p:1 }}>
          <Box  align="center">
              <HandshakeIcon sx={{color:"white",pt:"15px"}}/>
            <Typography variant="h4" align="center" color="white">Government Agencies</Typography>
            <Typography variant="body2" align="center" color="white">
            To monitor educational progress, make data-driven decisions, and allocate resources effectively.
        </Typography>
          </Box>
          </Grid>
          <Grid item xs={12} sm={6} md={6} lg={6} sx={{ borderTop: '1px solid #ccc',p:1}}>
          <Box  align="center">
              <CastForEducationIcon sx={{color:"white"}}/>
            <Typography variant="h4" align="center" color="white">Teachers</Typography>
            <Typography variant="body2" align="center" color="white">
            To track learning outcomes, identify areas for improvement, and tailor their teaching strategies. The dashboard can also provide insights into effective teaching practices and help teachers benchmark their performance.
        </Typography>
          </Box>
          </Grid>
          <Grid item xs={12} sm={6} md={6} lg={6} sx={{ borderTop: '1px solid #ccc',borderLeft: '1px solid #ccc',p:1 }}>
          <Box  align="center">
              <VolunteerActivismIcon sx={{color:"white"}}/>
            <Typography variant="h4" align="center" color="white">Education Leaders and Administrators</Typography>
            <Typography variant="body2" align="center" color="white">
            To analyze trends, assess current practices, and evaluate the effectiveness of schools and education systems.
        </Typography>
          </Box>
          </Grid>
          {/* <Grid item xs={12} sm={4} md={4} lg={4} sx={{ borderLeft: '1px solid #ccc', borderTop: '1px solid #ccc' }}>
          <Box  align="center">
              <AutoStoriesIcon sx={{color:"white"}}/>
            <Typography variant="h4" align="center" color="white"> Teachers</Typography>
            <Typography variant="body2" align="center" color="white">
          Some content explaining the benefits goes here.
        </Typography>
          </Box>
          </Grid>
          <Grid item xs={12} sm={4} md={4} lg={4} sx={{ borderLeft: '1px solid #ccc', borderTop: '1px solid #ccc' }}>
          <Box  align="center">
              <SchoolIcon sx={{color:"white"}}/>
            <Typography variant="h4" align="center" color="white"> Students</Typography>
            <Typography variant="body2" align="center" color="white">
          Some content explaining the benefits goes here.
        </Typography>
          </Box>
          </Grid> */}
        </Grid>
      </CardContent>
    </Card>
      
    </div>
  )
}

export default Benefits
