import React from "react";
import {Redirect} from "react-router-dom";
import useLoggedIn from "../../hooks/useLoggedIn";

export default function WithAuth({children}: React.PropsWithChildren<any>) {
  const loggedIn = useLoggedIn();

  if (loggedIn === false) {
    return <Redirect to="/login"/>
  }

  return children;
}
