import * as React from "react";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import { deepPurple, grey, deepOrange, indigo } from "@mui/material/colors";

export const ColorModeContext = React.createContext({
  toggleColorMode: () => {},
  mode: "light"
});


export const ColorModeContextProvider = ({ children }) => {
  const [mode, setMode] = React.useState("light");

  const colorMode = React.useMemo(
    () => ({
      toggleColorMode: () => {
        setMode((prevMode) => (prevMode === "light" ? "dark" : "light"));
      },
      mode
    }),
    [mode]
  );

  const theme = React.useMemo(
    () =>
      createTheme({
        typography: {
          fontSize:12,
          // Name of the component
          h4: {
            fontSize:12,
            marginBottom: 20,
            borderBottom: "solid 1 px #EAEAEA",
            fontSize:"20px",
            fontWeight:"600",
            fontSize:"16px",
            lineHeight:"2",
           }, 
           h6:{
            fontSize:"16px",
            fontWeight:"600",
            
           },
           body1:{
              fontWeight: 500,
              color: "white",
            },
          
            h1: {
              color: "white",
              fontSize: 35,
              fontWeight: 600,
            },
            h2: {
               color: "white",
              fontSize: 30,
              fontWeight: 700,
              // paddingTop: 15,
              // paddingBottom: 15,
            },
            h3: {
              // color: "indigo",
              fontWeight: "500",
              fontSize: "32px",
              // paddingTop: 15,
              // paddingBottom: 15,
            },
            
            
          },
        // Button:{
        //     contained:{
        //           fontSize:"250px",
        //           borderRadius: "20px",
                  
                
        //     }
            
        //   },
        components: {
        
          MuiButtonBase: {
            styleOverrides: {
              
              root: {
                fontSize:"250px",
                borderRadius: 50,
                
              },
            },
          },
          MuiCard: {
            styleOverrides: {
            
              root: {
                
                boxShadow: 2,
                borderRadius: 5,
                
              },
            },
          },
            MuiFormLabel: {
              styleOverrides: {
                root: {
                  display: "flex",
                width: "max-content",
               
                },
                asterisk: {
                  color: "red",
                  "&$error": {
                    color: "#db3131",
                  },
                  
                },
                
              },
            },
            MuiTextField: {
              styleOverrides: {
                root: {
                  "& .MuiOutlinedInput-root": {
                    "& > fieldset": {
                      // borderColor: "indigo",
                      // borderRadius: 5,
                    },
                    "& .MuiInputBase-input.Mui-disabled": {
                      backgroundColor: "#f0f0f0",
                    },
                    // "& label": {
                    //   color: secondary[400],
                    //   opacity: 1,
                    //   fontWeight: 800,
                    // },
                    // "& MuiInputBase": {
                    //   color: secondary[400],
                    //   opacity: 1,
                    //   fontWeight: 800,
                    // },
                  },
                },
              },
            },

         
        },
        
        palette: {
          mode,

          ...(mode === "light"
            ? {
                // palette values for light mode
                primary: {
                  main:"#2169B2"
                },
                secondary:{
                  main:'#f50057'
                },
                text: {
                  primary: "#000",
                  hint:"#8a78ea",
                  secondary: '#000'
                },
                background: {
                  default: "#F5FBFC",
                  // default:"#FAFBFC"
                },
                
              }
            : {
                
                primary: {
                  main: indigo[300]
                },
                divider: indigo[700],
                background: {
                  default: "#1e1e1e",
                  paper: "#1e1e1e"
                },
                text: {
                  primary: "#fff",
                  secondary: grey[500]
                }
              })
        }
      }),
    [mode]
  );

  return (
    <ColorModeContext.Provider value={colorMode}>
      <ThemeProvider theme={theme}>{children}</ThemeProvider>
    </ColorModeContext.Provider>
  );
};

export const useColorModeContext = () => React.useContext(ColorModeContext);
