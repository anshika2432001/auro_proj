

import React, { useState, useEffect } from "react";
import Carousel from "react-spring-3d-carousel";
import { config } from "react-spring";

// Custom CSS styles for the carousel and dots
const customCarouselStyles = `
  .dots-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }

  .dot {
    width: 10px;
    height: 10px;
    margin: 0 5px;
    background-color: white; 
    border-radius: 50%;
    cursor: pointer;
    transition: background-color 0.3s ease; 
  }

  .dot.active {
    background-color: white; 
    transform: scale(1.5); 
  }
`;

function CustomCarousel({ cards, height, width, margin, offset, showArrows }) {
  const table = cards.map((element, index) => {
    return { ...element, onClick: () => setGoToSlide(index) };
  });

  const [offsetRadius, setOffsetRadius] = useState(offset);
  const [showNavigation, setShowNavigation] = useState(showArrows);
  const [goToSlide, setGoToSlide] = useState(0);
  const [slides] = useState(table);

  useEffect(() => {
    setOffsetRadius(offset);
    setShowNavigation(showArrows);
  }, [offset, showArrows]);

  useEffect(() => {
    const interval = setInterval(() => {
      setGoToSlide(prev => (prev + 1) % slides.length);
    }, 3000);
    return () => clearInterval(interval);
  }, [slides.length]);

  return (
    <div style={{ width, height, margin }}>
      <style>{customCarouselStyles}</style> {/* Inject custom styles */}
      <Carousel
        slides={slides}
        goToSlide={goToSlide}
        offsetRadius={offsetRadius}
        showNavigation={showNavigation}
        animationConfig={config.gentle}
        onChange={(index) => setGoToSlide(index)} // Update the active slide index
      />
      <div className="dots-container">
        {slides.map((_, index) => (
          <div
            key={index}
            className={`dot ${goToSlide === index ? "active" : ""}`}
            onClick={() => setGoToSlide(index)}
          />
        ))}
      </div>
    </div>
  );
}

export default CustomCarousel;
