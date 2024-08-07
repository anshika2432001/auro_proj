import React from 'react';
import { Dialog, DialogContent, DialogTitle, Box, TextField, Button, IconButton, Typography, Link } from '@mui/material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { sha256 } from "js-sha256";
import { useRef, useState } from "react";
import { useSnackbar } from "../uiComponents/Snackbar";
import {userLogin} from '../../store/loginUserSlice';


const Login = ({ isOpen, onClose }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { showSnackbar } = useSnackbar();
  const [emptyPass, setEmptyPass] = useState(false);
  const [emptyUsername, setEmptyUsername] = useState(false);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const usernameRef = useRef();
  const passwordRef = useRef();
  let salt = "";


  const handleLogin = ()=> {
  
    navigate("/dashboard")
    //  dispatch(fetchFiltersDropdown());
   
  }

  const hashPassword = (passwordPlainText) => {
    var plainText = passwordPlainText;
    salt = randomString();
    let hash = sha256.hmac(salt, plainText);
    // console.log(hash)
    passwordRef.current.value = hash;
  };

  const randomString = () => {
    let length = 100;
    let chars =
      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    var result = "";
    for (var i = length; i > 0; --i)
      result += chars[Math.floor(Math.random() * chars.length)];
    return result;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (usernameRef.current.value === "" && passwordRef.current.value === "") {
      setEmptyUsername(true);
      setEmptyPass(true);
    } else {
      if (usernameRef.current.value === "") {
        setEmptyUsername(true);
        setEmptyPass(false);
      } else if (passwordRef.current.value === "") {
        setEmptyPass(true);
        setEmptyUsername(false);
      }
    }
    
    if (usernameRef.current.value !== "" && passwordRef.current.value !== "") {
      hashPassword(passwordRef.current.value);
      // console.log(passwordRef.current.value);
      
      setIsSubmitted(true);
      let request = {
        username: usernameRef.current.value,
        password: passwordRef.current.value.trim(),
        password2: salt,
      };
      setEmptyPass(false);
      setEmptyUsername(false);
      // console.log(request);
      dispatch(userLogin(request, successCb));
    }
  };

  const successCb = (response) => {
    // console.log("Inside successCB");
    //navigate("/home");
    try {
      if (
        response.data.status === true ||
        response.data.statusCode === "200" ||
        response.data.status === "SUCCESS"
      ) {
        showSnackbar("Login Successful", "success");
        navigate("/dashboard");
      } else if (
        response.data.statusCode === 400 ||
        response.data.status === false
      ) {
        setIsSubmitted(false);
        //setLoading(false);
        showSnackbar(response.data.message, "error");
        navigate("/");
      } else {
        setIsSubmitted(false);
        showSnackbar("Opps, something went wrong", "error");
        navigate("/");
      }
    } catch (error) {
      setIsSubmitted(false);
      showSnackbar("Opps, something went wrong", "error");
      // console.log(error.message);
      navigate("/");
    }
  };


 
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
                          id="userid"
                          placeholder="User Id"
                          variant="outlined"
                          fullWidth
                          inputRef={usernameRef}
                          sx={{
                            border: "1px solid #EAEDF1",
                            borderRadius: "5px",
                          }}
                          size="small"
                          error={emptyUsername}
                          helperText={
                            emptyUsername ? "User Id is required" : null
                          }
                        />

<TextField
                          id="password"
                          placeholder="Password"
                          variant="outlined"
                          type="password"
                          fullWidth
                          inputRef={passwordRef}
                          sx={{
                            border: "1px solid #EAEDF1",
                            borderRadius: "5px",
                          }}
                          size="small"
                          error={emptyPass}
                          helperText={emptyPass ? "Password is required" : null}
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