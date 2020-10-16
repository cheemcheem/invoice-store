import Page from "./common/Page";
import React, {useCallback, useMemo} from "react";
import {useEffect} from "react";
import ViewAll from "./viewAll/ViewAll";
import AppBarMoreButton from "./common/AppBarMoreButton";
import useRedirect from "../hooks/useRedirect";
import {useSnackbar} from "notistack";
import download from "downloadjs";
import AddIcon from "@material-ui/icons/Add";
import GetAppIcon from '@material-ui/icons/GetApp';
import {useQuery} from "@apollo/client";
import {BasicInvoice} from "../utils/Types";
import {GET_ALL_INVOICES} from "../utils/Queries";

export type ViewAllPageProps = { archived?: boolean };
export default function ViewAllPage({archived = false}: ViewAllPageProps) {
  const title = archived ? "Archive" : "Active Invoices";
  const {enqueueSnackbar, closeSnackbar} = useSnackbar();
  const {component, triggerRedirect} = useRedirect();

  const downloadCSV = useCallback(() => {
    const key = enqueueSnackbar("Starting download...", {variant: "info", persist: true})
    fetch(`/api/invoice/csv`)
    .then(response => {
      if (response.status === 204) {
        closeSnackbar(key);
        enqueueSnackbar("No records.", {variant: "info"});
      } else if (response.status === 200) {
        response.blob()
        .then(blob => {
          download(blob, response.headers
          .get("Content-Disposition")!
          .split(`attachment; filename="`)[1]
          .split(`"`)[0])
        })
        .then(() => {
          closeSnackbar(key);
          enqueueSnackbar("Download complete.", {variant: "success"});
        })
      } else {
        throw new Error("Invalid status code.");
      }
    })
    .catch(() => {
      closeSnackbar(key);
      enqueueSnackbar("Failed to download.", {variant: "error"});
    })
  }, [enqueueSnackbar, closeSnackbar]);

  const buttons = useMemo(() => {
    if (archived) {
      return <></>;
    }
    const options = [
      {option: "Create", icon: <AddIcon/>, onClick: () => triggerRedirect("/new")},
      {option: "Export (CSV)", icon: <GetAppIcon/>, onClick: downloadCSV},
    ]
    return <>
      <AppBarMoreButton options={options}/>
    </>
  }, [triggerRedirect, downloadCSV, archived]);

  const {loading, error, data, refetch} = useQuery<{ user: { invoices: BasicInvoice[] } }>(GET_ALL_INVOICES);

  if (error) {
    // wait for login todo count failed attempts and redirect to login page eventually
    setTimeout(refetch, 1000);
  }

  useEffect(() => {
    // this keeps the invoice list updated on page refresh
    refetch();
  }, [refetch]);

  return <Page title={title} buttons={buttons}>
    <ViewAll archived={archived}
             allInvoices={data?.user?.invoices.filter(i => i.archived === archived).sort((a, b) => {
               if (a.date < b.date) {
                 return 1;
               }
               if (a.date > b.date) {
                 return -1;
               }
               if (a.name > b.name) {
                 return 1;
               }
               if (a.name < b.name) {
                 return -1;
               }
               return 0;
             }) ?? []}
             loading={loading || (error !== undefined)}/>
    {component}
  </Page>;
}