import React, { useRef, useState, useEffect } from 'react';
import { Dialog, DialogContent, DialogTitle, Box, TextField, Button, IconButton, Typography, Link } from '@mui/material';
import DisabledByDefaultIcon from '@mui/icons-material/DisabledByDefault';
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { sha256 } from "js-sha256";
import { useSnackbar } from "../uiComponents/Snackbar";
import { userLogin } from '../../store/loginUserSlice';
import { fetchRoleDropdown } from '../../store/roleDropdownSlice';
import CircularProgress from '@mui/material/CircularProgress';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';

const Login = ({ isOpen, onClose }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [loading, setLoading] = useState(false);
  const { showSnackbar } = useSnackbar();
  const [emptyPass, setEmptyPass] = useState(false);
  const [emptyUsername, setEmptyUsername] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const usernameRef = useRef();
  const passwordRef = useRef();
  const [hashedPassword, setHashedPassword] = useState("");
  const [salt, setSalt] = useState("");

  const hashPassword = (passwordPlainText) => {
    const generatedSalt = randomString();
    setSalt(generatedSalt);
    const hash = sha256.hmac(generatedSalt, passwordPlainText);
    setHashedPassword(hash);
  };

  const randomString = () => {
    let length = 100;
    let chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    let result = "";
    for (let i = length; i > 0; --i)
      result += chars[Math.floor(Math.random() * chars.length)];
    return result;
  };

  useEffect(() => {
    if (isSubmitted && hashedPassword && salt) {
      submitLogin();
    }
  }, [hashedPassword, salt, isSubmitted]);

  const handleSubmit = (e) => {
    setLoading(true);
    e.preventDefault();

    const username = usernameRef.current.value;
    const password = passwordRef.current.value;

    if (username === "" && password === "") {
      setEmptyUsername(true);
      setEmptyPass(true);
      setLoading(false);
      return;
    } else if (username === "") {
      setEmptyUsername(true);
      setEmptyPass(false);
      setLoading(false);
      return;
    } else if (password === "") {
      setEmptyPass(true);
      setEmptyUsername(false);
      setLoading(false);
      return;
    }

    setEmptyUsername(false);
    setEmptyPass(false);

    hashPassword(password);
    setIsSubmitted(true);
  };

  const submitLogin = async () => {
    const username = usernameRef.current.value;

    const request = {
      username,
      password: hashedPassword,  // Using hashed password
      password2: salt,
    };

    try {
      const response = await dispatch(userLogin(request));
      console.log(response.payload);
      if (response.payload === undefined) {
        setLoading(false);
        setIsSubmitted(false);
        showSnackbar("Invalid Username or Password", "error");
      } else {
        setLoading(false);
        successCb(response.payload);
      }
    } catch (error) {
      console.log(error);
      setLoading(false);
      showSnackbar("Oops, something went wrong", "error");
      setIsSubmitted(false);
    }
  };

  const successCb = (response) => {
    if (response.status === true || response.statusCode === "200" || response.status === "SUCCESS") {
      showSnackbar("Login Successful", "success");
      dispatch(fetchRoleDropdown());
      navigate("/dashboard");
    } else {
      showSnackbar(response.message || "Oops, something went wrong", "error");
      setIsSubmitted(false);
    }
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
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
          <Box sx={{ position: 'relative', width: '100%' }}>
            <TextField
              id="password"
              placeholder="Password"
              variant="outlined"
              type={showPassword ? "text" : "password"} // Toggle between text and password
              fullWidth
              inputRef={passwordRef}
              size="small"
              error={emptyPass}
              helperText={emptyPass ? "Password is required" : null}
            />
            <IconButton
              onClick={togglePasswordVisibility}
              sx={{ position: 'absolute', right: 0, top: '50%', transform: 'translateY(-50%)' }}
            >
              {showPassword ? <VisibilityOff /> : <Visibility />}
            </IconButton>
          </Box>
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
          {loading ? (
            <Box sx={{ display: "flex", alignItems: 'center', justifyContent: "center", width: '100%', pb: 2, mt: 2 }}>
              <CircularProgress />
            </Box>
          ) : (
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
          )}
        </Box>
      </DialogContent>
    </Dialog>
  );
};

export default Login;
