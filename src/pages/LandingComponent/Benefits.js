import React from 'react';
import { Card, CardContent,Button,Typography,Grid, CardMedia } from '@mui/material';

const Benefits = () => {
  return (
    <div>
        <Card sx={{backgroundColor:"lightblue",padding:"10px"}}>
      <CardContent>
        <Typography variant="h4" align="center" gutterBottom>
          Benefits
        </Typography>
        <Typography variant="body1" align="center">
          Some content explaining the benefits goes here.
        </Typography>
        <Grid container spacing={0} sx={{ mt: 3 }}>
          <Grid item xs={4}>
            <Typography variant="h6" align="center" gutterBottom>
              Part 1
            </Typography>
          </Grid>
          <Grid item xs={4} sx={{ borderLeft: '1px solid #ccc' }}>
            <Typography variant="h6" align="center" gutterBottom>
              Part 2
            </Typography>
          </Grid>
          <Grid item xs={4} sx={{ borderLeft: '1px solid #ccc' }}>
            <Typography variant="h6" align="center" gutterBottom>
              Part 3
            </Typography>
          </Grid>
          <Grid item xs={4} sx={{ borderTop: '1px solid #ccc' }}>
            <Typography variant="h6" align="center" gutterBottom>
              Part 4
            </Typography>
          </Grid>
          <Grid item xs={4} sx={{ borderLeft: '1px solid #ccc', borderTop: '1px solid #ccc' }}>
            <Typography variant="h6" align="center" gutterBottom>
              Part 5
            </Typography>
          </Grid>
          <Grid item xs={4} sx={{ borderLeft: '1px solid #ccc', borderTop: '1px solid #ccc' }}>
            <Typography variant="h6" align="center" gutterBottom>
              Part 6
            </Typography>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
      
    </div>
  )
}

export default Benefits
