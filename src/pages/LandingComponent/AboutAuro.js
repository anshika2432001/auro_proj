import React from 'react';
import { Card, CardContent,Button,Box,Typography,Grid, CardMedia } from '@mui/material';
import AppRegistrationIcon from '@mui/icons-material/AppRegistration';
import GradingIcon from '@mui/icons-material/Grading';
import GamesIcon from '@mui/icons-material/Games';
import HandshakeIcon from '@mui/icons-material/Handshake';
import Society from '../../images/Society.png'
import Scholar from '../../images/Scholar.png'

const AboutAuro = () => {
  return (
    <div>
      <Card>
        <CardContent>
        <Grid container spacing={0} sx={{ mt: 3 }}>
          <Grid item  xs={12} sm={3} md={3} lg={3} display="flex">
          <AppRegistrationIcon sx={{color:"#4772D9"}}/>
            <Typography variant="h3" sx={{pl:2}} >
           
              1.6Mn+
              <Typography variant="body1"  >
              Registrations on App
            </Typography>
            </Typography>
            
          </Grid>
          <Grid item xs={12} sm={3} md={3} lg={3} display="flex">
          <GradingIcon sx={{color:"#4772D9"}}/>
            <Typography variant="h3" sx={{pl:2}} >
           
              10k+
              <Typography variant="body1"  >
              Registrations on App
            </Typography>
            </Typography>
            
          </Grid>
          <Grid item xs={12} sm={3} md={3} lg={3} display="flex">
          <HandshakeIcon sx={{color:"#4772D9"}}/>
            <Typography variant="h3" sx={{pl:2}} >
           
              175+
              <Typography variant="body1"  >
              Registrations on App
            </Typography>
            </Typography>
            
          </Grid>
          <Grid item xs={12} sm={3} md={3} lg={3} display="flex">
          <GamesIcon sx={{color:"#4772D9"}}/>
            <Typography variant="h3" sx={{pl:2}} >
           
              26
              <Typography variant="body1"  >
              Registrations on App
            </Typography>
            </Typography>
            
          </Grid>
          </Grid>
          <Grid container spacing={2} sx={{ mt: 3 }}>
          <Grid item xs={12} sm={4.5} md={4.5} lg={4.5} sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
              <CardMedia
                component="img"
                height="80px"
                width="80px"
                image={Society}
                alt="Image"
                sx={{ mt: 5 }}
              />
            </Grid>
           
            <Grid item xs={12} sm={7.5} md={7.5} lg={7.5}>
              <Typography variant="h2" >
                About Sri Aurobindo Society
              </Typography>
              <Typography variant="body1">
              Sri Aurobindo Society is a global, not-for-profit NGO aiming at building a beautiful tomorrow and a happier world. It creates an opportunity to connect and then surpass oneself, to realize one’s unity with others and integration with the entire creation, to work for a happier world, to participate in the collaborative effort, and to create the Next Future. 
              </Typography>
            </Grid>
            
          </Grid>
          
          
        </CardContent>
        </Card>
        <Grid container spacing={2} padding="20px 0px">
          <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
          <Box
            component = "img"
            src={Scholar}
            alt= "Image1"
            sx={{
              width:"200px",
              height:"200px",
            }}

            />
          </Grid>
           
            <Grid item xs={12} sm={7.5} md={7.5} lg={7.5}>
           
              <Box sx={{  lineHeight: 1.6, margin: '10px' }}>
              <Typography variant="h2" >
                About Auro Scholar
              </Typography>
      <Typography variant="body1" component="div">
      <b>Sri Aurobindo Society</b> started the <b>Auro Scholarships Programme</b> to award monthly <b>Micro-Scholarships to school students in India</b> through the <b>Auro Scholar App</b>. The goal is to provide online scholarship to students to improve their learning outcomes while strengthening academic foundation. Students can take <b>10 min curriculum-based quizzes</b> and get <b>INR 50 scholarship</b> per quiz within <b>24 hours</b> (INR 1,000 for 20 such quizzes).

        {/* Welcome Teachers & Parents, receive learning analytics of students to help improve progress. We provide quizzes across{' '}
        <Typography variant="body1" component="span" color="green">
         <b> 5 Subjects</b>
        </Typography>{' '}
        in{' '}
        <Typography variant="body1" component="span" color="orange">
         <b> 9 languages</b>
        </Typography>{' '}
        from{' '}
        <Typography variant="body1" component="span" color="#2899DB">
         <b> Grade 1 to 12th</b>
        </Typography>{' '}
        covering{' '}
        <Typography variant="body1" component="span" color="red">
          <b>Multiple School Brands</b>
        </Typography>
        . On this platform a student has to take quizzes to win a scholarship, and can master across 5 subjects by retaking the quiz multiple times if scored less. Students can also win cash prize up to Rs.1000/month. */}
      </Typography>
    </Box>
            </Grid>
            
          </Grid>
    </div>
  )
}

export default AboutAuro
