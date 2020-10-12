import React from "react";
import Page from "./common/Page";
import NotFound from "./notFound/NotFound";
import AppBarBackButton from "./common/AppBarBackButton";

export default function NotFoundPage() {
  return <Page title="Not Found!"
               buttons={<AppBarBackButton archived={false}/>}>
    <NotFound/>
  </Page>;
}