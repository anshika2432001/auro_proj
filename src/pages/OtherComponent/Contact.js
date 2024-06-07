import React from 'react';
import { Card, CardContent, Typography, Grid, TextField, Button, Box, FormControl, FormLabel, RadioGroup, FormControlLabel, Radio } from '@mui/material';
import PhoneIcon from '@mui/icons-material/Phone';
import EmailIcon from '@mui/icons-material/Email';
import LocationOnIcon from '@mui/icons-material/LocationOn';

const Contact = () => {
  return (
    <div>
       <Box >
      <Card sx={{ width: '100%', backgroundColor: 'lightblue', padding: '20px', marginBottom: '20px' }}>
        <CardContent>
          <Typography variant="h4" align="center" sx={{ fontWeight: 'bold', color: 'white' }}>
            Contact
          </Typography>
          <Typography variant="body1" align="center" sx={{ color: 'white', marginTop: '20px' }}>
            This is the content below the heading.
          </Typography>
        </CardContent>
      </Card>

      <Card>
        <CardContent>
            <Card sx={{padding:"30px 100px",borderRadius:"20px"}} >
            <CardContent>
          <Grid container spacing={2} >
            <Grid item xs={12} sm={4} sx={{backgroundColor:"blue",borderRadius:"10px"}}>
              <Typography variant="h5" sx={{ fontWeight: 'bold',color:"white" }}>
                Contact Information
              </Typography>
              <Typography  sx={{ color:"white",fontSize:"15px" }}>
                some info here.
              </Typography>
              <Box sx={{ marginTop: '40px' }}>
                <Box display="flex" alignItems="center" mb={5} color="white">
                  <PhoneIcon />
                  <Typography variant="body1" sx={{ marginLeft: '10px'}}>
                    Contact No: 123-456-7890
                  </Typography>
                </Box>
                <Box display="flex" alignItems="center" mb={5} color="white">
                  <EmailIcon />
                  <Typography variant="body1" sx={{ marginLeft: '10px' }}>
                    Email: info@example.com
                  </Typography>
                </Box>
                <Box display="flex" alignItems="center" color="white">
                  <LocationOnIcon />
                  <Typography variant="body1" sx={{ marginLeft: '10px' }}>
                    Address: 123 Main St, City, Country
                  </Typography>
                </Box>
              </Box>
            </Grid>
            <Grid item xs={12} sm={8} sx={{borderRadius:"10px"}}>
              <Typography variant="h5" sx={{ fontWeight: 'bold' }}>
                Connect with Us
              </Typography>
              <form>
                <Grid container spacing={2} sx={{ marginTop: '20px' }}>
                  <Grid item xs={6}>
                    <TextField 
                     size="small"
                    fullWidth 
                    label="First Name" 
                    variant="outlined" />
                  </Grid>
                  <Grid item xs={6}>
                    <TextField 
                      size="small"
                    fullWidth 
                    label="Last Name" 
                    variant="outlined" />
                  </Grid>
                  <Grid item xs={6}>
                    <TextField 
                      size="small"
                    fullWidth 
                    label="Email ID" 
                    variant="outlined" />
                  </Grid>
                  <Grid item xs={6}>
                    <TextField 
                      size="small"
                    fullWidth 
                    label="Mobile No"
                     variant="outlined" />
                  </Grid>
                  <Grid item xs={12}>
                    <FormControl component="fieldset" fullWidth>
                      <FormLabel component="legend">Select Subject?</FormLabel>
                      <RadioGroup row>
                        <FormControlLabel value="subject1" control={<Radio />} label="Subject 1" />
                        <FormControlLabel value="subject2" control={<Radio />} label="Subject 2" />
                        <FormControlLabel value="subject3" control={<Radio />} label="Subject 3" />
                        <FormControlLabel value="subject4" control={<Radio />} label="Subject 4" />
                      </RadioGroup>
                    </FormControl>
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      multiline
                      rows={4}
                      placeholder="Write a message"
                      variant="outlined"
                    />
                  </Grid>
                  <Grid item xs={12} textAlign="right">
                    <Button variant="contained" color="primary">
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
    </Box>
    </div>
  )
}

export default Contact
