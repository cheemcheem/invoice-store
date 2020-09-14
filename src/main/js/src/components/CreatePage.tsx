import React from "react";
import Page from "./common/Page";
import Create from "./create/Create";
import AppBarBackButton from "./common/AppBarBackButton";

export default function CreatePage() {
  return <Page title="Create New Invoice" buttons={<AppBarBackButton archived={false}/>}>
    <Create/>
  </Page>
}