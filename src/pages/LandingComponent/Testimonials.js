// Testimonials.js

import React from "react";
import { v4 as uuidv4 } from "uuid";
import CardData from "./CardData";
import { Card, CardContent, Typography } from "@mui/material";
import CustomCarousel from "./CustomCarousel"; 
import ProfileImg from '../../images/ProfileImage.png'

function Testimonials() {
  const testimonialsData = [
    {
      photo: ProfileImg,
      name: "Divya Pathak",
      designation: "Designation",
      details:
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia odio vitae vestibulum vestibulum. Cras venenatis euismod malesuada.",
    },
    {
      photo: ProfileImg,
      name: "Megha Singh",
      designation: "Designation",
      details:
        "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    },
    {
      photo: ProfileImg,
      name: "Anjali Roy",
      designation: "Designation",
      details:
        "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    },
    {
      photo: ProfileImg,
      name: "Ishika Jain",
      designation: "Designation",
      details:
        "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    },
    {
      photo: ProfileImg,
      name: "Trisha Pathak",
      designation: "Designation",
      details:
        "Quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
    },
  ];

  const cards = testimonialsData.map((testimonial) => ({
    key: uuidv4(),
    content: <CardData testimonial={testimonial} />,
  }));

  return (
    <Card sx={{ background: "linear-gradient(to right, #4772D9, #2899DB, #70CCE2)" }}>
      <CardContent>
        <Typography variant="h2" align="center" color="#fff" mb={4}>
          Testimonials
        </Typography>
        <CustomCarousel
          cards={cards}
          height="300px"
          width="50%"
          margin="0 auto"
          offset={2}
          showArrows={false}
        />
      </CardContent>
    </Card>
  );
}

export default Testimonials;
