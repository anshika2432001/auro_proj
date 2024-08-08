import React, { useRef, useState } from 'react';
import { Dialog, DialogContent, DialogTitle, Box, TextField, Button, IconButton, Typography, Link } from '@mui/material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { sha256 } from "js-sha256";
import { useSnackbar } from "../uiComponents/Snackbar";
import { userLogin } from '../../store/loginUserSlice';

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

  const hashPassword = (passwordPlainText) => {
    salt = randomString();
    let hash = sha256.hmac(salt, passwordPlainText);
    passwordRef.current.value = hash;
  };

  const randomString = () => {
    let length = 100;
    let chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    let result = "";
    for (let i = length; i > 0; --i)
      result += chars[Math.floor(Math.random() * chars.length)];
    return result;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const username = usernameRef.current.value;
    const password = passwordRef.current.value;

    if (username === "" && password === "") {
      setEmptyUsername(true);
      setEmptyPass(true);
      return;
    } else if (username === "") {
      setEmptyUsername(true);
      setEmptyPass(false);
      return;
    } else if (password === "") {
      setEmptyPass(true);
      setEmptyUsername(false);
      return;
    }

    setEmptyUsername(false);
    setEmptyPass(false);

    hashPassword(password);
    setIsSubmitted(true);

    const request = {
      username,
      password: passwordRef.current.value.trim(),
      password2: salt,
    };

    try {
      const response = await dispatch(userLogin(request)).unwrap();
      successCb(response);
    } catch (error) {
      showSnackbar("Oops, something went wrong", "error");
      setIsSubmitted(false);
    }
  };

  const successCb = (response) => {
    if (response.status === true || response.statusCode === "200" || response.status === "SUCCESS") {
      showSnackbar("Login Successful", "success");
      navigate("/dashboard");
    } else {
      showSnackbar(response.message || "Oops, something went wrong", "error");
      setIsSubmitted(false);
    }
  };

  return (
    <Dialog open={isOpen} onClose={onClose} maxWidth="xs" fullWidth>
      <DialogTitle sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: "0px 15px 0px 24px" }}>
        <Typography variant="h3" color="#4772D9">Login</Typography>
        <IconButton onClick={onClose} color="primary">
          <DisabledByDefaultIcon />
        </IconButton>
      </DialogTitle>
      <DialogContent>
        <Box
          component="form"
          onSubmit={handleSubmit}
          display="flex"
          flexDirection="column"
          alignItems="center"
          gap={2}
        >
          <TextField
            id="userid"
            placeholder="User Id"
            variant="outlined"
            fullWidth
            inputRef={usernameRef}
            size="small"
            error={emptyUsername}
            helperText={emptyUsername ? "User Id is required" : null}
          />
          <TextField
            id="password"
            placeholder="Password"
            variant="outlined"
            type="password"
            fullWidth
            inputRef={passwordRef}
            size="small"
            error={emptyPass}
            helperText={emptyPass ? "Password is required" : null}
          />
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'flex-end',
              width: '100%',
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
              width: '100%',
            }}
          >
            <Button type="submit" variant="contained" disabled={isSubmitted} sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }}>
              Login
            </Button>
          </Box>
        </Box>
      </DialogContent>
    </Dialog>
  );
};

export default Login;
