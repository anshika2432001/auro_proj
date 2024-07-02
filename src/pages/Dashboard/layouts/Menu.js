import { Route, Navigate, Routes } from "react-router-dom";
import React, { lazy, Suspense } from "react";

import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import DashboardSharpIcon from "@mui/icons-material/DashboardSharp";
import PersonIcon from '@mui/icons-material/Person';
import PeopleAltIcon from '@mui/icons-material/PeopleAlt';
import ShowChartIcon from '@mui/icons-material/ShowChart';
import HomeWorkIcon from '@mui/icons-material/HomeWork';

import PaidIcon from '@mui/icons-material/Paid';
import BudgetState from "../DashboardPages/BudgetState";


const Home = lazy(() => import("../DashboardPages/Home"));
const StudentSchoolAttributes_R3 = lazy(() => import("../DashboardPages/StudentSchoolAttributes_R3"));


export const menu = [
  {
    icon: <DashboardSharpIcon sx={{color:"white"}}/>,
    title: "Dashboard",
    pageLink: "/dashboard",
    view: <Home />,
  },
 
  
  {
    icon: <AccountBalanceIcon sx={{color:"white"}}/>,
    title: "Student Learning Outcomes(R1)",
    items: [
      {
        
            title: "Subject Wise - Average Score",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Gradewise - Average Score",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Microscholarship Amount",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
        {
          title: "Microscholarship Quizzes - Total Quizzes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        },
      {
        title: "Total Winning Quizzes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Subject Wise - % improvement",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Gradewise - % improvement",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },

      {
        title: "Top Performing Topics",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      }, 
     
      {
        title: "Microscholarship Won",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Topicwise breakdown - Student Attempts",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Topicwise breakdown - No. of Winning Quizzes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Topicwise breakdown - Average Score",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Topicwise breakdown - % improvement",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Weak Performing Topics",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Core-Retake No of Students",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },

    ]

  },
  {
    icon: <AccountBalanceIcon sx={{color:"white"}}/>,
    title: "Student - School Attributes(R2)",
    items: [
      {
        
            title: "PrePrimary School",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Student Strength of the Classroom",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Types of Student Clubs",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
        {
          title: "Academic Stream",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        },
      {
        title: "First or home language is the same as that in the school",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Student with access to Bank Acoount/ UPI ",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Students engagement in extra curricular activities in school",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },

      {
        title: "Students in leadership positions in school clubs",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      }, 
     
      
    ]

  },
  {
    icon: <AccountBalanceIcon sx={{color:"white"}}/>,
    title: "Student School Attributes(R3)",
    items: [
      {
        title: "Student Learning Behaviours",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        items: [
          {
            title: "Hours of individual study/practice per day",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Student learning style preferences",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Student collaborative learning style preferences",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
        {
          title: "Paid Private Tuition Hours",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        },
      {
        title: "Children who read other materials in addition to textbooks",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Paid Private Tuition Subjectwise",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      }

        ]
       
      },
  

    ]

  },
  {
    icon: <PersonIcon sx={{color:"white"}}/>,
    title: "Teacher Attributes",
    items: [
      {
        title: "Teacher - Attributes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        items: [
          {
            title: "Teacher - Grade",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Teacher - Number of classes ratio",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Teacher - Student score range",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
        {
          title: "Teacher - Subject",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        },
      {
        title: "Average Teacher Salary",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Teacher - Pupil Ratio",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Teacher Qualification",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      }

        ]
       
      },
      {
        title: "Teacher - Training Data",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        items: [
          {
            title: "Teachers trained on CCE and Classroom based assessment",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Teacher needs of the teacher",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Nature of employment of teachers",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
        {
          title: "Time Spent by teacher on school related activities weekly",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        },
      {
        title: "Teachers satisfied with trainings by School/ Education Dept",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Teachers with platform to share best practices",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Time spent by teacher in mandatory training annually",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Teachers aware in pedagogical methodologies",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Periodicity of formative assessments in school",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "Schools utilising teaching resources by the SCRET / DIETs",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },

        ]
       
      },
      {
        title: "Teacher - School - Parent Attributes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        items: [
          {
            title: "Frequency of parent teacher meetings",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "CSRs engaged with school in last academic year",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
          {
            title: "Schools with SMCs",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
          },
        {
          title: "Schools registered on Vidyanjali portal =",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        },
      {
        title: "Functional SMCs",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      {
        title: "No of Teachers in school",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
      },
      
        ]
       
      },

    ]

  },
  {
    icon: <ShowChartIcon sx={{color:"white"}}/>,
    title: "Student Career Growth Outlook(R4)",
    items: [
      {
        title: "Student- Higher Education Attributes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        
       
      },
  

    ]

  },
  {
    icon: <HomeWorkIcon sx={{color:"white"}}/>,
    title: "School Infrastructure",
    items: [
      {
        title: "School having handwash facility",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        
      
       
      },
  

    ]

  },
  {
    icon: <PeopleAltIcon sx={{color:"white"}}/>,
    title: "Parental & Household Engagement",
    items: [
      {
        title: "Parental - Student - Household Attributes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentSchoolAttributes_R3 />,
        
      
       
      },
  

    ]

  },
  {
    icon: <PaidIcon sx={{color:"white"}}/>,
    title: "Budget State",
    pageLink: '/budgetState',
    view: <BudgetState />,
    

  },
  {
    icon: <PersonIcon sx={{color:"white"}}/>,
    title: "User Management",
    

  },
  
];

export const AppRoutes = () => {
  return (
    <Suspense fallback={<div />}>
      <Routes>
        {/* <Route path="/" element={<Login/>} exact />
        <Route path="/home" element={<Home/>} /> */}

        {/* {menu.map((page, index) => {
            return (
              <Route
                exact
                element={page.pageLink}
                render={({match}) => <page.view />}
                key={index}
              />
            );
          })} */}
        {/* <Navigate to="/" /> */}
      </Routes>
    </Suspense>
  );
};
