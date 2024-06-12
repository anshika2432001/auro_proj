
import './App.css';
import HomePage from './pages/LandingComponent/HomePage';
import LandingPage from './pages/LandingComponent/LandingPage';
import Navbar from './pages/LandingComponent/Navbar';
import {BrowserRouter as Router, Route,Routes } from 'react-router-dom';
import { SnackbarProvider } from "./pages/uiComponents/Snackbar";
import Contact from './pages/OtherComponent/Contact';
import Footer from './pages/LandingComponent/Footer';
import News from './pages/OtherComponent/News';
import FAQ from './pages/OtherComponent/FAQ';
import Blog from './pages/OtherComponent/Blog';
import { ColorModeContextProvider } from "./utils/ColorModeContext";
import RegistrationForm from './pages/LandingComponent/RegistrationForm';
import OtpVerification from './pages/LandingComponent/OtpVerification';

function App() {
  return (
    <SnackbarProvider>
    <ColorModeContextProvider>
    <div>
    
     
    <Router>
      <Navbar />
     
      <Routes>
            <Route exact path = "/" element={<LandingPage/>}></Route>
            <Route exact path = "/contact" element={<Contact/>}></Route>
            <Route exact path = "/news" element={<News/>}></Route>
            <Route exact path = "/faq" element={<FAQ/>}></Route>
            <Route exact path = "/blog" element={<Blog/>}></Route>
            <Route exact path = "/registrationForm" element={<RegistrationForm/>}></Route>
            <Route exact path = "/otpVerify" element={<OtpVerification/>}></Route>
         </Routes>

      <Footer/>
      </Router>
  </div>
  </ColorModeContextProvider>
  </SnackbarProvider>
  );
}

export default App;
