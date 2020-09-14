import React from 'react';
import MainPage from "./components/MainPage";
import Box from "@material-ui/core/Box";
import {SnackbarProvider} from 'notistack';
import {makeStyles} from "@material-ui/core/styles";
import {Theme} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";
const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      snackbar: {
        marginTop: theme.spacing(6)
      },
    })
)
export default function App() {
  const classes = useStyles();
  return <>
    <SnackbarProvider maxSnack={3}
                      className={classes.snackbar}
                      autoHideDuration={1000}
                      anchorOrigin={{vertical: 'top', horizontal: 'center'}}>
      <Box>
        <MainPage/>
      </Box>
    </SnackbarProvider>
  </>;
}