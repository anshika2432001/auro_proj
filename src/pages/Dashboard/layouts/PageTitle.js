import { Typography } from '@mui/material';
import { Box } from '@mui/system';
import React from 'react';
// import Typography from '@mui/material/Typography';


const PageTitle = (name) => {
  return (
    <div>
        <Typography variant='h4' >{name.name}</Typography>
    </div>
  )
}

export default PageTitle
