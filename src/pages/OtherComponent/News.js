import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, CardContent, CardMedia, Typography, Grid, TextField, Button, Box, Pagination, CardActionArea } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import NewsImg from '../../images/NewsImage.png';

const News = () => {
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(1);

  const newsData = [
    {
      id: 1,
      author: 'BBC News',
      date: '2024-06-06',
      heading: 'Breaking News 1',
      image: NewsImg,
      description: 'This is a two-line description of the news article.',
      source: 'https://example.com'
    },
    {
      id: 2,
      author: 'BBC News',
      date: '2024-06-06',
      heading: 'Breaking News 2',
      image: NewsImg,
      description: 'This is a two-line description of the news article.',
      source: 'https://example.com'
    },
    {
      id: 3,
      author: 'ABC News',
      date: '2024-06-15',
      heading: 'Breaking News 3',
      image: NewsImg,
      description: 'This is a two-line description of the news article.',
      source: 'https://example.com'
    },
    {
      id: 4,
      author: 'ABC News',
      date: '2024-06-15',
      heading: 'Breaking News 4',
      image: NewsImg,
      description: 'This is a two-line description of the news article.',
      source: 'https://example.com'
    },
    {
      id: 5,
      author: 'ABC News',
      date: '2024-06-15',
      heading: 'Breaking News 5',
      image: NewsImg,
      description: 'This is a two-line description of the news article.',
      source: 'https://example.com'
    },
    {
      id: 6,
      author: 'ABC News',
      date: '2024-06-15',
      heading: 'Breaking News 6',
      image: NewsImg,
      description: 'This is a two-line description of the news article.',
      source: 'https://example.com'
    },
    {
      id: 7,
      author: 'ABC News',
      date: '2024-06-15',
      heading: 'Breaking News 7',
      image: NewsImg,
      description: 'This is a two-line description of the news article.',
      source: 'https://example.com'
    },
    {
      id: 8,
      author: 'ABC News',
      date: '2024-06-15',
      heading: 'Breaking News 8',
      image: NewsImg,
      description: 'This is a two-line description of the news article.',
      source: 'https://example.com'
    },
    // Add more news items here
  ];

  const ITEMS_PER_PAGE = 6;

  const navigate = useNavigate();

  const filteredNews = newsData.filter((news) =>
    news.heading.toLowerCase().includes(search.toLowerCase())
  );

  const handleSearchChange = (event) => {
    setSearch(event.target.value);
    setPage(1);
  };

  const handlePageChange = (event, value) => {
    setPage(value);
  };

  const handleNewsClick = (news) => {
    navigate(`/article/${news.id}`, { state: { article: news } });
  };

  const displayedNews = filteredNews.slice(
    (page - 1) * ITEMS_PER_PAGE,
    page * ITEMS_PER_PAGE
  );

  return (
    <div>
      <Card sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }}>
        <CardContent>
          <Typography variant="h1" align="center" sx={{ color: 'white' }}>
            News
          </Typography>
          <Typography variant="body1" align="center" sx={{ color: 'white' }}>
            This is the content below the heading.
          </Typography>
        </CardContent>
      </Card>

      <Box sx={{ padding: '20px' }}>
        <Box display="flex" justifyContent="flex-end" alignItems="center" mb={2}>
          <TextField
            size="small"
            placeholder="Search..."
            onChange={handleSearchChange}
          />
          <Button variant="contained" color="primary" size="small" sx={{ minWidth: 'auto',ml:1, p: 0 }}><SearchIcon /></Button>
        </Box>
        <Grid container spacing={2}>
          {displayedNews.map((news, index) => (
            <Grid item xs={12} sm={6} md={4} key={index}>
              <CardActionArea onClick={() => handleNewsClick(news)} sx={{ '&:hover': { boxShadow: 6 } }}>
                <Card sx={{ borderTop: '4px solid #2899DB' }} className='mini-card'>
                  <CardContent>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                      <Typography variant="body1">
                        {news.author}
                      </Typography>
                      <Typography variant="body1">
                        {news.date}
                      </Typography>
                    </Box>
                    <Typography mb={1} variant="h4">
                      {news.heading}
                    </Typography>
                    <CardMedia
                      component="img"
                      height="140"
                      image={news.image}
                      alt={news.heading}
                      sx={{ borderRadius: "5px" }}
                    />
                    <Typography variant="body1" mt={2}>
                      {news.description}
                    </Typography>
                  </CardContent>
                </Card>
              </CardActionArea>
            </Grid>
          ))}
        </Grid>
        {filteredNews.length > ITEMS_PER_PAGE && (
          <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
            <Pagination
              count={Math.ceil(filteredNews.length / ITEMS_PER_PAGE)}
              page={page}
              onChange={handlePageChange}
              color="primary"
            />
          </Box>
        )}
      </Box>
    </div>
  );
};

export default News;
