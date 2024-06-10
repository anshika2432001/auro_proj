import React from 'react';
import { Card, Typography, Grid, Link,Box, CardContent,Divider } from '@mui/material';
import { makeStyles } from '@mui/styles';
import AuroLogo from '../../images/AuroLogo.png';
import FacebookLogo from '../../images/Facebook.png';
import InstagramLogo from '../../images/Instagram.png';
import LinkedinLogo from '../../images/LinkedIn.png';
import YoutubeLogo from '../../images/YouTube.png';
import GooglePlayStore from '../../images/GooglePlayStore.png';
import ApplePlayStore from '../../images/ApplePlayStore.png';


const useStyles = makeStyles((theme) => ({
  
  logo: {
    width: '50px',
    marginRight: 2,
  },
  quickLinksHeading: {
    paddingTop: 10,
    paddingBottom: 10,
    color: "white" 
  },
  quickLinks: {
   '& a': {
    color: 'white',
   
    textDecoration: 'none',
    '&:hover': {
      textDecoration: 'underline'
    }
   }
  },
  socialMediaIcon: {
    width:"40px",
    height:"40px",
    marginRight:"5px"
  },
  copyright: {
    display:'flex',
    justifyContent:'flex-end',
    
    color:"white"
  }
  
}));

const Footer = () => {
  const classes = useStyles();

  return (
   
      <Card sx={{backgroundColor:"#20252C"}}>
        <CardContent>
        <Grid container spacing={2} display="flex">
          <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
         
         
              <Typography variant="h6">
                <img src={AuroLogo} alt="Logo" height="40" />
              </Typography>
           
            <Typography variant="body2"color="white" sx={{mr:"30px"}}>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium eos fuga laborum doloremque. Modi libero nam, fugit quasi quidem aliquam placeat officia possimus ad accusamus inventore eius, nesciunt iure deserunt.</Typography>
            <Box sx={{mt:1}}>
              <Link href="#" target="_blank" sx={{mr:2}}>
              <img src={GooglePlayStore} alt="GooglePlayStore" />
              </Link>
              <Link href="#" target="_blank">
              <img src={ApplePlayStore} alt="ApplePlayStore" />
              </Link>
              </Box>
          </Grid>
          <Grid item xs={12} sm={2.5} md={2.5} lg={2.5}>
         
            <Typography variant="h4"  className={classes.quickLinksHeading}>
              Quick Links
            </Typography>
            <Typography variant="body2" className={classes.quickLinks}>
              <Link href="#">Donation</Link>
              <br />
              <Link href="#">Entreprenures</Link>
              <br />
              <Link href="#">Research</Link>
              <br />
              <Link href="#">Partnership</Link>
            </Typography>
          </Grid>
          <Grid item xs={12} sm={2.5} md={2.5} lg={2.5}>
         
          <Typography variant="h4" className={classes.quickLinksHeading}>
              Links
            </Typography>
            <Typography variant="body2" className={classes.quickLinks} >
              <Link href="#" >Blog</Link>
              <br />
              <Link href="#">News</Link>
              <br />
              <Link href="#">About</Link>
              <br />
              <Link href="#">Content</Link>
            </Typography>
       </Grid>
          <Grid item xs={12} sm={2.5} md={2.5} lg={2.5}>
          
            <Typography variant="h4"  className={classes.quickLinksHeading}>
              Get in Touch
            </Typography>
            <Typography variant="body2"color="white">info@example.com</Typography>
            <Typography variant="body2"color="white">Plot-1201, Vasundhara, Ghaziabad, UttarPradesh,123456</Typography>
            <Box sx={{mt:1}}>
              <Link href="#" target="_blank">
              <img src={LinkedinLogo} alt="Linkedin" className={classes.socialMediaIcon}/>
              </Link>
              <Link href="#" target="_blank">
              <img src={YoutubeLogo} alt="Youtube" className={classes.socialMediaIcon}/>
              </Link>
              <Link href="#" target="_blank">
              <img src={FacebookLogo} alt="Facebook" className={classes.socialMediaIcon}/>
              </Link>
              <Link href="#" target="_blank">
              <img src={InstagramLogo} alt="Instagram" className={classes.socialMediaIcon}/>
              </Link>
            </Box>
          </Grid>
        </Grid>
        </CardContent>
        <Divider sx={{ backgroundColor:"#ffff",marginTop:1,marginBottom:1}}/>
        <Box className={classes.copyright}>
          <Typography variant="body1">
            &copy; Copyright 2024, All Rights Reserved
          </Typography>
        </Box>
      </Card>
 
  );
};

export default Footer;