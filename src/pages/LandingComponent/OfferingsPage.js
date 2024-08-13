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
            <Typography variant="body2"  >
            All- encompassing data on education indicators with interactive data visualization and accessibility through user-friendly dashboards to aid comparative analysis and benchmarking of educational practices and policies.
            </Typography>
            <Grid container spacing={2} sx={{mt:2}}>
              <Grid item xs={6}>
                <Typography variant="h6" display="flex" flexDirection="column">
                  <BarChartIcon sx={{color:"#4772D9"}}/>
                  Comprehensive data analysis
                </Typography>
                <Typography variant="body2" >
                •	Extensive datasets on education indicators, including measurements of learning outcomes and competencies<br></br>
•	Analysis and assessment of trends, contextual factors, and educational practices and learning environment attributes

                </Typography>
              </Grid>
              <Grid item xs={6} >
                <Typography variant="h6" display="flex" flexDirection="column">
                  <DashboardOutlinedIcon sx={{color:"#4772D9"}}/>
                  Data Visualization and Accessibility
                </Typography>
                <Typography variant="body2">
                •	Interactive visualizations and user-friendly data dashboards<br></br>
•	Regular updates and data refreshes<br></br>
•	Collaboration and partnerships to enhance data collection, analysis, and interpretation.

                </Typography>
              </Grid>
              <Grid item xs={6} >
                <Typography variant="h6" display="flex" flexDirection="column">
                  <LockClockOutlinedIcon sx={{color:"#4772D9"}}/>
                  Comparative Analysis and Benchmarking
                </Typography>
                <Typography variant="body2">
                •	Benchmarking and comparison of educational practices and policies<br></br>
•	Evaluation of effectiveness of schools and education systems

                </Typography>
              </Grid>
              <Grid item xs={6} >
                <Typography variant="h6" display="flex" flexDirection="column">
                  <LocationOnOutlinedIcon sx={{color:"#4772D9"}}/>
                  Data Reporting 
                </Typography>
                <Typography variant="body2">
                • Tailored reports providing insights into state of learning, learning outcomes and educational contexts of students in the country.
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
