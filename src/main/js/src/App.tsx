import React from 'react';
import './App.css';
import MainPage from "./components/MainPage";
import {Box} from "@material-ui/core";
import {SnackbarProvider} from 'notistack';

export default function App() {
  return <SnackbarProvider maxSnack={3} autoHideDuration={1000} anchorOrigin={{
    vertical: 'top',
    horizontal: 'center'
  }}>
    <Box>
      <MainPage/>
    </Box>
  </SnackbarProvider>
}