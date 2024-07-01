import React from 'react';
import { Card, CardContent, Typography, Grid, TextField, Button, Box, FormControl, FormLabel, RadioGroup, FormControlLabel, Radio } from '@mui/material';
import PhoneIcon from '@mui/icons-material/Phone';
import EmailIcon from '@mui/icons-material/Email';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import { useFormik } from 'formik';
import * as yup from 'yup';
import '../../App.css';

const validationSchema = yup.object({
  firstName: yup.string('Enter your first name').required('First Name is required'),
  lastName: yup.string('Enter your last name').required('Last Name is required'),
  email: yup.string('Enter your email').email('Enter a valid email').required('Email is required'),
  mobile: yup.string('Enter your mobile number').matches(/^[0-9]{10}$/, 'Enter a valid mobile number').required('Mobile number is required'),
  category: yup.string('Select a category').required('Category is required'),
  message: yup.string('Enter your message').required('Message is required'),
});

const Contact = () => {
  const formik = useFormik({
    initialValues: {
      firstName: '',
      lastName: '',
      email: '',
      mobile: '',
      category: '',
      message: '',
    },
    validationSchema: validationSchema,
    onSubmit: (values) => {
      console.log(values);
      // Handle form submission here
    },
  });

  return (
    <div>
      <Card sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }}>
        <CardContent>
          <Typography variant="h1" align="center" sx={{ color: 'white' }}>
            Contact
          </Typography>
          <Typography variant="body1" align="center" sx={{ color: 'white' }}>
            This is the content below the heading.
          </Typography>
        </CardContent>
      </Card>

      <Card>
        <CardContent>
          <Card>
            <CardContent>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={4} sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)', borderRadius: '10px' }}>
                  <Typography variant="h3" sx={{ color: "white" }}>
                    Contact Information
                  </Typography>
                  <Typography variant="body2" color="white">
                    some info here.
                  </Typography>
                  <Box sx={{ marginTop: '20px' }}>
                    <Box display="flex" alignItems="center" mb={3} color="white">
                      <PhoneIcon />
                      <Typography variant="body1" sx={{ marginLeft: '10px' }}>
                        +91 9667480783
                      </Typography>
                    </Box>
                    <Box display="flex" alignItems="center" mb={3} color="white">
                      <EmailIcon />
                      <Typography variant="body1" sx={{ marginLeft: '10px' }}>
                        info@auroscholar.com
                      </Typography>
                    </Box>
                    <Box display="flex" alignItems="center" color="white">
                      <LocationOnIcon />
                      <Typography variant="body1" sx={{ marginLeft: '10px' }}>
                        Plot-1201, Vasundhara, Ghaziabad, Uttar Pradesh 201012
                      </Typography>
                    </Box>
                  </Box>
                </Grid>
                <Grid item xs={12} sm={8} sx={{ borderRadius: '10px' }}>
                  <Typography variant="h4">
                    Connect with Us
                  </Typography>
                  <form onSubmit={formik.handleSubmit}>
                    <Grid container spacing={2} sx={{ marginTop: '10px' }}>
                      <Grid item xs={6}>
                        <TextField
                          fullWidth
                          id="firstName"
                          name="firstName"
                          label="First Name"
                          value={formik.values.firstName}
                          onChange={formik.handleChange}
                          onBlur={formik.handleBlur}
                          error={formik.touched.firstName && Boolean(formik.errors.firstName)}
                          helperText={formik.touched.firstName && formik.errors.firstName}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextField
                          fullWidth
                          id="lastName"
                          name="lastName"
                          label="Last Name"
                          value={formik.values.lastName}
                          onChange={formik.handleChange}
                          onBlur={formik.handleBlur}
                          error={formik.touched.lastName && Boolean(formik.errors.lastName)}
                          helperText={formik.touched.lastName && formik.errors.lastName}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextField
                          fullWidth
                          id="email"
                          name="email"
                          label="Email ID"
                          value={formik.values.email}
                          onChange={formik.handleChange}
                          onBlur={formik.handleBlur}
                          error={formik.touched.email && Boolean(formik.errors.email)}
                          helperText={formik.touched.email && formik.errors.email}
                        />
                      </Grid>
                      <Grid item xs={6}>
                        <TextField
                          fullWidth
                          id="mobile"
                          name="mobile"
                          label="Mobile No"
                          value={formik.values.mobile}
                          onChange={formik.handleChange}
                          onBlur={formik.handleBlur}
                          error={formik.touched.mobile && Boolean(formik.errors.mobile)}
                          helperText={formik.touched.mobile && formik.errors.mobile}
                        />
                      </Grid>
                      <Grid item xs={12}>
                        <FormControl component="fieldset" fullWidth error={formik.touched.category && Boolean(formik.errors.category)}>
                          <FormLabel component="legend">Select Category:</FormLabel>
                          <RadioGroup
                            row
                            id="category"
                            name="category"
                            value={formik.values.category}
                            onChange={formik.handleChange}
                          >
                            <FormControlLabel value="donation" control={<Radio />} label={<span className='radioLabel'>Donation</span>} />
                            <FormControlLabel value="technical" control={<Radio />} label={<span className='radioLabel'>Technical Issue</span>} />
                            <FormControlLabel value="partnership" control={<Radio />} label={<span className='radioLabel'>Request For Partnership</span>} />
                            <FormControlLabel value="info" control={<Radio />} label={<span className='radioLabel'>Request For<br /> More Information</span>} />
                          </RadioGroup>
                          {formik.touched.category && formik.errors.category && (
                            <Typography variant="body2" color="error">{formik.errors.category}</Typography>
                          )}
                        </FormControl>
                      </Grid>
                      <Grid item xs={12}>
                        <TextField
                          fullWidth
                          multiline
                          rows={4}
                          id="message"
                          name="message"
                          placeholder="Write a message"
                          variant="outlined"
                          value={formik.values.message}
                          onChange={formik.handleChange}
                          onBlur={formik.handleBlur}
                          error={formik.touched.message && Boolean(formik.errors.message)}
                          helperText={formik.touched.message && formik.errors.message}
                        />
                      </Grid>
                      <Grid item xs={12} textAlign="right">
                        <Button type="submit" variant="contained" sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }}>
                          Send Message
                        </Button>
                      </Grid>
                    </Grid>
                  </form>
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </CardContent>
      </Card>
    </div>
  );
};

export default Contact;
