import React from 'react';
import { Card, CardContent,Button,Typography,Grid, Box } from '@mui/material';
import OfferingsBackgrund from '../../images/OfferingsBackground.png'
import OfferingsIllustration from '../../images/OfferingsIllustration.png'
import BarChartIcon from '@mui/icons-material/BarChart';
import DashboardOutlinedIcon from '@mui/icons-material/DashboardOutlined';
import LockClockOutlinedIcon from '@mui/icons-material/LockClockOutlined';
import LocationOnOutlinedIcon from '@mui/icons-material/LocationOnOutlined';

const OfferingsPage = () =>{
  return (
    <div>
       <Card >
      <CardContent>
        <Grid container alignItems="center">
          <Grid item xs={12} sm={6} md={6} lg={6}>
          <Box
           sx={{position: 'relative',width:"500px",height:"400px"}}
           >
            <Box
            component = "img"
            src={OfferingsBackgrund}
            alt= "Image1"
            sx={{position:'absolute',
              top:'10px',left:'10px',width:"500px",
              height:"400px",
            }}

            />
              <Box
            component = "img"
            src={OfferingsIllustration}
            alt= "Image1"
            sx={{position:'absolute',
              top:'50px',left:'20px',width:"350px",
              height:"350px",
            }}

            />
             

           </Box>
          </Grid>
          <Grid item xs={12} sm={6}>
            <Typography variant="h2"  >
              Our Offerings
            </Typography>
            <Typography variant="body1"  >
              Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium eos fuga laborum doloremque. Modi libero nam, fugit quasi quidem aliquam placeat officia possimus ad accusamus inventore eius, nesciunt iure deserunt.
            </Typography>
            <Grid container spacing={2} sx={{mt:2}}>
              <Grid item xs={6}>
                <Typography variant="h6" display="flex" flexDirection="column">
                  <BarChartIcon sx={{color:"#4772D9"}}/>
                  Real Time Data
                </Typography>
                <Typography variant="body2" >
                  Content for Real Time section.
                </Typography>
              </Grid>
              <Grid item xs={6} >
                <Typography variant="h6" display="flex" flexDirection="column">
                  <DashboardOutlinedIcon sx={{color:"#4772D9"}}/>
                  Dashboard
                </Typography>
                <Typography variant="body2">
                  Content for Dashboard section.
                </Typography>
              </Grid>
              <Grid item xs={6} >
                <Typography variant="h6" display="flex" flexDirection="column">
                  <LockClockOutlinedIcon sx={{color:"#4772D9"}}/>
                  Safety Security
                </Typography>
                <Typography variant="body2">
                  Content for Safety Security section.
                </Typography>
              </Grid>
              <Grid item xs={6} >
                <Typography variant="h6" display="flex" flexDirection="column">
                  <LocationOnOutlinedIcon sx={{color:"#4772D9"}}/>
                  Heading
                </Typography>
                <Typography variant="body2">
                  Content for Heading section.
                </Typography>
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
      
    </div>
  )
}

export default OfferingsPage
