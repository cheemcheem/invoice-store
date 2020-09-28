import React from "react";
import Page from "./common/Page";
import Home from "./home/Home";
import useLoggedIn from "../hooks/useLoggedIn";
import {Redirect} from "react-router-dom";

export default function HomePage() {

  const loggedIn = useLoggedIn();

  if (loggedIn) {
    return <Redirect to={"/all"}/>
  }

  return <Page title={"Invoice Store"}>
    <Home/>
  </Page>
}