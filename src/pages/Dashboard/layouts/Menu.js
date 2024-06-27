import { Route, Navigate, Routes } from "react-router-dom";
import React, { lazy, Suspense } from "react";

import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import DashboardSharpIcon from "@mui/icons-material/DashboardSharp";
import NoteAddSharpIcon from "@mui/icons-material/NoteAddSharp";
import FactCheckSharpIcon from "@mui/icons-material/FactCheckSharp";
import SummarizeSharpIcon from "@mui/icons-material/SummarizeSharp";
import MailSharpIcon from "@mui/icons-material/MailSharp";
import OutboxIcon from "@mui/icons-material/Outbox";
// import Sentbox from '../pages/sentbox/Sentbox';
// const ViewCR = lazy(() => import("../dashboardPages/ViewCR/ViewCR"));

const Home = lazy(() => import("../DashboardPages/Home"));
const StudentSchoolAttributes_R3 = lazy(() => import("../DashboardPages/StudentSchoolAttributes_R3"));
// const Login = lazy(() => import("../dashboardPages/auth/Login"));
// const CreateRequest = lazy(() => import("../dashboardPages/CreateRequest"));
// const Approval = lazy(() => import("../dashboardPages/approval/Approval"));
// const Inbox = lazy(() => import("../dashboardPages/inbox/Inbox"));
// const Sentbox = lazy(() => import("../dashboardPages/sentbox/Sentbox"));
// const MyDepartmentReport = lazy(() => import("../dashboardPages/cmsreports/MyDepartmentReport"))
// const CMSStatisticsReport = lazy(() => import("../dashboardPages/cmsreports/CMSStatisticsReport"))
// const CMSWorkStatisticsReport = lazy(() => import("../dashboardPages/cmsreports/CMSWorkStatisticsReport"))
// const MyCMSReport = lazy(() => import("../dashboardPages/cmsreports/MyCMSReport"))
// const CMSSectionWiseReport = lazy(() => import("../dashboardPages/cmsreports/CMSSectionWiseReport"))

export const menu = [
  {
    icon: <DashboardSharpIcon sx={{color:"white"}}/>,
    title: "Dashboard",
    pageLink: "/dashboard",
    view: <Home />,
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
  // {
  //   icon: <AppRegistrationIcon />,
  //   title: "Manage Request",
  //   pageLink: '/approval',
  //   view: <Approval/>,
  //   items: [
  //       // {
  //       //   title: "Create Employee",
  //       //   pageLink: "/createemployee",
  //       //   // view: <RegisteredPatient/>,
  //       // },
  //       // {
  //       //   title: "Medical Reimbursement Claim Tracker",
  //       //   // pageLink: "/demo",
  //       //   pageLink: "/medicalclaimtracker",
  //       //   // view: <RegisteredPatientView/>,
  //       // },
  //       // {
  //       //   title: "Initiate Health Card/View Application",
  //       //   pageLink: "/healthcardapplication",
  //       //   // view: <ReferredPatientView/>,
  //       // },
  //       // {
  //       //   title: "Initiate New/Rejected Beneficiaries",
  //       //   pageLink: "/initiatenewrejectedbeneficiaries",
  //       //   // view: <TelephonicRegView/>,
  //       // },
  //       // {
  //       //   title: "Download Health Card",
  //       //   pageLink: "/downloadhealthcard",
  //       //   // view: <TelephonicRegView/>,
  //       // }
  //     ]
  // },
  // {
  //   icon: <LocalLibraryOutlinedIcon />,
  //   title: "Patients",
  //   items: [
  //       {
  //         title: "Telephonic Registration",
  //         pageLink: "/telephonicRegistration",
  //         view: <TelephonicRegistration/>,
  //       },
  //       {
  //         title: "Registered Patients View",
  //         pageLink: "/telephonicRegEdit",
  //         view: <TelephonicRegEdit/>,
  //       },
  //       {
  //         title: "Telephonic Registered Patients",
  //         pageLink: "/telephonicRegView",
  //         view: <TelephonicRegView/>,
  //       },
  //     ]
  // },
  // {
  //   icon: <TrendingUpOutlinedIcon />,
  //   title: "Reports",
  //   pageLink: "/demo",
  //   view: <Demo/>,
  // },
  // {
  //   icon: <DescriptionOutlinedIcon />,
  //   title: "Settings"
  // }
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
