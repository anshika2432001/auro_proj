import React from 'react';
import { useNavigate} from "react-router-dom";
import '../../App.css'
import { Card, CardContent,Button,Typography,Grid, CardMedia,Box } from '@mui/material';
import NetworksImage from '../../images/Networks1.png'
import Image1 from '../../images/Image1.png'
import Image2 from '../../images/Image2.png'
import Image3 from '../../images/Image3.png'
import Image4 from '../../images/Image4.png'
import Image5 from '../../images/LadyImage.png'
import AuroLogo from '../../images/AuroLogo.png'
import PlayCircleIcon from '@mui/icons-material/PlayCircle';


const HomePage = () => {
  const navigate = useNavigate();

  const handleRegisterForm = ()=> {
    navigate("/registrationForm")
  }

  return (
    <div>
       <Card sx={{background: 'linear-gradient(to right,#4772D9, #2899DB,#70CCE2)'}}>
      <CardContent>
        <Grid container  alignItems="center" >
          {/* Left side */}
          <Grid item xs={12} sm={7} padding="30px">
            <Typography variant="h1" >
              A Captivative Heading will be placed here.
            </Typography>
            <Typography variant="body1" gutterBottom sx={{mt:3,color:"white"}}>
              Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium eos fuga laborum doloremque. Modi libero nam, fugit quasi quidem aliquam placeat officia possimus ad accusamus inventore eius, nesciunt iure deserunt.
            </Typography>
            <Button variant="contained"   onClick={()=> handleRegisterForm()} sx={{backgroundColor:"white",color:"#4772D9",mt:3}}>
              Register Now
            </Button>
            <Button variant="outlined"  sx={{ ml: 1,color:"white",mt:3 }}>
              <PlayCircleIcon sx={{mr:1}}/>
              Watch Video
            </Button>
            <Box sx={{ backgroundColor: "white", color: "#20252C", padding: '5px 10px', mt: 1, display: 'flex', alignItems: 'center', borderRadius: '5px', width: 'fit-content' }}>
                <Typography variant="body2" sx={{ fontWeight: 'bold', mr: 1 }}>
                  Powered By
                </Typography>
                <Box
                  component="img"
                  src={AuroLogo}
                  alt="Powered By Logo"
                  sx={{ width: '70px', height: 'auto' }}
                />
              </Box>
            
          </Grid>
          {/* Right side */}
          <Grid item xs={12} sm={5}>
           <Box
           sx={{position: 'relative',width:"400px",height:"400px"}}
           >
            <Box
            component = "img"
            src={NetworksImage}
            alt= "Image1"
            sx={{position:'absolute',
              top:'10px',left:'10px',width:"400px",
              height:"400px",
            }}

            />
              <Box
            component = "img"
            src={Image1}
            alt= "Image1"
            sx={{position:'absolute',
              top:'175px',left:'175px',width:"70px",
              height:"70px",
            }}

            />
              <Box
            component = "img"
            src={Image2}
            alt= "Image1"
            sx={{position:'absolute',
              top:'30px',left:'40px',width:"70px",
              height:"70px",
            }}

            />
              <Box
            component = "img"
            src={Image3}
            alt= "Image1"
            sx={{position:'absolute',
              top:'10px',left:'300px',width:"70px",
              height:"70px",
            }}

            />
            <Box
            component = "img"
            src={Image4}
            alt= "Image1"
            sx={{position:'absolute',
              top:'300px',left:'300px',width:"70px",
              height:"70px",
            }}
            />
            <Box
            component = "img"
            src={Image5}
            alt= "Image5"
            sx={{position:'absolute',
              top:'250px',left:'10px',width:"230px",
              height:"230px",
            }}

            />

           </Box>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
    </div>
  )
}

export default HomePage
