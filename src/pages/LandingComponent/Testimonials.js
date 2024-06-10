import React, { useState, useEffect } from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';

const Testimonials = () => {
  const testimonialsData = [
    "Testimonial 1: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.'",
    "Testimonial 2: 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.'",
    "Testimonial 3: 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'"
  ];

  const [currentTestimonialIndex, setCurrentTestimonialIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentTestimonialIndex((prevIndex) => (prevIndex + 1) % testimonialsData.length);
    }, 3000);

    return () => clearInterval(interval);
  }, []);

  return (
    <Card sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)'}}>
      <CardContent>
        <Typography variant="h2" align="center" color="#ffff">
          Testimonials
        </Typography>
        <Box sx={{position:'relative',height:'150px'}}>
          {testimonialsData.map((testimonial, index) => (
            <Box
              key={index}
              style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                position: 'absolute',
               
                opacity: index === currentTestimonialIndex ? 1 : 0,
                transition: 'opacity 1s ease-in-out'
              }}
            >
              <Card sx={{ backgroundColor: "white", marginBottom: '30px', padding: '10px' }}>
                <CardContent>
                  <Typography variant="body1">{testimonial}</Typography>
                </CardContent>
              </Card>
            </Box>
          ))}
          </Box>
     
        
      </CardContent>
    </Card>
  );
};

export default Testimonials;