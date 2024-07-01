import React, { useState } from 'react';
import { AppBar, Toolbar, Tabs, Tab, TextField, Button, Box, Grid, Card, CardMedia, CardContent, Typography, Pagination, useMediaQuery, CardActionArea } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { useTheme } from '@emotion/react';
import { useNavigate } from "react-router-dom";
import BlogImg from '../../images/BlogImage.png';

const Blog = () => {
  const navigate = useNavigate();
  const [category, setCategory] = useState('All');
  const [page, setPage] = useState(1);
  const [searchQuery, setSearchQuery] = useState('');

  const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));

  const blogsData = [
    { id: 1, category: 'Research', title: 'Blog Title 1', description: 'This is a description of blog 1.', author: 'Author 1', date: '2023-06-01', image: BlogImg },
    { id: 2, category: 'Donors', title: 'Blog Title 2', description: 'This is a description of blog 2.', author: 'Author 2', date: '2023-06-02', image: BlogImg },
    { id: 3, category: 'What\'s Happening', title: 'Blog Title 3', description: 'This is a description of blog 3.', author: 'Author 3', date: '2023-06-03', image: BlogImg },
    { id: 4, category: 'Leader\'s Speak', title: 'Blog Title 4', description: 'This is a description of blog 4.', author: 'Author 4', date: '2023-06-04', image: BlogImg },
    { id: 5, category: 'Policy', title: 'Blog Title 5', description: 'This is a description of blog 5.', author: 'Author 5', date: '2023-06-05', image: BlogImg },
    { id: 6, category: 'What\'s Happening', title: 'Blog Title 6', description: 'This is a description of blog 6.', author: 'Author 6', date: '2023-06-03', image: BlogImg },
    { id: 7, category: 'Leader\'s Speak', title: 'Blog Title 7', description: 'This is a description of blog 7.', author: 'Author 7', date: '2023-06-04', image: BlogImg },
    { id: 8, category: 'Policy', title: 'Blog Title 8', description: 'This is a description of blog 8.', author: 'Author 7', date: '2023-06-05', image: BlogImg },
  ];

  const ITEMS_PER_PAGE = 6;

  const handleCategoryChange = (event, newCategory) => {
    setCategory(newCategory);
    setPage(1);
  };

  const handlePageChange = (event, value) => {
    setPage(value);
  };

  const handleBlogClick = (blog) => {
    navigate(`/article/${blog.id}`, { state: { article: blog } });
  };

  const filteredBlogs = blogsData.filter(blog => {
    return (category === 'All' || blog.category === category) && 
           blog.title.toLowerCase().includes(searchQuery.toLowerCase());
  });

  const displayedBlogs = filteredBlogs.slice((page - 1) * ITEMS_PER_PAGE, page * ITEMS_PER_PAGE);

  const handleDetails = () => {
    navigate("/blogDetails");
  };

  return (
    <div>
      <Card sx={{ background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)' }}>
        <CardContent>
          <Typography variant="h1" align="center" sx={{ color: 'white' }}>
            Blog
          </Typography>
          <Typography variant="body1" align="center" sx={{ color: 'white' }}>
            This is the content below the heading.
          </Typography>
        </CardContent>
      </Card>
      <Box sx={{ padding: '20px' }}>
        <AppBar position="static" sx={{ backgroundColor: 'white' }}>
          <Toolbar>
            <Grid container alignItems="center">
              <Grid item xs={12} sm={8}>
                <Tabs
                  value={category}
                  onChange={handleCategoryChange}
                  textColor="primary"
                  indicatorColor="primary"
                  variant={isSmallScreen ? 'scrollable' : 'standard'}
                  scrollButtons={isSmallScreen ? 'auto' : 'off'}
                  sx={{ '& .MuiTab-root': { fontWeight: 'bold', fontSize: '13px', fontFamily: "Inter" } }}
                >
                  <Tab label="All" value="All" />
                  <Tab label="Research" value="Research" />
                  <Tab label="Donors" value="Donors" />
                  <Tab label="What's Happening" value="What's Happening" />
                  <Tab label="Leader's Speak" value="Leader's Speak" />
                  <Tab label="Policy" value="Policy" />
                </Tabs>
              </Grid>
              <Grid item xs={12} sm={4}>
                <Box display="flex" justifyContent="flex-end" alignItems="center">
                  <TextField
                    size="small"
                    placeholder="Search..."
                    value={searchQuery}
                    onChange={e => setSearchQuery(e.target.value)}
                  />
                  <Button variant="contained" size="small" sx={{ minWidth: 'auto', padding: 0, ml: 1 }}>
                    <SearchIcon />
                  </Button>
                </Box>
              </Grid>
            </Grid>
          </Toolbar>
        </AppBar>
        <Grid container spacing={2}>
          {displayedBlogs.map(blog => (
            <Grid item xs={12} sm={6} md={4} key={blog.id}>
              <CardActionArea onClick={() => handleBlogClick(blog)} sx={{ '&:hover': { boxShadow: 6 } }}>
                <Card className="mini-card" sx={{ '&:hover': { boxShadow: 6 } }}>
                  <CardContent>
                    <CardMedia
                      component="img"
                      height="140"
                      image={blog.image}
                      alt={blog.title}
                      sx={{ borderRadius: "5px" }}
                    />
                    <Typography variant="body2" color="textSecondary">
                      {blog.category}
                    </Typography>
                    <Typography variant="h4" gutterBottom>
                      {blog.title}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      {blog.description}
                    </Typography>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', marginTop: 1 }}>
                      <Typography variant="body2">
                        {blog.author}
                      </Typography>
                      <Typography variant="body2">
                        {blog.date}
                      </Typography>
                    </Box>
                  </CardContent>
                </Card>
              </CardActionArea>
            </Grid>
          ))}
        </Grid>
        {filteredBlogs.length > ITEMS_PER_PAGE && (
          <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
            <Pagination
              count={Math.ceil(filteredBlogs.length / ITEMS_PER_PAGE)}
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

export default Blog;
