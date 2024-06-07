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
    <Card sx={{ backgroundColor: 'lightblue', color: 'white', paddingBottom: '100px',  textAlign: 'center' }}>
      <CardContent>
        <Typography variant="h4" gutterBottom>
          Testimonials
        </Typography>
        <div style={{  margin: '0 auto',  }}>
          {testimonialsData.map((testimonial, index) => (
            <div
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
            </div>
          ))}
        </div>
        
      </CardContent>
    </Card>
  );
};

export default Testimonials;