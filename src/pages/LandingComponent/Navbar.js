import React from 'react';
import { AppBar, Toolbar,Grid, Typography, Button, Box } from '@mui/material';
import { useNavigate} from "react-router-dom";

const Navbar = () => {
  const navigate = useNavigate();
  return (
    <div>
      <AppBar position="static" sx={{ backgroundColor: 'white' }}>
        <Toolbar>
          <Grid container alignItems="center">
            <Grid item xs={12} sm={3} md={3} lg={3}> 
              <Typography variant="h6">
                <img src="your-logo-url.png" alt="Logo" height="40" />
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6} md={6} lg={6} textAlign="center"> 
            <Box sx={{
    display: 'flex',
    justifyContent: 'center',
    flexDirection: { xs: 'column', sm: 'row' },
    alignItems: 'center',
  }}>
                <Button sx={{ color: 'black', marginRight: '10px' }} onClick={() => navigate('/')}>About Org</Button>
                <Button sx={{ color: 'black', marginRight: '10px' }} onClick={() => navigate('/blog')}>Blog</Button>
                <Button sx={{ color: 'black', marginRight: '10px' }} onClick={() => navigate('/news')}>News</Button>
                <Button sx={{ color: 'black', marginRight: '10px' }} onClick={() => navigate('/faq')}>FAQ</Button>
                <Button sx={{ color: 'black', marginRight: '10px' }}>Donate</Button>
                <Button sx={{ color: 'black' }} onClick={() => navigate('/contact')} >Contact</Button>
              </Box>
            </Grid>
            <Grid item xs={12} sm={3} md={3} lg={3} textAlign="right"z> 
           
              <Button variant="filled" sx={{ backgroundColor: "blue", marginRight: '10px' }}>Register</Button>
             
              <Button color="primary" variant="outlined">Login</Button>
      
            </Grid>
          </Grid>
        </Toolbar>
      </AppBar>
    </div>
  )
}

export default Navbar;