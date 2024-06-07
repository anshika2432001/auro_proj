import React from 'react';

import '../../App.css'
import { Card, CardContent,Button,Typography,Grid, CardMedia } from '@mui/material';


const HomePage = () => {
  return (
    <div>
       <Card sx={{backgroundColor:"lightblue",padding:"10px"}}>
      <CardContent>
        <Grid container spacing={2} alignItems="center" >
          {/* Left side */}
          <Grid item xs={12} sm={6} >
            <Typography variant="h1" gutterBottom >
              A Captivative Heading will be placed here.
            </Typography>
            <Typography variant="body1" gutterBottom>
              Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium eos fuga laborum doloremque. Modi libero nam, fugit quasi quidem aliquam placeat officia possimus ad accusamus inventore eius, nesciunt iure deserunt.
            </Typography>
            <Button variant="contained" sx={{backgroundColor:"white",color:"blue"}}>
              Register Now
            </Button>
            <Button variant="outlined"  sx={{ ml: 1 }}>
              Watch Video
            </Button>
          </Grid>
          {/* Right side */}
          <Grid item xs={12} sm={6}>
            <CardMedia
              component="img"
              height="auto"
              image="/path/to/your/image.jpg"
              alt="Image"
            />
          </Grid>
        </Grid>
      </CardContent>
    </Card>
    </div>
  )
}

export default HomePage
