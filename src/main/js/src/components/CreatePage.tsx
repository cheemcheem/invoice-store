import React from "react";
import {useMemo} from "react";
import Page from "./common/Page";
import Create from "./create/Create";
import {CreateState} from "./create/Create";
import AppBarBackButton from "./common/AppBarBackButton";
import useRedirect from "../hooks/useRedirect";
import {useSnackbar} from "notistack";
import {useMutation} from "@apollo/client";
import {Invoice} from "../utils/Types";
import {CREATE_INVOICE} from "../utils/Queries";

export default function CreatePage() {
  const {component, triggerRedirect} = useRedirect();
  const {enqueueSnackbar, closeSnackbar} = useSnackbar();
  const [createInvoice] = useMutation<{ createInvoice: Invoice }>(CREATE_INVOICE);

  const submit = useMemo(() => (values: CreateState) => {

    const key = enqueueSnackbar("Uploading invoice.", {variant: "info", persist: true});
    createInvoice({
      variables: {
        input: {
          date: values.invoiceDate.toISOString().split("T")[0],
          name: values.invoiceName,
          vatTotal: values.invoiceTotalVAT,
          total: values.invoiceTotal
        }
      }
    })
    .then((result) => {
      closeSnackbar(key);

      if (result.errors) {
        enqueueSnackbar("Failed to create invoice.", {variant: "error"});
      } else {
        enqueueSnackbar("Created invoice!", {variant: "success"});
        triggerRedirect(`/view/${result?.data!.createInvoice.id}`);
      }
    })
    .catch((e) => {
      console.log({e})
      closeSnackbar(key);
      enqueueSnackbar("Failed to create invoice.", {variant: "error"});
    })
    ;
  }, [triggerRedirect, enqueueSnackbar, closeSnackbar, createInvoice]);

  return <Page title="Create New Invoice" buttons={<AppBarBackButton archived={false}/>}>
    <Create submit={submit}/>
    {component}
  </Page>
}