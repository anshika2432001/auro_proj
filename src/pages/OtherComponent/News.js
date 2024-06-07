import React,{ useState } from 'react';
import { Card, CardContent,CardMedia, Typography, Grid, TextField, Button, Box, Pagination } from '@mui/material';

const News = () => {

    const [search, setSearch] = useState('');
    const [page, setPage] = useState(1);

    const newsData = [
        {
          source: 'BBC News',
          date: '2024-06-06',
          heading: 'Breaking News 1',
          image: 'https://via.placeholder.com/150',
          description: 'This is a two-line description of the news article.',
        },
        {
            source: 'BBC News',
            date: '2024-06-06',
            heading: 'Breaking News 1',
            image: 'https://via.placeholder.com/150',
            description: 'This is a two-line description of the news article.',
          },
          {
            source: 'BBC News',
            date: '2024-06-06',
            heading: 'Breaking News 1',
            image: 'https://via.placeholder.com/150',
            description: 'This is a two-line description of the news article.',
          },
          {
            source: 'BBC News',
            date: '2024-06-06',
            heading: 'Breaking News 1',
            image: 'https://via.placeholder.com/150',
            description: 'This is a two-line description of the news article.',
          },
          {
            source: 'BBC News',
            date: '2024-06-06',
            heading: 'Breaking News 1',
            image: 'https://via.placeholder.com/150',
            description: 'This is a two-line description of the news article.',
          },
          {
            source: 'BBC News',
            date: '2024-06-06',
            heading: 'Breaking News 1',
            image: 'https://via.placeholder.com/150',
            description: 'This is a two-line description of the news article.',
          },
          {
            source: 'BBC News',
            date: '2024-06-06',
            heading: 'Breaking News 1',
            image: 'https://via.placeholder.com/150',
            description: 'This is a two-line description of the news article.',
          },
          {
            source: 'BBC News',
            date: '2024-06-06',
            heading: 'Breaking News 1',
            image: 'https://via.placeholder.com/150',
            description: 'This is a two-line description of the news article.',
          },
        // Add more news items here
      ];
      
      const ITEMS_PER_PAGE = 6;
      

        
      
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
      
        const displayedNews = filteredNews.slice(
          (page - 1) * ITEMS_PER_PAGE,
          page * ITEMS_PER_PAGE
        );
    
  return (
    <div>
         <Box >
      <Card sx={{ width: '100%', backgroundColor: 'lightblue', padding: '20px', marginBottom: '20px' }}>
        <CardContent>
          <Typography variant="h4" align="center" sx={{ fontWeight: 'bold', color: 'white' }}>
            News & Media
          </Typography>
          <Typography variant="body1" align="center" sx={{ color: 'white', marginTop: '20px' }}>
            This is the content below the heading.
          </Typography>
        </CardContent>
      </Card>
      <Box sx={{ padding: '20px' }}>
      <Box sx={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '20px',marginRight:'25px' }}>
        <TextField
          label="Search"
          variant="outlined"
          value={search}
          onChange={handleSearchChange}
          size="small"
          sx={{marginRight:"10px"}}
        />
        <Button variant="contained" color="primary">Search</Button>
      </Box>
      <Grid container spacing={2}>
        {displayedNews.map((news, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <Card sx={{ maxWidth: 345, margin: 2,borderTop: '4px solid blue' }}>
            <CardContent>
            <Typography variant="subtitle2" color="primary">
          {news.source} 
        </Typography>
        <Typography variant="subtitle2" color="textSecondary">
           {news.date}
        </Typography>
        <Typography gutterBottom variant="h5" component="div">
          {news.heading}
        </Typography>
      <CardMedia
        component="img"
        height="140"
        image={news.image}
        alt={news.heading}
        sx={{borderRadius:"10px"}}
      />
      
       
       
        <Typography variant="body2" color="textSecondary" mt={2}>
          {news.description}
        </Typography>
      </CardContent>
    </Card>
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
      </Box>
      
    </div>
  )
}

export default News
