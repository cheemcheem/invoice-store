import React from "react";
import {useCallback} from "react";
import {useMemo} from "react";
import {Redirect, useParams} from "react-router-dom";
import Page from "./common/Page";
import View from "./view/View";
import AppBarBackButton from "./common/AppBarBackButton";
import {useQuery} from "@apollo/client";
import {useMutation} from "@apollo/client";
import {GET_INVOICE} from "../utils/Queries";
import {ARCHIVE_INVOICE} from "../utils/Queries";
import {DELETE_INVOICE} from "../utils/Queries";
import {GET_ALL_INVOICES} from "../utils/Queries";
import {Invoice} from "../utils/Types";
import {BasicInvoice} from "../utils/Types";
import {Skeleton} from "@material-ui/lab";
import {useSnackbar} from "notistack";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import useRedirect from "../hooks/useRedirect";
import download from "downloadjs";
import NotFound from "./notFound/NotFound";

const useStyles = makeStyles(() => createStyles({
  skeletonText: {
    height: "2em",
  },
}));
export default function ViewPage() {
  const {invoiceId} = useParams();
  const classes = useStyles();
  const {enqueueSnackbar, closeSnackbar} = useSnackbar();
  const {loading, error, data} = useQuery<{ invoiceById: Invoice | null }>(GET_INVOICE, {variables: {invoiceId}});

  const {component, triggerRedirect} = useRedirect();
  const [setArchived] = useMutation(ARCHIVE_INVOICE);
  const [deleteInvoice] = useMutation(DELETE_INVOICE);
  const archiveButton = useCallback(() => {
    if (loading) {
      return;
    }
    const archived = data!.invoiceById!.archived;
    setArchived({variables: {updated: {id: data!.invoiceById!.id, archived: !archived}}})
    .catch(() => {
      if (archived) {
        enqueueSnackbar("Failed to restore invoice.", {variant: "error"})
      } else {
        enqueueSnackbar("Failed to archive invoice.", {variant: "error"})
      }
    })
  }, [data, enqueueSnackbar, setArchived, loading]);
  const deleteButton = useCallback(() => {
    if (loading) {
      return;
    }
    deleteInvoice({
      variables: {id: data!.invoiceById!.id},
      update: async (cache, mutationResult) => {
        if (mutationResult.data.deleteInvoice) {
          const oldData = cache.readQuery({query: GET_ALL_INVOICES}) as { user: { invoices: BasicInvoice[] } };
          const newInvoices = oldData.user.invoices.filter(i => i.id !== data!.invoiceById!.id);
          await cache.writeQuery({
            query: GET_ALL_INVOICES,
            data: {...oldData, user: {...oldData.user, invoices: newInvoices}}
          })
        }
      }
    })
    .then(({data}) => {
      if (data.deleteInvoice) {
        triggerRedirect("/all");
      } else {
        enqueueSnackbar("Failed to delete.", {variant: "error"})
      }
    })
    .catch(() => {
      enqueueSnackbar("Failed to delete.", {variant: "error"})
    })
  }, [data, deleteInvoice, enqueueSnackbar, triggerRedirect, loading]);
  const downloadButton = useCallback(() => {
    if (loading) {
      return;
    }
    if (!data!.invoiceById!.invoiceFile) {
      return enqueueSnackbar("Can't download, no file.", {variant: "warning"})
    }
    const key = enqueueSnackbar("Starting download...", {variant: "info", persist: true})
    fetch(`/api/invoice/file/${data!.invoiceById!.id}`)
    .then(response => response.blob())
    .then(blob => {
      download(blob, data!.invoiceById!.invoiceFile?.name, data!.invoiceById!.invoiceFile?.type)
    })
    .then(() => {
      closeSnackbar(key);
      enqueueSnackbar("Download complete.", {variant: "success"});
    })
    .catch(() => {
      closeSnackbar(key);
      enqueueSnackbar("Failed to download.", {variant: "error"});
    })
  }, [data, enqueueSnackbar, closeSnackbar, loading]);

  const notFound = useMemo(() => data?.invoiceById === null, [data]);

  const title = useMemo(() => {
    if (loading) {
      return <Skeleton className={classes.skeletonText}/>;
    } else if (notFound) {
      return `Not Found`;
    } else {
      return `View ${data?.invoiceById?.name ?? ""}`;
    }
  }, [loading, classes.skeletonText, notFound, data]);

  const buttons = useMemo(() => {
    let archived;

    if (loading) {
      archived = undefined;
    } else if (notFound) {
      archived = false;
    } else {
      archived = data?.invoiceById?.archived;
    }

    return <AppBarBackButton archived={archived}/>
  }, [data, loading, notFound]);


  if (error) {
    enqueueSnackbar("Failed to load invoice!", {variant: "error"})
    return <Redirect to="/all"/>;
  }

  return <Page title={title}
               buttons={buttons}>
    {notFound
        ? <NotFound/>
        : <View invoice={data?.invoiceById ?? undefined}
                loading={loading}
                archiveButton={archiveButton}
                deleteButton={deleteButton}
                downloadButton={downloadButton}/>
    }
    {component}
  </Page>
}