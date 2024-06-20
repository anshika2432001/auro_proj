import React from 'react';
import { Dialog, DialogContent, DialogTitle, Box, TextField, Button, IconButton, Typography, Link } from '@mui/material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import { useNavigate } from "react-router-dom";

const Login = ({ isOpen, onClose }) => {
  const navigate = useNavigate();
  const handleLogin = ()=> {
    navigate("/dashboard")
  }
  return (
    <Dialog open={isOpen} onClose={onClose} maxWidth="xs" fullWidth>
   <DialogTitle sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center',padding:"0px 15px 0px 24px" }}>
        <Typography variant="h3" color="#4772D9">Login</Typography>
        <IconButton onClick={onClose} color="primary">
          <DisabledByDefaultIcon />
        </IconButton>
      </DialogTitle>
    <DialogContent>
      <Box component="form" >
        <TextField
          label="Username"
          variant="outlined"
          fullWidth
          margin="normal"
        />
        <TextField
          label="Password"
          type="password"
          variant="outlined"
          fullWidth
          margin="normal"
        />
        <Box
            sx={{
              display: 'flex',
              justifyContent: 'flex-end',
              mt: 1,
            }}
          >
            <Link href="/forgotPassword" underline="hover" color="primary">
              Forgot Password?
            </Link>
          </Box>
        <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
             
            }}
          >
            <Button type="submit" variant="contained" sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)'}} onClick={()=>handleLogin()}>
              Login
            </Button>
          </Box>
      </Box>
    </DialogContent>
  </Dialog>
  );
};

export default Login;