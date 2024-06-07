import React from 'react';
import { Container, Typography, Grid, Link } from '@mui/material';
import { makeStyles } from '@mui/styles';

const useStyles = makeStyles((theme) => ({
  footer: {
    backgroundColor: '#333',
    color: '#fff',
    padding: 4,
  },
  logo: {
    width: '50px',
    marginRight: 2,
  },
  quickLinksHeading: {
    marginTop: 2,
    marginBottom: 1,
  },
  quickLinks: {
    marginBottom: 2,
  },
  getInTouch: {
    marginTop: 2,
  },
}));

const Footer = () => {
  const classes = useStyles();

  return (
    <footer className={classes.footer}>
      <Container>
        <Grid container spacing={2} display="flex">
          <Grid item xs={12} sm={3} md={3} lg={3}>
         
            <Grid container alignItems="center">
              <img src="/logo.png" alt="logo" className={classes.logo} />
              <Typography variant="body1">Company Name</Typography>
            </Grid>
            <Typography variant="body2">Some content about the company.</Typography>
          </Grid>
          <Grid item xs={12} sm={3} md={3} lg={3}>
         
            <Typography variant="h6" className={classes.quickLinksHeading}>
              Quick Links
            </Typography>
            <Typography variant="body2" className={classes.quickLinks}>
              <Link href="#">Link 1</Link>
              <br />
              <Link href="#">Link 2</Link>
              <br />
              <Link href="#">Link 3</Link>
            </Typography>
          </Grid>
          <Grid item xs={12} sm={3} md={3} lg={3}>
         
         <Typography variant="h6" className={classes.quickLinksHeading}>
         Links
         </Typography>
         <Typography variant="body2" className={classes.quickLinks}>
           <Link href="#">Link 1</Link>
           <br />
           <Link href="#">Link 2</Link>
           <br />
           <Link href="#">Link 3</Link>
         </Typography>
       </Grid>
          <Grid item xs={12} sm={3} md={3} lg={3}>
          
            <Typography variant="h6" className={classes.getInTouch}>
              Get in Touch
            </Typography>
            <Typography variant="body2">Contact: contact@example.com</Typography>
            <Typography variant="body2">Phone: +1234567890</Typography>
          </Grid>
        </Grid>
      </Container>
    </footer>
  );
};

export default Footer;