import './App.css';
import LandingPage from './pages/LandingComponent/LandingPage';
import Contact from './pages/OtherComponent/Contact';
import News from './pages/OtherComponent/News';
import FAQ from './pages/OtherComponent/FAQ';
import Blog from './pages/OtherComponent/Blog';
import ArticleDetails from './pages/OtherComponent/ArticleDetails';
import RegistrationForm from './pages/LandingComponent/RegistrationForm';
import OtpVerification from './pages/LandingComponent/OtpVerification';
import ForgotPassword from './pages/LandingComponent/ForgotPassword';
import Login from './pages/LandingComponent/Login';
import Home from './pages/Dashboard/DashboardPages/Home';
import Header from "./pages/Dashboard/layouts/Header";
import LandingPageRoutng from './pages/LandingPageRouting'; 
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { SnackbarProvider } from "./pages/uiComponents/Snackbar";
import { ColorModeContextProvider } from "./utils/ColorModeContext";
import StudentSchoolAttributes_R3 from './pages/Dashboard/DashboardPages/StudentSchoolAttributes_R3';
import BudgetState from './pages/Dashboard/DashboardPages/BudgetState';

function App() {
  return (
    <SnackbarProvider>
      <ColorModeContextProvider>
        <Router>
          <Routes>
            {/* Routes with Navbar and Footer */}
            <Route element={<LandingPageRoutng />}>
              <Route path="/" element={<LandingPage />} />
              <Route path="/contact" element={<Contact />} />
              <Route path="/faq" element={<FAQ />} />
              <Route path="/news" element={<News />} />
              <Route path="/blog" element={<Blog />} />
              <Route path="/article/:id" element={<ArticleDetails />} />
              <Route path="/registrationForm" element={<RegistrationForm />} />
              <Route path="/login" element={<Login />} />
              <Route path="/otpVerify" element={<OtpVerification />} />
              <Route path="/forgotPassword" element={<ForgotPassword />} />
            </Route>
            {/* Routes with Header */}
            <Route element={<Header />}>
              <Route path="/dashboard" element={<Home />} />
              <Route path="/studentLearningBehaviours" element={<StudentSchoolAttributes_R3 />} />
              <Route path="/budgetState" element={<BudgetState />} />
            </Route>
          </Routes>
        </Router>
      </ColorModeContextProvider>
    </SnackbarProvider>
  );
}

export default App;
