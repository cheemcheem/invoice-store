import Page from "./common/Page";
import React from "react";
import ViewAll from "./viewAll/ViewAll";
import {ViewAllProps} from "./viewAll/ViewAll";
import AppBarCreateButton from "./common/AppBarCreateButton";

export default function ViewAllPage(props: ViewAllProps) {
  const title = props.archived ? "View Archived Invoices" : "View All Invoices"
  return <Page title={title} buttons={<AppBarCreateButton/>}>
    {ViewAll(props)}
  </Page>;
}