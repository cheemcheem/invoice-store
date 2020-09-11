import React, {useEffect, useState} from 'react';
import './App.css';
import MainPage from "./components/MainPage";
import LoginPage from "./components/LoginPage";
import {AppBar} from "@material-ui/core";
import {Box} from "@material-ui/core";
import {SnackbarProvider} from 'notistack';

export default function App() {
  const [loggedIn, setIsLoggedIn] = useState("not checked" as "not checked" | boolean);

  useEffect(() => {
    fetch("/api/user")
    .then(response => {
      if (response.redirected) {
        return setIsLoggedIn(false);
      }
      if (response.headers.has('Content-Length')
          && Number(response.headers.get('Content-Length')) === 0) {
        return setIsLoggedIn(false);
      }

      return setIsLoggedIn(true);

    })
    .catch(() => setIsLoggedIn(false))
  }, [setIsLoggedIn]);

  return <SnackbarProvider maxSnack={3} autoHideDuration={1000} anchorOrigin={{
    vertical: 'top',
    horizontal: 'center'
  }}>
    <Box>{
      loggedIn === "not checked"
          ? <AppBar/>
          : loggedIn ? <MainPage/> : <LoginPage/>
    }</Box>
  </SnackbarProvider>
}