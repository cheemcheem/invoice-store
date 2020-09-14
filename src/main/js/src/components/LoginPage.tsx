import React from "react";
import Page from "./common/Page";
import Login from "./login/Login";

export default function LoginPage() {
  return <Page title={"Login to Invoice Store"}>
    <Login/>
  </Page>
}