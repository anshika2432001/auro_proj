import React from 'react';
import { Card, CardContent,Button,Typography,Grid, CardMedia } from '@mui/material';

const OfferingsPage = () =>{
  return (
    <div>
       <Card sx={{ backgroundColor: '#fff' }}>
      <CardContent>
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} sm={6}>
            <CardMedia
              component="img"
              height="200"
              image="/path/to/your/image.jpg"
              alt="Image"
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <Typography variant="h4" color="black" sx={{ fontWeight: 'bold' }}>
              Our Offerings
            </Typography>
            <Typography variant="body1" sx={{mt:2}}>
              Some data below the heading.
            </Typography>
            <Grid container spacing={2} sx={{mt:2}}>
              <Grid item xs={6}>
                <Typography variant="h6" color="primary" gutterBottom>
                  Real Time Data
                </Typography>
                <Typography variant="body2">
                  Content for Real Time section.
                </Typography>
              </Grid>
              <Grid item xs={6}>
                <Typography variant="h6" color="primary" gutterBottom>
                  Dashboard
                </Typography>
                <Typography variant="body2">
                  Content for Dashboard section.
                </Typography>
              </Grid>
              <Grid item xs={6}>
                <Typography variant="h6" color="primary" gutterBottom>
                  Safety Security
                </Typography>
                <Typography variant="body2">
                  Content for Safety Security section.
                </Typography>
              </Grid>
              <Grid item xs={6}>
                <Typography variant="h6" color="primary" gutterBottom>
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
