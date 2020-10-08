import React from "react";
import {useMemo} from "react";
import Page from "./common/Page";
import Create from "./create/Create";
import {CreateState} from "./create/Create";
import AppBarBackButton from "./common/AppBarBackButton";
import useRedirect from "../hooks/useRedirect";
import {useSnackbar} from "notistack";
import {useMutation} from "@apollo/client";
import {gql} from "@apollo/client";
import {Invoice} from "../utils/Types";
import {BasicInvoice} from "../utils/Types";
import {CREATE_INVOICE} from "../utils/Queries";
import Cookies from "js-cookie";
import useLoggedIn from "../hooks/useLoggedIn";

export default function CreatePage() {
  const {component, triggerRedirect} = useRedirect();
  const {enqueueSnackbar, closeSnackbar} = useSnackbar();
  const {userId} = useLoggedIn();

  const [createInvoice] = useMutation<{ createInvoice: Invoice }>(CREATE_INVOICE, {
    update(cache, {data}) {
      if (!data) {
        return;
      }
      const invoice = data!.createInvoice;
      cache.modify({
        id: `User:${userId}`,
        fields: {
          invoices(existingInvoices: BasicInvoice[] = []) {
            const newInvoice = cache.writeFragment({
              data: invoice,
              fragment: gql`
                  fragment NewInvoice on Invoice {
                      id
                      date
                      name
                      total
                      vatTotal
                      archived
                  }
              `
            })
            return [...existingInvoices, newInvoice]
          }
        }
      })

    }
  });

  const submit = useMemo(() => (values: CreateState) => {
    const key = enqueueSnackbar("Uploading invoice.", {variant: "info", persist: true});

    const upload = (id: string) => {
      if (!values.invoiceFile) {
        return id;
      }
      const formData = new FormData();
      formData.append("invoiceFile", values.invoiceFile!);
      return fetch(`/api/invoice/file/${id}`, {
        method: "PUT",
        headers: {"X-XSRF-TOKEN": Cookies.get("XSRF-TOKEN")!},
        body: formData
      }).then(() => id);
    };

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
        throw result.errors;
      }
      return result.data!.createInvoice.id;
    })
    .then(upload)
    .then((id: string) => {
      enqueueSnackbar("Created invoice!", {variant: "success"});
      triggerRedirect(`/view/${id}`);
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