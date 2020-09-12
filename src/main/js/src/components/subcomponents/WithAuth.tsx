import React from "react";
import {useState} from "react";
import {useCallback} from "react";
import {useEffect} from "react";
import {AppBar} from "@material-ui/core";
import {Redirect} from "react-router-dom";

export default function WithAuth({children}: React.PropsWithChildren<any>) {
  const [loggedIn, setIsLoggedIn] = useState("not checked" as "not checked" | boolean);

  const checkLoggedIn = useCallback(() => {
    fetch("/api/user", {keepalive: true})
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

  useEffect(checkLoggedIn, [checkLoggedIn]);

  if (loggedIn === "not checked") {
    return <AppBar/>;
  }

  if (!loggedIn) {
    return <Redirect to="/login"/>
  }

  return children;
}
