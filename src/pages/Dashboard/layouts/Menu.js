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
import StudentInternetBehaviourPatterns from "../DashboardPages/StudentSchoolAttributes_R3/StudentInternetBehaviourPatterns";
import Home from "../DashboardPages/Home";
import StudentSchoolAttributes_R1 from "../DashboardPages/StudentSchoolAttributes_R1";
import Teacher_Attributes from "../DashboardPages/TeacherAttributes/Teacher_Attributes";
import TeacherTrainingData from "../DashboardPages/TeacherAttributes/TeacherTrainingData";
import TeacherSchoolParent from "../DashboardPages/TeacherAttributes/TeacherSchoolParent";
import StudentCareerGrowth_R4 from "../DashboardPages/StudentCareerGrowth_R4/StudentCareerGrowth_R4";
import SchoolInfrastructure from "../DashboardPages/SchoolInfrastructure";
import ParentalandHousehold from "../DashboardPages/Parental&Household/Parental&Household";
import ParentSchoolAttribute from "../DashboardPages/Parental&Household/ParentSchoolAttribute";
import UserManagement from "../DashboardPages/UserManagement";






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
        title: "Subject Wise Breakdown - Average Score",
        pageLink: '/studentR1Attributes/1',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Gradewise - Average Score",
        pageLink: '/studentR1Attributes/2',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Microscholarship Quizzes - Average Score",
        pageLink: '/studentR1Attributes/3',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Total Quiz Attempted",
        pageLink: '/studentR1Attributes/4',
        view: <StudentSchoolAttributes_R1 />,
      },
      {
        title: "Topic wise breakdown - Average Score",
        pageLink: '/studentR1Attributes/5',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Topic wise breakdown - No. of Microscholarship Quizzes",
        pageLink: '/studentR1Attributes/6',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Top Performing Topics",
        pageLink: '/studentR1Attributes/7',
        view: <StudentSchoolAttributes_R1 />,
      },
      {
        title: "Weak Performing Topics",
        pageLink: '/studentR1Attributes/8',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Core-Retake- ( No of Students)",
        pageLink: '/studentR1Attributes/9',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Core-Retake- (Average Score)",
        pageLink: '/studentR1Attributes/10',
        view: <StudentSchoolAttributes_R1 />,
      },
      {
        title: "Subject Wise Breakdown - % Improvement",
        pageLink: '/studentR1Attributes/11',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Grade wise- % Improvement Score",
        pageLink: '/studentR1Attributes/12',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Topic wise breakdown - %Improvement",
        pageLink: '/studentR1Attributes/13',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Topic wise breakdown - Student Attempts",
        pageLink: '/studentR1Attributes/14',
        view: <StudentSchoolAttributes_R1  />,
      },
    ],


  },
  {
    icon: <AccountBalanceIcon sx={{color:"white"}}/>,
    title: "Student - School Attributes(R2)",
    items: [
      {
        
            title: "Pre-Primary School",
            pageLink: '/studentR2Attributes/1',
            view: <StudentSchoolAttributes_R2 />,
          },
          {
            title: "Student Strength of the Classroom",
            pageLink: '/studentR2Attributes/2',
            view: <StudentSchoolAttributes_R2 />,
          },
          {
            title: "Types of Student Clubs",
            pageLink: '/studentR2Attributes/3',
            view: <StudentSchoolAttributes_R2 />,
          },
        {
          title: "Academic Stream",
          pageLink: '/studentR2Attributes/4',
          view: <StudentSchoolAttributes_R2 />,
        },
      {
        title: "Student with access to Bank Account/ UPI",
        pageLink: '/studentR2Attributes/5',
            view: <StudentSchoolAttributes_R2 />,
      },
      {
        title: "Students engagement in extra curricular activities in school",
        pageLink: '/studentR2Attributes/6',
            view: <StudentSchoolAttributes_R2 />,
      },
      {
        title: "Children whose first or home language is the same as that in the school ",
        pageLink: '/studentR2Attributes/7',
        view: <StudentSchoolAttributes_R2 />,
      },

      {
        title: "Social Groups",
        pageLink: '/studentR2Attributes/8',
        view: <StudentSchoolAttributes_R2 />,
      }, 
      {
        title: "No. of Students in leadership positions in school clubs in the school ",
        pageLink: '/studentR2Attributes/9',
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
        pageLink: '/studentLearningBehaviours/1',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Student learning style preferences",
        pageLink: '/studentLearningBehaviours/2',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Student preferences collaborative learning style preferences",
        pageLink: '/studentLearningBehaviours/3',
        view: <StudentLearningBehaviour />,
          },
        {
          title: "Paid Private Tuition Hours",
        pageLink: '/studentLearningBehaviours/4',
        view: <StudentLearningBehaviour />,
        },
      {
        title: "Children who read other materials in addition to textbooks",
        pageLink: '/studentLearningBehaviours/5',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Paid Private Tuition - Subject studied",
        pageLink: '/studentLearningBehaviours/6',
        view: <StudentLearningBehaviour />,
      }

        ]
       
      },
      {
        title: "Student-Internet Behaviour Patterns",
        pageLink: '/studentInternetBehaviours',
        view: <StudentInternetBehaviourPatterns />,
        items: [
          {
            title: "Student hours spent on mobile phones - social/entertainment",
            pageLink: '/studentInternetBehaviours/1',
            view: <StudentInternetBehaviourPatterns />,
          },
          {
            title: "Children having access to digital devices at home",
            pageLink: '/studentInternetBehaviours/2',
            view: <StudentInternetBehaviourPatterns />,
          },
          {
            title: "Students using learning apps at home",
            pageLink: '/studentInternetBehaviours/3',
            view: <StudentInternetBehaviourPatterns />,
          },
        {
          title: "Edtech Product Type",
          pageLink: '/studentInternetBehaviours/4',
          view: <StudentInternetBehaviourPatterns />,
        },
      {
        title: "Students who have one or more social media accounts",
        pageLink: '/studentInternetBehaviours/5',
        view: <StudentInternetBehaviourPatterns />,
      },
      {
        title: "Types of sites",
        pageLink: '/studentInternetBehaviours/6',
        view: <StudentInternetBehaviourPatterns />,
      },
      {
        title: "Student hours spent on mobile phones - study",
        pageLink: '/studentInternetBehaviours/7',
        view: <StudentInternetBehaviourPatterns />,
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
        pageLink: '/teacher_Attributes',
        view: <Teacher_Attributes />,
        items: [
          {
            title: "Teacher - Grade",
            pageLink: '/teacher_Attributes/1',
            view: <Teacher_Attributes />,
          },
          {
            title: "Teacher - Number of classes ratio",
            pageLink: '/teacher_Attributes/2',
            view: <Teacher_Attributes />,
          },
          {
            title: "Teacher - Student score range",
            pageLink: '/teacher_Attributes/3',
            view: <Teacher_Attributes />,
          },
        {
          title: "Teacher - Subject",
          pageLink: '/teacher_Attributes/4',
          view: <Teacher_Attributes />,
        },
      {
        title: "Average Teacher Salary",
        pageLink: '/teacher_Attributes/5',
        view: <Teacher_Attributes />,
      },
      {
        title: "Teacher - Pupil Ratio/6",
        pageLink: '/teacher_Attributes',
        view: <Teacher_Attributes />,
      },
      {
        title: "Qualification of Teacher",
        pageLink: '/teacher_Attributes/7',
        view: <Teacher_Attributes />,
      },
      {
        title: "No. of Teachers in School",
        pageLink: '/teacher_Attributes/8',
        view: <Teacher_Attributes />,
      }

        ]
       
      },
      {
        title: "Teacher - Training Data",
        pageLink: '/teacherTrainingData',
        view: <TeacherTrainingData />,
        items: [
          {
            title: "Teachers trained on CCE and Classroom based assessment",
            pageLink: '/teacherTrainingData/1',
            view: <TeacherTrainingData />,
          },
          {
            title: "Teachers satisfied with trainings by School/ Education Dept",
            pageLink: '/teacherTrainingData/2',
            view: <TeacherTrainingData />,
          },
          {
            title: "Nature of employment of teachers",
            pageLink: '/teacherTrainingData/3',
            view: <TeacherTrainingData />,
          },
          {
            title: "Time Spent by teacher on school related activities weekly",
            pageLink: '/teacherTrainingData/4',
            view: <TeacherTrainingData />,
          },
          {
            title: "Teachers with platform to share best practices",
            pageLink: '/teacherTrainingData/5',
            view: <TeacherTrainingData />,
          },
          {
            title: "Time spent by teacher in mandatory training annually",
            pageLink: '/teacherTrainingData/6',
            view: <TeacherTrainingData />,
          },
          {
            title: "Teachers aware in pedagogical methodologies",
            pageLink: '/teacherTrainingData/7',
            view: <TeacherTrainingData />,
          },
          {
            title: "Teacher needs of the teacher",
            pageLink: '/teacherTrainingData/8',
            view: <TeacherTrainingData />,
          },
         
      {
        title: "Periodicity of formative assessments in school",
        pageLink: '/teacherTrainingData/9',
        view: <TeacherTrainingData />,
      },
      {
        title: "Schools utilising teaching resources by the SCRET / DIETs",
        pageLink: '/teacherTrainingData/10',
        view: <TeacherTrainingData />,
      },

        ]
       
      },
      {
        title: "Teacher - School - Parent Attributes",
        pageLink: '/teacherSchoolParent',
        view: <TeacherSchoolParent />,
        items: [
          {
            title: "Frequency of parent teacher meetings",
            pageLink: '/teacherSchoolParent/1',
            view: <TeacherSchoolParent />,
          },
          {
            title: "CSRs engaged with school in last academic year",
            pageLink: '/teacherSchoolParent/2',
            view: <TeacherSchoolParent />,
          },
          {
            title: "Schools with SMCs",
            pageLink: '/teacherSchoolParent/3',
            view: <TeacherSchoolParent />,
          },
          {
            title: "Functional SMCs",
            pageLink: '/teacherSchoolParent/4',
            view: <TeacherSchoolParent />,
          },
        {
          title: "Schools registered on Vidyanjali portal =",
          pageLink: '/teacherSchoolParent/5',
          view: <TeacherSchoolParent />,
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
        pageLink: '/studentCareerGrowth',
        view: <StudentCareerGrowth_R4 />,
        items: [
          {
            title: "Students taking vocational courses",
            pageLink: '/studentCareerGrowth/1',
            view: <StudentCareerGrowth_R4 />,
          },
          {
            title: "Students who want to access internship",
            pageLink: '/studentCareerGrowth/2',
            view: <StudentCareerGrowth_R4 />,
          },
          {
            title: "Students likely to attend higher education",
            pageLink: '/studentCareerGrowth/3',
            view: <StudentCareerGrowth_R4 />,
          },
        {
          title: "Number of students who are confident that they have knowledge of career options",
          pageLink: '/studentCareerGrowth/4',
          view: <StudentCareerGrowth_R4 />,
        },
      {
        title: "Student career domains",
        pageLink: '/studentCareerGrowth/5',
        view: <StudentCareerGrowth_R4 />,
      },
      {
        title: "Students confident in the communication skills, problem solving, team building",
        pageLink: '/studentCareerGrowth/6',
        view: <StudentCareerGrowth_R4 />,
      },
      {
        title: "Number of students accessing preparatory classes for higher education (college)",
        pageLink: '/studentCareerGrowth/7',
        view: <StudentCareerGrowth_R4 />,
      },
      {
        title: "Students who want to access vocational courses",
        pageLink: '/studentCareerGrowth/8',
        view: <StudentCareerGrowth_R4 />,
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
        pageLink: '/schoolInfrastructure',
        view: <SchoolInfrastructure />,
        
      
       
      },
  

    ]

  },
  {
    icon: <PeopleAltIcon sx={{color:"white"}}/>,
    title: "Parental & Household Engagement",
    items: [
      {
        title: "Parental - Student - Household Attributes",
        pageLink: '/parentalAndHousehold',
        view: <ParentalandHousehold />,
        items: [
          {
            title: "Immigrant background (i.e one or both parents born in another state)",
            pageLink: '/parentalAndHousehold/1',
            view: <ParentalandHousehold />,
          },
          {
            title: "Household with at least one member who completed Std XII",
            pageLink: '/parentalAndHousehold/2',
            view: <ParentalandHousehold />,
          },
          {
            title: "Parents monthly spend on childs education",
            pageLink: '/parentalAndHousehold/3',
            view: <ParentalandHousehold />,
          },
        {
          title: "Household has internet connection",
          pageLink: '/parentalAndHousehold/4',
          view: <ParentalandHousehold />,
        },
      {
        title: "Mothers level of Education",
        pageLink: '/parentalAndHousehold/5',
        view: <ParentalandHousehold />,
      },
      {
        title: "Average income of household",
        pageLink: '/parentalAndHousehold/6',
        view: <ParentalandHousehold />,
      },
      {
        title: "Households with at least one member who knows how to operate a computer",
        pageLink: '/parentalAndHousehold/7',
        view: <ParentalandHousehold />,
      },
      {
        title: "House type",
        pageLink: '/parentalAndHousehold/8',
        view: <ParentalandHousehold />,
      },
      {
        title: "% Households which have other reading material (activity books, reading books, puzzles, newspaper)",
        pageLink: '/parentalAndHousehold/9',
        view: <ParentalandHousehold />,
      },

      {
        title: "Father's level of education",
        pageLink: '/parentalAndHousehold/10',
        view: <ParentalandHousehold />,
      },
      {
        title: "Household has electricity connection ",
        pageLink: '/parentalAndHousehold/11',
        view: <ParentalandHousehold />,
      },
      {
        title: "I (or another adult in the home) read(s) with my child every day or nearly every day",
        pageLink: '/parentalAndHousehold/12',
        view: <ParentalandHousehold />,
      },
      {
        title: "My child’s teacher and I communicate with each other at least once a month (in person or by notes, text, email, phone, etc.)",
        pageLink: '/parentalAndHousehold/13',
        view: <ParentalandHousehold />,
      },
      {
        title: "I expect my child will graduate from high school",
        pageLink: '/parentalAndHousehold/14',
        view: <ParentalandHousehold />,
      },
      {
        title: "I expect my child will go to college one day",
        pageLink: '/parentalAndHousehold/15',
        view: <ParentalandHousehold />,
      },


        ]
       
      
       
      },

      {
        title: "Parental - School - Attributes",
        pageLink: '/parentSchoolAttribute',
        view: <ParentSchoolAttribute />,
        items: [
          {
            title: "Frequency of parent teacher meetings",
            pageLink: '/parentSchoolAttribute/1',
        view: <ParentSchoolAttribute />,
          },
          {
            title: "Schools having lack of parental support in students learning",
            pageLink: '/parentSchoolAttribute/2',
        view: <ParentSchoolAttribute />,
          },
          {
            title: "Schools provide guidance on how parents can support children in learning",
            pageLink: '/parentSchoolAttribute/3',
        view: <ParentSchoolAttribute />,
          },
        {
          title: "% of schools, where parents have been made aware of learning levels through PTM, Letters, discussion forums",
          pageLink: '/parentSchoolAttribute/4',
        view: <ParentSchoolAttribute />,
        },
      {
        title: "Schools informed parents about school activity",
        pageLink: '/parentSchoolAttribute/5',
        view: <ParentSchoolAttribute />,
      },
      {
        title: "I attend parent-teacher conferences",
        pageLink: '/parentSchoolAttribute/6',
        view: <ParentSchoolAttribute />,
      },
      {
        title: "I regularly participate in events at my childs school",
        pageLink: '/parentSchoolAttribute/7',
        view: <ParentSchoolAttribute />,
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
    pageLink: '/userManagement',
    view: <UserManagement />,
    
    

  },
  
];

const generateRoutes = () => {
  const routes = [];
  
  menu.forEach(item => {
    if (item.items) {
      item.items.forEach(subItem => {
        routes.push(
          <Route
            key={subItem.pageLink}
            path={subItem.pageLink}
            element={subItem.view}
          />
        );
        if (subItem.items) {
          subItem.items.forEach(nestedItem => {
            routes.push(
              <Route
                key={nestedItem.pageLink}
                path={nestedItem.pageLink}
                element={nestedItem.view}
              />
            );
          });
        }
      });
    } else {
      routes.push(
        <Route
          key={item.pageLink}
          path={item.pageLink}
          element={item.view}
        />
      );
    }
  });

  return routes;
};

export const AppRoutes = () => {
  // return (
  //   // <Suspense fallback={<div>Loading...</div>}>
  //   //   <Routes>
  //   //     {generateRoutes(menu)}
  //   //     <Route path="*" element={<Navigate to="/dashboard" />} />
  //   //   </Routes>
  //   // </Suspense>
  // );
};
