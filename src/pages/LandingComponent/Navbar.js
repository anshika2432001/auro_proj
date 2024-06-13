import React,{useState} from 'react';
import { AppBar, Toolbar,Grid, Typography, Button, Box } from '@mui/material';
import { useNavigate} from "react-router-dom";
import AuroLogo1 from '../../images/AuroLogo1.png'
import Login from './Login';


const Navbar = () => {
  const navigate = useNavigate();
  const [activeButton, setActiveButton] = useState(null);
  const [isLoginDialogVisible, setLoginDialogVisible] = useState(false);

  const handleButtonClick = (buttonName,path) => {
    setActiveButton(buttonName);
    if(path) navigate(path);
  }
  const getButtonStyle = (buttonName) => ({
    color: activeButton === buttonName ? '#2899DB': 'black',
    marginRight:'10px'
  })

  const toggleLoginDialog = () => {
    setLoginDialogVisible(!isLoginDialogVisible);
  };
  const handleRegisterForm = ()=> {
    navigate("/registrationForm")
  }

  return (
    <div>
      <AppBar position="static" sx={{ backgroundColor: 'white' }}>
        <Toolbar>
          <Grid container alignItems="center">
            <Grid item xs={12} sm={3} md={3} lg={3}> 
              <Typography variant="h6">
                <img src={AuroLogo1} alt="Logo" height="40" />
              </Typography>
            </Grid>
            <Grid item xs={12} sm={6} md={6} lg={6} textAlign="center"> 
            <Box sx={{
    display: 'flex',
    justifyContent: 'center',
    flexDirection: { xs: 'column', sm: 'row' },
    alignItems: 'center',
  }}>
                <Button sx={getButtonStyle('about')} onClick={()=> handleButtonClick('about','/')}><Typography variant="h5">About Org</Typography></Button>
                <Button sx={getButtonStyle('blog')} onClick={()=> handleButtonClick('blog','/blog')}><Typography variant="h5">Blog</Typography></Button>
                <Button sx={getButtonStyle('news')} onClick={()=> handleButtonClick('news','/news')}><Typography variant="h5">News</Typography></Button>
                <Button sx={getButtonStyle('faq')} onClick={()=> handleButtonClick('faq','/faq')}><Typography variant="h5">FAQ</Typography></Button>
                <Button sx={getButtonStyle('donate')}><Typography variant="h5">Donate</Typography></Button>
                <Button sx={getButtonStyle('contact')} onClick={()=> handleButtonClick('contact','/contact')} ><Typography variant="h5">Contact</Typography></Button>
              </Box>
            </Grid>
            <Grid item xs={12} sm={3} md={3} lg={3}  display="flex" justifyContent="flex-end"> 
           
              <Button variant="contained" onClick={()=> handleRegisterForm()} sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)',mr:2}} >Register</Button>
             
              <Button color="primary" variant="outlined" onClick={toggleLoginDialog}>Login</Button>
      
            </Grid>
          </Grid>
        </Toolbar>
      </AppBar>
      <Login isOpen={isLoginDialogVisible} onClose={toggleLoginDialog} />
    </div>
  )
}

export default Navbar;