import React from "react";
import {useContext} from "react";
import Page from "./common/Page";
import Home from "./home/Home";
import {Redirect} from "react-router-dom";
import {LoggedInContext} from "../utils/Providers";

export default function HomePage() {

  const {loggedIn} = useContext(LoggedInContext);

  if (loggedIn) {
    return <Redirect to={"/all"}/>
  }

  return <Page title={"Invoice Store"}>
    <Home/>
  </Page>
}