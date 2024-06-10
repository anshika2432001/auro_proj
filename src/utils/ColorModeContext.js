import * as React from "react";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import { deepPurple, grey, deepOrange, indigo } from "@mui/material/colors";
import { GlobalStyles } from "@mui/material";
import "@fontsource/inter";

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
            fontSize:20,
          fontFamily:"Inter",
            fontWeight:"bold",
            
           }, 
           h5: {
            fontSize:"14px",
            
            fontFamily:"Inter",
            color:"black",
            fontWeight:"bold",
            
           }, 
           h6:{
            fontSize:"16px",
            fontWeight:"600",
            fontFamily:"Inter",
          
            
           },
           body1:{
              fontWeight: 500,
              fontSize: 14,
            fontFamily:"Inter",
       
              
            },
            body2:{
              fontWeight: 500,
              fontSize: 14,
            fontFamily:"Inter",
            paddingTop:10,
            paddingBottom:10
              
            },
          
            h1: {
           
              fontSize: 40,
              fontWeight: 700,
              paddingTop: 20,
              paddingBottom: 20,
              color:"white",
              fontFamily:"Inter"
            },
            h2: {
              
              fontSize: 30,
              fontWeight: "bold",
               paddingTop: 20,
               paddingBottom: 20,
               fontFamily:"Inter"
            },
            h3: {
              // color: "indigo",
              fontWeight: 700,
              fontSize: 25,
              fontFamily:"Inter"
              // paddingTop: 15,
              // paddingBottom: 15,
            },
            
            
          },
        img: {
          background:'none'
        },
        components: {
        
          MuiButton: {
            styleOverrides: {
              
              root: {
                fontSize:"13px",
                borderRadius: 5,
                margin:"10px"
            
                
              },
              contained: {
                
                fontWeight:"bold"
              },
              outlined: {
                border: 'none',
                fontWeight:"bold"
              }
            },
          },
          MuiSvgIcon: {
            styleOverrides: {
            
              root: {
                padding:"3px",
                fontSize:"30px"
                
              },
            },

          },
          
          MuiCard: {
            styleOverrides: {
            
              root: {
                padding:"30px",
                border:"none",

                '&.mini-card':{
                  margin:3,
                  padding:"20px",
                  borderRadius:"5px"
                }
                
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
      <ThemeProvider theme={theme}>
       
        {children}
        </ThemeProvider>
    </ColorModeContext.Provider>
  );
};

export const useColorModeContext = () => React.useContext(ColorModeContext);
