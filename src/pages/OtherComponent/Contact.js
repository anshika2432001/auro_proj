import React from 'react';
import { Card, CardContent, Typography, Grid, TextField, Button, Box, FormControl, FormLabel, RadioGroup, FormControlLabel, Radio } from '@mui/material';
import PhoneIcon from '@mui/icons-material/Phone';
import EmailIcon from '@mui/icons-material/Email';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import '../../App.css';

const Contact = () => {

  return (
    <div>
      
      <Card sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)'}}>
        <CardContent>
          <Typography variant="h1" align="center" sx={{  color: 'white' }}>
            Contact
          </Typography>
          <Typography variant="body1" align="center" sx={{ color: 'white' }}>
            This is the content below the heading.
          </Typography>
        </CardContent>
      </Card>

      <Card>
        <CardContent>
            <Card >
            <CardContent>
          <Grid container spacing={2} >
            <Grid item xs={12} sm={4} sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)',borderRadius:"10px"}}>
              <Typography variant="h3" sx={{ color:"white" }}>
                Contact Information
              </Typography>
              <Typography variant="body2" color="white">
                some info here.
              </Typography>
              <Box sx={{ marginTop: '20px' }}>
                <Box display="flex" alignItems="center" mb={3} color="white">
                  <PhoneIcon />
                  <Typography variant="body1" sx={{ marginLeft: '10px'}}>
                    Contact No: 123-456-7890
                  </Typography>
                </Box>
                <Box display="flex" alignItems="center" mb={3} color="white">
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
              <Typography variant="h4" >
                Connect with Us
              </Typography>
              <form>
                <Grid container spacing={2} sx={{ marginTop: '10px' }}>
                  <Grid item xs={6}>
                    <TextField 
                     
                    fullWidth 
                    label="First Name" 
                    />
                  </Grid>
                  <Grid item xs={6}>
                    <TextField 
                  
                    fullWidth 
                    label="Last Name" 
                   />
                  </Grid>
                  <Grid item xs={6}>
                    <TextField 
                     
                    fullWidth 
                    label="Email ID" 
                     />
                  </Grid>
                  <Grid item xs={6}>
                    <TextField 
                     
                    fullWidth 
                    label="Mobile No"
                     />
                  </Grid>
                  <Grid item xs={12} sm={4} md={4} lg={12}>
                    <FormControl component="fieldset" fullWidth>
                      <FormLabel component="legend">Select Category?</FormLabel>
                      <RadioGroup row>
                        <FormControlLabel value="donation" control={<Radio />} label={<span className='radioLabel'>Donation</span>} />
                        <FormControlLabel value="technical" control={<Radio />} label={<span className='radioLabel'>Technical Issue</span>} />
                        <FormControlLabel value="partnership" control={<Radio />} label={<span className='radioLabel'>Request For Partnership</span>} />
                        <FormControlLabel value="info" control={<Radio />} label={<span className='radioLabel'>Request For<br></br> More Information</span>} />
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
                    <Button variant="contained" sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)',}}>
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
  )
}

export default Contact
