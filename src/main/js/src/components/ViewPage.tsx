import React, {useState} from "react";
import {useParams} from "react-router-dom";
import useInvoice from "../hooks/useInvoice";
import Page from "./common/Page";
import View from "./view/View";
import AppBarBackButton from "./common/AppBarBackButton";

export default function ViewPage() {
  const {invoiceId} = useParams();
  const [refresh, triggerRefresh] = useState({});
  const invoice = useInvoice(invoiceId, refresh);
  return <Page title={`View ${invoice?.invoiceName ?? ""}`} buttons={<AppBarBackButton
      archived={invoice ? invoice.invoiceArchived : undefined}/>}>
    <View invoice={invoice} triggerRefresh={triggerRefresh}/>
  </Page>
}