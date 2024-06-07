import React from 'react';
import { Card, CardContent,Button,Box,Typography,Grid, CardMedia } from '@mui/material';

const AboutAuro = () => {
  return (
    <div>
      <Card>
        <CardContent>
        <Grid container spacing={0} sx={{ mt: 3 }}>
          <Grid item xs={3}>
            <Typography variant="h6" align="center" gutterBottom>
              Part 1
            </Typography>
          </Grid>
          <Grid item xs={3} sx={{ borderLeft: '1px solid #ccc' }}>
            <Typography variant="h6" align="center" gutterBottom>
              Part 2
            </Typography>
          </Grid>
          <Grid item xs={3} sx={{ borderLeft: '1px solid #ccc' }}>
            <Typography variant="h6" align="center" gutterBottom>
              Part 3
            </Typography>
          </Grid>
          <Grid item xs={3} sx={{ borderLeft: '1px solid #ccc' }} >
            <Typography variant="h6" align="center" gutterBottom>
              Part 4
            </Typography>
          </Grid>
          </Grid>
          <Grid container spacing={2} sx={{ mt: 5 }}>
          <Grid item xs={12} sm={3}>
            <CardMedia
              component="img"
              height="200"
              image="/path/to/your/image.jpg"
              alt="Image"
            />
          </Grid>
           
            <Grid item xs={12} sm={9}>
              <Typography variant="h4" color="black" sx={{ fontWeight: 'bold',mb:"30px" }}>
                About Sri Aurobindo Society
              </Typography>
              <Typography variant="body1">
                Sri Aurobindo society exists to make known the aims and ideals of Sri Aurobindo & The Mother. Sri Aurobindo taught that man has the possibility of acquiring a new consciousness, the Truth consciousness, and is capable of living a life perfectly harmonious, good and beautiful, happy and fully conscious, through an inner self development, to discover the oneself in all and divine human nature. His teaching, though deeply routed to indian spirituality, is not bound by any religious doctrine. The society was founded by the mother in 1960 and an international non-governmental organization recognized by the Government of India as a charitable and research institute.
              </Typography>
            </Grid>
            
          </Grid>
          
          <Grid container spacing={2} sx={{ mt: 5 }}>
          <Grid item xs={12} sm={3}>
            <CardMedia
              component="img"
              height="200"
              image="/path/to/your/image.jpg"
              alt="Image"
            />
          </Grid>
           
            <Grid item xs={12} sm={9}>
              <Typography variant="h4" color="black" sx={{ fontWeight: 'bold',textAlign:"center",mb:"30px" }} >
                About Auro Scholar
              </Typography>
              <Box sx={{ textAlign: 'center', lineHeight: 1.6, margin: '20px' }}>
      <Typography variant="body1" component="div">
        Welcome Teachers & Parents, receive learning analytics of students to help improve progress. We provide quizzes across{' '}
        <Typography variant="body1" component="span" color="green">
          5 Subjects
        </Typography>{' '}
        in{' '}
        <Typography variant="body1" component="span" color="orange">
          9 languages
        </Typography>{' '}
        from{' '}
        <Typography variant="body1" component="span" color="blue">
          Grade 1 to 12th
        </Typography>{' '}
        covering{' '}
        <Typography variant="body1" component="span" color="red">
          Multiple School Brands
        </Typography>
        . On this platform a student has to take quizzes to win a scholarship, and can master across 5 subjects by retaking the quiz multiple times if scored less. Students can also win cash prize up to Rs.1000/month.
      </Typography>
    </Box>
            </Grid>
            
          </Grid>
        </CardContent>
        </Card>
    </div>
  )
}

export default AboutAuro
