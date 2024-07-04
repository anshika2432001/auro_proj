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
import StudentSchoolAttributes_R2 from "../DashboardPages/StudentSchoolAttributes_R2";
import StudentLearningBehaviour from "../DashboardPages/StudentSchoolAttributes_R3/StudentLearningBehaviour";
import Home from "../DashboardPages/Home";






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
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Gradewise - Average Score",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Microscholarship Amount",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
        {
          title: "Microscholarship Quizzes - Total Quizzes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        },
      {
        title: "Total Winning Quizzes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Subject Wise - % improvement",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Gradewise - % improvement",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },

      {
        title: "Top Performing Topics",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      }, 
     
      {
        title: "Microscholarship Won",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Topicwise breakdown - Student Attempts",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Topicwise breakdown - No. of Winning Quizzes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Topicwise breakdown - Average Score",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Topicwise breakdown - % improvement",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Weak Performing Topics",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Core-Retake No of Students",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },

    ]

  },
  {
    icon: <AccountBalanceIcon sx={{color:"white"}}/>,
    title: "Student - School Attributes(R2)",
    items: [
      {
        
            title: "PrePrimary School",
            pageLink: '/studentR2Attributes',
            view: <StudentSchoolAttributes_R2 />,
          },
          {
            title: "Student Strength of the Classroom",
            pageLink: '/studentR2Attributes',
            view: <StudentSchoolAttributes_R2 />,
          },
          {
            title: "Types of Student Clubs",
            pageLink: '/studentR2Attributes',
            view: <StudentSchoolAttributes_R2 />,
          },
        {
          title: "Academic Stream",
          pageLink: '/studentR2Attributes',
          view: <StudentSchoolAttributes_R2 />,
        },
      {
        title: "First or home language is the same as that in the school",
        pageLink: '/studentR2Attributes',
            view: <StudentSchoolAttributes_R2 />,
      },
      {
        title: "Student with access to Bank Acoount/ UPI ",
        pageLink: '/studentR2Attributes',
            view: <StudentSchoolAttributes_R2 />,
      },
      {
        title: "Students engagement in extra curricular activities in school",
        pageLink: '/studentR2Attributes',
        view: <StudentSchoolAttributes_R2 />,
      },

      {
        title: "Students in leadership positions in school clubs",
        pageLink: '/studentR2Attributes',
        view: <StudentSchoolAttributes_R2 />,
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
        view: <StudentLearningBehaviour />,
        items: [
          {
            title: "Hours of individual study/practice per day",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Student learning style preferences",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Student collaborative learning style preferences",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
        {
          title: "Paid Private Tuition Hours",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        },
      {
        title: "Children who read other materials in addition to textbooks",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Paid Private Tuition Subjectwise",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      }

        ]
       
      },
      {
        title: "Student-Internet Behaviour Patterns",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        items: [
          {
            title: "Students hours spent on mobile phone - social/entertainment",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Children having access to digital devices at home",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Students using learning apps at home",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
        {
          title: "Edtech Product Type",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        },
      {
        title: "Students having social media accounts",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Types of sites",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Student hours spent on mobile phones - study",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
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
        view: <StudentLearningBehaviour />,
        items: [
          {
            title: "Teacher - Grade",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Teacher - Number of classes ratio",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Teacher - Student score range",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
        {
          title: "Teacher - Subject",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        },
      {
        title: "Average Teacher Salary",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Teacher - Pupil Ratio",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Teacher Qualification",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      }

        ]
       
      },
      {
        title: "Teacher - Training Data",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        items: [
          {
            title: "Teachers trained on CCE and Classroom based assessment",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Teacher needs of the teacher",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Nature of employment of teachers",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
        {
          title: "Time Spent by teacher on school related activities weekly",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        },
      {
        title: "Teachers satisfied with trainings by School/ Education Dept",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Teachers with platform to share best practices",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Time spent by teacher in mandatory training annually",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Teachers aware in pedagogical methodologies",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Periodicity of formative assessments in school",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Schools utilising teaching resources by the SCRET / DIETs",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },

        ]
       
      },
      {
        title: "Teacher - School - Parent Attributes",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        items: [
          {
            title: "Frequency of parent teacher meetings",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "CSRs engaged with school in last academic year",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Schools with SMCs",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
        {
          title: "Schools registered on Vidyanjali portal =",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        },
      {
        title: "Functional SMCs",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "No of Teachers in school",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
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
        view: <StudentLearningBehaviour />,
        items: [
          {
            title: "Students taking vocational courses",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Number of students likely to attend higher education",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Students who want to access vocational courses",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
        {
          title: "Students who are confident that they have knowledge of career options",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        },
      {
        title: "Student career domains",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Student career domains - STEM/Non-STEM",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Students confident in skills- communication skills, problem solving, team building",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Students accessing preparatory classes for higher education",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Students who want to access internship",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },

      

        ]
        
       
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
        view: <StudentLearningBehaviour />,
        
      
       
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
        view: <StudentLearningBehaviour />,
        items: [
          {
            title: "Immigrant background (ie one or both parents born in another state)",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Household with at least one member who completed Std XII",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Parents annual spend on child's education",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
          },
        {
          title: "Household with internet connection",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        },
      {
        title: "Mother's level of education",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Households with at least one member who knows how to operate a computer",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "House Type",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Households with other reading material (activity books, reading books, puzzles, newspaper)",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Father's level of education",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },

      {
        title: "Average income of household",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Household with electricity connection",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "My child's teacher and I communicate with each other at least once a month",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "I expect my child will graduate from high school",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "I expect my child will go to college one day",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
      },


        ]
       
      
       
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
