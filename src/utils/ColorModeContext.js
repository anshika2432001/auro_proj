import * as React from "react";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import { deepPurple, grey, deepOrange, indigo } from "@mui/material/colors";
import { GlobalStyles } from "@mui/material";
import "@fontsource/inter";

export const ColorModeContext = React.createContext({
  toggleColorMode: () => {},
  mode: "light",
});

export const ColorModeContextProvider = ({ children }) => {
  const [mode, setMode] = React.useState("light");

  const colorMode = React.useMemo(
    () => ({
      toggleColorMode: () => {
        setMode((prevMode) => (prevMode === "light" ? "dark" : "light"));
      },
      mode,
    }),
    [mode]
  );

  const theme = React.useMemo(
    () =>
      createTheme({
        typography: {
          h1: {
            fontSize: "40px",
            fontWeight: 800,
            paddingTop: 10,
            paddingBottom: 10,
            color: "white",
            fontFamily: "Inter",
          },
          h2: {
            fontSize: "32px",
            fontWeight: 700,
            paddingTop: 10,
            paddingBottom: 10,
            fontFamily: "Inter",
          },
          h3: {
            // color: "indigo",
            fontWeight: "bold",
            fontSize: "24px",
            fontFamily: "Inter",
            // paddingTop: 15,
            // paddingBottom: 15,
          },
          h4: {
            fontSize: "22px",
            fontFamily: "Inter",
            fontWeight: "bold",
          },
          h5: {
            fontSize: "18px",

            fontFamily: "Inter",
            color: "black",
            fontWeight: "bold",
          },
          h6: {
            fontSize: "16px",
            fontWeight: "bold",
            fontFamily: "Inter",
          },
          body1: {
            fontSize: "14px",
            fontFamily: "Inter",
          },
          body2: {
            fontWeight: 500,
            fontSize: "16px",
            fontFamily: "Inter",
            paddingTop: 10,
            paddingBottom: 10,
          },

         
        },
        img: {
          background: "none",
        },
        components: {
          MuiButton: {
            styleOverrides: {
              root: {
                fontSize: "13px",
                borderRadius: 5,
                marginTop: "10px",
                marginBottom: "10px",
              },
              contained: {
               
                padding: "4px 20px 4px 20px",
                
                "&.MuiButton-containedPrimary": {
                  fontWeight: "700",
                  lineHeight: "27.28px",
                  fontSize: "16px",
                  fontFamily: "Inter",
                  textTransform: "none",
                },
              },
              outlined: {
                border: "none",
               
                "&.MuiButton-outlinedPrimary": {
                  fontWeight: "700",
                  lineHeight: "27.28px",
                  fontSize: "16px",
                  fontFamily: "Inter",
                  textTransform: "none",
                },
              },
            },
          },
          MuiSvgIcon: {
            styleOverrides: {
              root: {
                padding: "3px",
                fontSize: "30px",
              },
            },
          },
          MuiTextField: {
            styleOverrides: {
              root: {
                "& label": {
                  opacity: 1,
                },
                
                "& .MuiInputBase-input": {
                  padding: "15px", 
                },
                "& .MuiOutlinedInput-root": {
                  borderRadius: "12px",
                  padding: "auto 0px",
                },
              },
            },
          },

          MuiCard: {
            styleOverrides: {
              root: {
                padding: "30px",
                border: "none",

                "&.mini-card": {
                  marginRight: "auto",
                  marginLeft: "auto",
                  marginTop: "10px",
                  padding: 3,
                  borderRadius: "5px",
                },
                "&.mini-card1": {
                  maxWidth: 400,
                  marginRight: "auto",
                  marginLeft: "auto",
                  marginTop: "20px",
                  marginBottom: "20px",
                  padding: 3,
                  borderRadius: "5px",
                },
                "&.dashboard-card": {
                  width: '100%',
                  margin: '2px',
                  paddingTop:"30px",
                  paddingBottom:"0px",
                  paddingLeft:"0px",
                  paddingRight:"0px",
                  position: 'relative',
                  borderRadius: "5px",
                },
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
                  main: "#2169B2",
                },
                secondary: {
                  main: "#f50057",
                },
                text: {
                  primary: "#000",
                  hint: "#8a78ea",
                  secondary: "#000",
                },
                background: {
                  default: "#F5FBFC",
                  // default:"#FAFBFC"
                },
              }
            : {
                primary: {
                  main: indigo[300],
                },
                divider: indigo[700],
                background: {
                  default: "#1e1e1e",
                  paper: "#1e1e1e",
                },
                text: {
                  primary: "#fff",
                  secondary: grey[500],
                },
              }),
        },
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
