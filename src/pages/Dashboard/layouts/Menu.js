import { Route, Navigate, Routes } from "react-router-dom";
import React, {useState, lazy, Suspense } from "react";
import axios from '../../../utils/axios';

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
        title: "Average score as per subject",
        pageLink: '/studentR1Attributes/1',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Average score as per grade",
        pageLink: '/studentR1Attributes/2',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Average Score on microscholarship quiz",
        pageLink: '/studentR1Attributes/3',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Total Quiz Attempted",
        pageLink: '/studentR1Attributes/4',
        view: <StudentSchoolAttributes_R1 />,
      },
      {
        title: "Average score as per topic",
        pageLink: '/studentR1Attributes/5',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "Total Microscholarship Quizzes",
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
        title: "No of Students in core & retake quizzes",
        pageLink: '/studentR1Attributes/9',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "% Improvement in Average score - Core & Retake",
        pageLink: '/studentR1Attributes/10',
        view: <StudentSchoolAttributes_R1 />,
      },
      // {
      //   title: "Subject Wise Breakdown - % Improvement",
      //   pageLink: '/studentR1Attributes/11',
      //   view: <StudentSchoolAttributes_R1  />,
      // },
      // {
      //   title: "Grade wise- % Improvement Score",
      //   pageLink: '/studentR1Attributes/12',
      //   view: <StudentSchoolAttributes_R1  />,
      // },
      // {
      //   title: "Topic wise breakdown - %Improvement",
      //   pageLink: '/studentR1Attributes/13',
      //   view: <StudentSchoolAttributes_R1  />,
      // },
      {
        title: "Student Attempts as per topic",
        pageLink: '/studentR1Attributes/11',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "% Improvement as per topic - Top Performing",
        pageLink: '/studentR1Attributes/12',
        view: <StudentSchoolAttributes_R1  />,
      },
      {
        title: "% Improvement as per topic - Weak Performing",
        pageLink: '/studentR1Attributes/13',
        view: <StudentSchoolAttributes_R1  />,
      },
    ],


  },
  {
    icon: <AccountBalanceIcon sx={{color:"white"}}/>,
    title: "Student School Attributes(R2)",
    items: [
      {
        
            title: "Pre-Primary school type",
            pageLink: '/studentR2Attributes/1',
            view: <StudentSchoolAttributes_R2 />,
          },
          {
            title: "Student strength in classroom",
            pageLink: '/studentR2Attributes/2',
            view: <StudentSchoolAttributes_R2 />,
          },
          {
            title: "Types of student's clubs",
            pageLink: '/studentR2Attributes/3',
            view: <StudentSchoolAttributes_R2 />,
          },
        {
          title: "Academic stream",
          pageLink: '/studentR2Attributes/4',
          view: <StudentSchoolAttributes_R2 />,
        },
      {
        title: "Student with access to bank account/ UPI",
        pageLink: '/studentR2Attributes/5',
            view: <StudentSchoolAttributes_R2 />,
      },
      {
        title: "Students engagement in extracurricular activities in school",
        pageLink: '/studentR2Attributes/6',
            view: <StudentSchoolAttributes_R2 />,
      },
      {
        title: "Children with same first/home language at home and school",
        pageLink: '/studentR2Attributes/7',
        view: <StudentSchoolAttributes_R2 />,
      },

      {
        title: "Social Group",
        pageLink: '/studentR2Attributes/8',
        view: <StudentSchoolAttributes_R2 />,
      }, 
      {
        title: "Students in leadership positions in school clubs",
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
        title: "Student-R3-Learning Behaviours",
        pageLink: '/studentLearningBehaviours',
        view: <StudentLearningBehaviour />,
        items: [
          {
            title: "Hours of individual study per day (does not include tuition hours)",
        pageLink: '/studentLearningBehaviours/1',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Student learning style",
        pageLink: '/studentLearningBehaviours/2',
        view: <StudentLearningBehaviour />,
          },
          {
            title: "Student preferences on collaborative learning style",
        pageLink: '/studentLearningBehaviours/3',
        view: <StudentLearningBehaviour />,
          },
        {
          title: "Hours of paid private tuition",
        pageLink: '/studentLearningBehaviours/4',
        view: <StudentLearningBehaviour />,
        },
      {
        title: "Students reading extra materials other than textbooks",
        pageLink: '/studentLearningBehaviours/5',
        view: <StudentLearningBehaviour />,
      },
      {
        title: "Subjects studied in paid private tuition",
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
            title: "Hours spent on mobile phones for social/entertainment",
            pageLink: '/studentInternetBehaviours/1',
            view: <StudentInternetBehaviourPatterns />,
          },
          {
            title: "Students with access to digital devices at home",
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
        title: "Students with one or more social media accounts",
        pageLink: '/studentInternetBehaviours/5',
        view: <StudentInternetBehaviourPatterns />,
      },
      {
        title: "Types of sites",
        pageLink: '/studentInternetBehaviours/6',
        view: <StudentInternetBehaviourPatterns />,
      },
      {
        title: "Hours spent on mobile phones for study",
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
            title: "Grades taught by teacher",
            pageLink: '/teacher_Attributes/1',
            view: <Teacher_Attributes />,
          },
          {
            title: "No. of classes taught by teacher",
            pageLink: '/teacher_Attributes/2',
            view: <Teacher_Attributes />,
          },
          {
            title: "Teacher - Student score range",
            pageLink: '/teacher_Attributes/3',
            view: <Teacher_Attributes />,
          },
        {
          title: "No. of subjects taught by teacher",
          pageLink: '/teacher_Attributes/4',
          view: <Teacher_Attributes />,
        },
      {
        title: "Average teacher salary",
        pageLink: '/teacher_Attributes/5',
        view: <Teacher_Attributes />,
      },
      {
        title: "Teacher - pupil ratio",
        pageLink: '/teacher_Attributes/6',
        view: <Teacher_Attributes />,
      },
      {
        title: "Qualification of teacher",
        pageLink: '/teacher_Attributes/7',
        view: <Teacher_Attributes />,
      },
      {
        title: "No. of teachers in school",
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
            title: "% of teachers trained on CCE and Classroom based assessment",
            pageLink: '/teacherTrainingData/1',
            view: <TeacherTrainingData />,
          },
          {
            title: "% of teachers satisfied with the trainings held during the academic year by School/ Education Dept",
            pageLink: '/teacherTrainingData/2',
            view: <TeacherTrainingData />,
          },
          {
            title: "Type of teacher employment in school",
            pageLink: '/teacherTrainingData/3',
            view: <TeacherTrainingData />,
          },
          {
            title: "Time spent by teacher in a week on school related activities",
            pageLink: '/teacherTrainingData/4',
            view: <TeacherTrainingData />,
          },
          {
            title: "No. of teachers with access to platform in school to share their best practices and challenges",
            pageLink: '/teacherTrainingData/5',
            view: <TeacherTrainingData />,
          },
          {
            title: "Time spent by teacher in mandatory training annually",
            pageLink: '/teacherTrainingData/6',
            view: <TeacherTrainingData />,
          },
          {
            title: "% of teachers aware about pedagogical methodologies",
            pageLink: '/teacherTrainingData/7',
            view: <TeacherTrainingData />,
          },
          {
            title: "Training needs of the teacher",
            pageLink: '/teacherTrainingData/8',
            view: <TeacherTrainingData />,
          },
         
      {
        title: "Periodicity of formative assessments in school",
        pageLink: '/teacherTrainingData/9',
        view: <TeacherTrainingData />,
      },
      {
        title: "Schools with access to and are utilising SCRET / DIETs teaching resources",
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
            title: "Frequency of parent teacher meetings for each grade",
            pageLink: '/teacherSchoolParent/1',
            view: <TeacherSchoolParent />,
          },
          {
            title: "Number of CSRs engaged with school in last academic year",
            pageLink: '/teacherSchoolParent/2',
            view: <TeacherSchoolParent />,
          },
          {
            title: "Schools with SMCs",
            pageLink: '/teacherSchoolParent/3',
            view: <TeacherSchoolParent />,
          },
          {
            title: "Schools with functional SMCs",
            pageLink: '/teacherSchoolParent/4',
            view: <TeacherSchoolParent />,
          },
        {
          title: "School registered on Vidyanjali portal",
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
            title: "No. of students who want internship",
            pageLink: '/studentCareerGrowth/2',
            view: <StudentCareerGrowth_R4 />,
          },
          {
            title: "Students likely to attend higher education",
            pageLink: '/studentCareerGrowth/3',
            view: <StudentCareerGrowth_R4 />,
          },
        {
          title: "Students confidence on knowledge of career options",
          pageLink: '/studentCareerGrowth/4',
          view: <StudentCareerGrowth_R4 />,
        },
      {
        title: "Student career domains",
        pageLink: '/studentCareerGrowth/5',
        view: <StudentCareerGrowth_R4 />,
      },
      {
        title: "Students confident in the following skills- communication skills, problem solving, team building",
        pageLink: '/studentCareerGrowth/6',
        view: <StudentCareerGrowth_R4 />,
      },
      {
        title: "No. of students accessing preparatory classes for higher education (college)",
        pageLink: '/studentCareerGrowth/7',
        view: <StudentCareerGrowth_R4 />,
      },
      {
        title: "No. of students who want access to vocational courses",
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
    title: "Parental and Household Engagement",
    items: [
      {
        title: "Parental - Student - Household Attributes",
        pageLink: '/parentalAndHousehold',
        view: <ParentalandHousehold />,
        items: [
          {
            title: "Immigrant background (i.e one or both parents born in another state) ",
            pageLink: '/parentalAndHousehold/1',
            view: <ParentalandHousehold />,
          },
          {
            title: "Household with at least one member who completed Std XII",
            pageLink: '/parentalAndHousehold/2',
            view: <ParentalandHousehold />,
          },
          {
            title: "Parents monthly spend on child's education",
            pageLink: '/parentalAndHousehold/3',
            view: <ParentalandHousehold />,
          },
        {
          title: "Household with internet connection",
          pageLink: '/parentalAndHousehold/4',
          view: <ParentalandHousehold />,
        },
      {
        title: "Mother's level of Education",
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
        title: "% households with access to other reading material (activity books, reading books, puzzles, newspaper)",
        pageLink: '/parentalAndHousehold/9',
        view: <ParentalandHousehold />,
      },

      {
        title: "Father's level of education",
        pageLink: '/parentalAndHousehold/10',
        view: <ParentalandHousehold />,
      },
      {
        title: "Household with electricity connection  ",
        pageLink: '/parentalAndHousehold/11',
        view: <ParentalandHousehold />,
      },
      {
        title: "No. of adult/parents who read with child everyday or nearly everyday",
        pageLink: '/parentalAndHousehold/12',
        view: <ParentalandHousehold />,
      },
      {
        title: "No. of parents who communicate wth teacher at least once a month (in person or by notes, text, email, phone, etc.)",
        pageLink: '/parentalAndHousehold/13',
        view: <ParentalandHousehold />,
      },
      {
        title: "No. of parents expecting their children to graduate from high school",
        pageLink: '/parentalAndHousehold/14',
        view: <ParentalandHousehold />,
      },
      {
        title: "No. of parents expecting their children to graduate from college",
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
            title: "Schools with lack of parental support in students learning",
            pageLink: '/parentSchoolAttribute/2',
        view: <ParentSchoolAttribute />,
          },
          {
            title: "Schools provide guidance on parental support for child's learning",
            pageLink: '/parentSchoolAttribute/3',
        view: <ParentSchoolAttribute />,
          },
        {
          title: "% of schools informing parents on child's learning levels",
          pageLink: '/parentSchoolAttribute/4',
        view: <ParentSchoolAttribute />,
        },
      {
        title: "% of schools informing parents on school activity",
        pageLink: '/parentSchoolAttribute/5',
        view: <ParentSchoolAttribute />,
      },
      {
        title: "No. of parents attending parent-teacher conferences",
        pageLink: '/parentSchoolAttribute/6',
        view: <ParentSchoolAttribute />,
      },
      {
        title: "No. of parents participating in school events regularly",
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
  // {
  //   icon: <PersonIcon sx={{color:"white"}}/>,
  //   title: "User Management",
  //   pageLink: '/userManagement',
  //   view: <UserManagement />,
    
    

  // },
  
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
  const selectedRoleId = localStorage.getItem('roleId');
  const [data, setData] = useState([]);

  const fetchRoleBasedDashboards = async () => {
    let payload = {
      "roleId": selectedRoleId,
    };
    try {
      const res = await axios.post("/user/role-access-fetch", payload);
      if (res.data.status === true && res.data.statusCode === 200) {
        const fetchedData = res.data.result.map(item => ({
          access: item.dashboardName,
          grantAccess: item.grantAccess === 1
        }));
        setData(fetchedData);
        
      } else {
        console.log(res.data.message);
      }
    } catch (error) {
      console.log(error);
      
    }
  };
  // return (
  //   // <Suspense fallback={<div>Loading...</div>}>
  //   //   <Routes>
  //   //     {generateRoutes(menu)}
  //   //     <Route path="*" element={<Navigate to="/dashboard" />} />
  //   //   </Routes>
  //   // </Suspense>
  // );
};
