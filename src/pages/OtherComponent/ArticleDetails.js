import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Card, CardContent, CardMedia, Typography, Button, Box } from '@mui/material';

const ArticleDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const article = location.state?.article || {};
  console.log(article);

  return (
    <div>
      <Card sx={{ padding: '10px', margin: '20px', background: '#DFF4FF' }}>
        <CardContent>
          <Typography variant="h2"  textAlign="center">
            {article.heading || article.title || 'No Title Available'}
          </Typography>
          <Box display="flex" justifyContent="space-between" alignItems="center" marginTop="10px">
            {(article.author && article.date) && (
              <Typography variant="body1"  gutterBottom sx={{fontWeight:"bold"}}>
                {article.author} - {article.date}
              </Typography>
            )}
            {article.category && (
              <Typography variant="body1"  sx={{fontWeight:"bold"}}>
                {article.category}
              </Typography>
            )}
          </Box>
          {article.image && (
            <CardMedia
              component="img"
              height="300"
              image={article.image}
              alt={article.heading || article.title || 'Article Image'}
              sx={{ borderRadius: "5px", marginBottom: '20px', marginTop:'10px' }}
            />
          )}
          <Typography variant="body1" paragraph  >
            {article.description || 'No description available.'}
          </Typography>
          <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
            <Button variant="contained" sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }} onClick={() => navigate(-1)}>
              Back
            </Button>
            {article.source && (
              <Button variant="contained" sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }} href={article.source} target="_blank" rel="noopener noreferrer">
                View Source
              </Button>
            )}
          </Box>
        </CardContent>
      </Card>
    </div>
  );
};

export default ArticleDetails;
