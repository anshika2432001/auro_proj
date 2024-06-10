import React, { useState } from 'react';
import { Card, CardContent,Box, Typography, IconButton, Collapse,Grid,Pagination } from '@mui/material';
import { ExpandMore, ExpandLess } from '@mui/icons-material';

const FAQ = () => {
  
    const questionsData = [
        { question: 'What is React?', answer: 'React is a JavaScript library for building user interfaces.' },
        { question: 'What is Material-UI?', answer: 'Material-UI is a popular React UI framework.' },
        { question: 'How to use hooks in React?', answer: 'Hooks are functions that let you use state and other React features without writing a class.' },
        { question: 'What is a component?', answer: 'A component is a reusable piece of code that represents a part of the user interface.' },
        { question: 'How to manage state in React?', answer: 'State in React can be managed using hooks like useState and useReducer.' },
        { question: 'What is JSX?', answer: 'JSX is a syntax extension that allows writing HTML-like code within JavaScript.' },
        { question: 'What is a prop in React?', answer: 'Props are inputs to a React component that allow data to be passed from parent to child components.' },
        { question: 'How to handle events in React?', answer: 'Events in React are handled using event handlers like onClick, onChange, etc.' },
        { question: 'What is JSX?', answer: 'JSX is a syntax extension that allows writing HTML-like code within JavaScript.' },
        { question: 'What is a prop in React?', answer: 'Props are inputs to a React component that allow data to be passed from parent to child components.' },
        { question: 'How to handle events in React?', answer: 'Events in React are handled using event handlers like onClick, onChange, etc.' },
     
      ];

      const [page, setPage] = useState(1);
      const [expandedIndex, setExpandedIndex] = useState(null);
      const ITEMS_PER_PAGE = 8;
    
      const handleExpandClick = (index) => {
        setExpandedIndex(expandedIndex === index ? null : index);
      };
    
      const handlePageChange = (event, value) => {
        setPage(value);
        setExpandedIndex(null);  // Reset expanded state when page changes
      };
    
      const displayedQuestions = questionsData.slice(
        (page - 1) * ITEMS_PER_PAGE,
        page * ITEMS_PER_PAGE
      );
      console.log(displayedQuestions)
      
  return (
    <div>
         
      
         <Card sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)'}}>
        <CardContent>
          <Typography variant="h1" align="center" sx={{  color: 'white' }}>
            FAQ's
          </Typography>
          <Typography variant="body1" align="center" sx={{ color: 'white' }}>
            This is the content below the heading.
          </Typography>
        </CardContent>
      </Card>

        <Card>
      <Grid container spacing={3}>
        {displayedQuestions.map((item, index) => (
          <Grid item xs={12} sm={6} key={index}>
            <Card  sx={{padding:"0px" }}>
              <CardContent>
                <Typography  variant="h4" justifyContent="space-between" display="flex" alignItems="center">
                  {item.question}
                  <IconButton onClick={() => handleExpandClick(index)}>
                    {expandedIndex === index ? <ExpandLess /> : <ExpandMore />}
                  </IconButton>
                </Typography>
                <Collapse in={expandedIndex === index} timeout="auto" unmountOnExit>
                  <Typography variant="body1" color="textSecondary">
                    {item.answer}
                  </Typography>
                </Collapse>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      {questionsData.length > ITEMS_PER_PAGE && (
        <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
          <Pagination
            count={Math.ceil(questionsData.length / ITEMS_PER_PAGE)}
            page={page}
            onChange={handlePageChange}
            color="primary"
          />
        </Box>
      )}
    </Card>
    
      
    </div>
  )
}

export default FAQ
