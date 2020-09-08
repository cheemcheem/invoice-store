import React, {useEffect, useState} from 'react';
import './App.css';
import MainPage from "./components/MainPage";
import LoginPage from "./components/LoginPage";
import {AppBar, Box} from "@material-ui/core";

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

      // setIsLoggedIn(!response.redirected);
    })
    .catch(() => setIsLoggedIn(false))
  }, [setIsLoggedIn]);

  return <Box>{
    loggedIn === "not checked"
        ? <AppBar/>
        : loggedIn ? <MainPage/> : <LoginPage/>
  }</Box>;
}