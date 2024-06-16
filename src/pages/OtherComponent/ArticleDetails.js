import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Card, CardContent, CardMedia, Typography, Button, Box } from '@mui/material';

const ArticleDetails = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const article = location.state?.article || {};
console.log(article)
  return (
    <div>
     
      <Card sx={{ padding: '20px', margin: '20px',backgroundColor:"#DFF4FF" }}>
        <CardContent>
          <Typography variant="h3" gutterBottom color="#4772D9">
            {article.heading || article.title || 'No Title Available'}
          </Typography>
          {(article.author)  && article.date && (
            <Typography variant="body2" color="textSecondary" gutterBottom>
              { article.author} - {article.date}
            </Typography>
          )}
          {article.image && (
            <CardMedia
              component="img"
              height="300"
              image={article.image}
              alt={article.heading || article.title || 'Article Image'}
              sx={{ borderRadius: "5px", marginBottom: '20px' }}
            />
          )}
          {article.category &&
          <Typography variant="h4" paragraph>
          {article.category }
        </Typography>
          }
          <Typography variant="body1" paragraph>
            {article.description || 'No description available.'}
          </Typography>
          <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
            <Button variant="contained" sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)'}} onClick={() => navigate(-1)}>
              Back
            </Button>
            {article.source && (
              <Button variant="contained" color="secondary" href={article.source} target="_blank" rel="noopener noreferrer">
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
