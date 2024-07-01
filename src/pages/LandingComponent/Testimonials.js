import React, { useState, useEffect } from 'react';
import { Card, CardContent, Typography, Box, Avatar } from '@mui/material';
import ProfileImg from '../../images/ProfileImage.png'

const Testimonials = () => {
  const testimonialsData = [
    {
      photo: {ProfileImg},
      name: 'Divya Pathak',
      designation: 'Designation',
      details: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia odio vitae vestibulum vestibulum. Cras venenatis euismod malesuada.'
    },
    {
      photo: 'https://via.placeholder.com/150',
      name: 'John Doe',
      designation: 'Designation',
      details: 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
    },
    {
      photo: 'https://via.placeholder.com/150',
      name: 'John Doe',
      designation: 'Designation',
      details: 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
    },
    {
      photo: 'https://via.placeholder.com/150',
      name: 'John Doe',
      designation: 'Designation',
      details: 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
    },
    {
      photo: 'https://via.placeholder.com/150',
      name: 'Jane Smith',
      designation: 'Designation',
      details: 'Quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.'
    }
  ];

  const [currentTestimonialIndex, setCurrentTestimonialIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentTestimonialIndex((prevIndex) => (prevIndex + 1) % testimonialsData.length);
    }, 3000);

    return () => clearInterval(interval);
  }, []);

  return (
    <Card sx={{ background: 'linear-gradient(to right, #4772D9, #2899DB, #70CCE2)', padding: '20px' }}>
      <CardContent>
        <Typography variant="h2" align="center" color="#fff" gutterBottom>
          Testimonials
        </Typography>
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', position: 'relative', height: '300px', overflow: 'hidden' }}>
          {testimonialsData.map((testimonial, index) => (
            <Box
              key={index}
              sx={{
                position: 'absolute',
                left: `${(index - currentTestimonialIndex) * 100 + 50}%`,
                transition: 'left 1s ease-in-out',
                width: '50%',
                transform: 'translateX(-50%)',
                opacity: index === currentTestimonialIndex ? 1 : 0.5,
                zIndex: index === currentTestimonialIndex ? 1 : 0,
              
              }}
            >
              <Card className='mini-card' sx={{textAlign:'center'}}>
                <CardContent>
                  <Avatar src={ProfileImg} sx={{ width: 100, height: 100, margin: '0 auto 10px' }} />
                  <Typography variant="h6">{testimonial.name}</Typography>
                  <Typography variant="subtitle1" color="textSecondary">{testimonial.designation}</Typography>
                  <Typography variant="body2">{testimonial.details}</Typography>
                </CardContent>
              </Card>
            </Box>
          ))}
        </Box>
        <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
          {testimonialsData.map((_, index) => (
            <Box
              key={index}
              sx={{
                width: '10px',
                height: '10px',
                backgroundColor: index === currentTestimonialIndex ? 'white' : 'gray',
                borderRadius: '50%',
                margin: '0 5px'
              }}
            />
          ))}
        </Box>
      </CardContent>
    </Card>
  );
};

export default Testimonials;
