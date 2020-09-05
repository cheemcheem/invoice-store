import React, {useEffect, useState} from 'react';
import './App.css';
import MainPage from "./components/MainPage";
import LoginPage from "./components/LoginPage";

export default function App() {

  const [loggedIn, setIsLoggedIn] = useState(false);

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

  return loggedIn ? <MainPage/> : <LoginPage/>;
}