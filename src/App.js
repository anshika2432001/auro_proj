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
import StudentLearningBehaviour from './pages/Dashboard/DashboardPages/StudentSchoolAttributes_R3/StudentLearningBehaviour';
import StudentInternetBehaviourPatterns from './pages/Dashboard/DashboardPages/StudentSchoolAttributes_R3/StudentInternetBehaviourPatterns';
import BudgetState from './pages/Dashboard/DashboardPages/BudgetState';
import StudentSchoolAttributes_R2 from './pages/Dashboard/DashboardPages/StudentSchoolAttributes_R2';
import StudentSchoolAttributes_R1 from './pages/Dashboard/DashboardPages/StudentSchoolAttributes_R1';
import Teacher_Attributes from './pages/Dashboard/DashboardPages/TeacherAttributes/Teacher_Attributes';
import TeacherTrainingData from './pages/Dashboard/DashboardPages/TeacherAttributes/TeacherTrainingData';
import TeacherSchoolParent from './pages/Dashboard/DashboardPages/TeacherAttributes/TeacherSchoolParent';
import StudentCareerGrowth_R4 from './pages/Dashboard/DashboardPages/StudentCareerGrowth_R4/StudentCareerGrowth_R4';
import SchoolInfrastructure from './pages/Dashboard/DashboardPages/SchoolInfrastructure';
import ParentalandHousehold from './pages/Dashboard/DashboardPages/Parental&Household/Parental&Household';
import ParentSchoolAttribute from './pages/Dashboard/DashboardPages/Parental&Household/ParentSchoolAttribute';
import ViewDetailsComponent from './pages/Dashboard/components/ViewDetailsComponent';

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
             
              <Route path="/studentLearningBehaviours/:id" element={<StudentLearningBehaviour />} />
              <Route path="/studentInternetBehaviours/:id" element={<StudentInternetBehaviourPatterns />} />
              <Route path="/studentR2Attributes/:id" element={<StudentSchoolAttributes_R2 />} />
              <Route path="/studentR1Attributes/:id" element={<StudentSchoolAttributes_R1 />} />
              <Route path="/teacher_Attributes/:id" element={<Teacher_Attributes />} />
              <Route path="/teacherTrainingData/:id" element={<TeacherTrainingData />} />
              <Route path="/teacherSchoolParent/:id" element={<TeacherSchoolParent />} />
              <Route path="/studentCareerGrowth/:id" element={<StudentCareerGrowth_R4 />} />
              <Route path="/schoolInfrastructure" element={<SchoolInfrastructure />} />
              <Route path="/parentalAndHousehold/:id" element={<ParentalandHousehold />} />
              <Route path="/parentSchoolAttribute/:id" element={<ParentSchoolAttribute />} />
              <Route path="/budgetState" element={<BudgetState />} />
            </Route>
            <Route path="/viewDetailsPage" element={<ViewDetailsComponent />} />
          </Routes>
        </Router>
      </ColorModeContextProvider>
    </SnackbarProvider>
  );
}

export default App;
