import React from "react";
import {useCallback} from "react";
import {Redirect, useParams} from "react-router-dom";
import Page from "./common/Page";
import View from "./view/View";
import AppBarBackButton from "./common/AppBarBackButton";
import {useQuery} from "@apollo/client";
import {GET_INVOICE} from "../utils/Queries";
import {Invoice} from "../utils/Types";
import {Skeleton} from "@material-ui/lab";
import {useSnackbar} from "notistack";
import {createStyles, makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles(() => createStyles({
  skeletonText: {
    height: "2em",
  },
}));
export default function ViewPage() {
  const {invoiceId} = useParams();
  const classes = useStyles();
  const {enqueueSnackbar} = useSnackbar();
  const {loading, error, data} = useQuery<{ invoiceById: Invoice | null }>(GET_INVOICE, {variables: {invoiceId}});

  if (data?.invoiceById === null) {
    enqueueSnackbar("Invoice not found!", {variant: "warning"})
    return <Redirect to="/all"/>;
  }

  if (error) {
    enqueueSnackbar("Failed to load invoice!", {variant: "error"})
    return <Redirect to="/all"/>;
  }

  return <Page title={loading
      ? <Skeleton className={classes.skeletonText}/>
      : `View ${data!.invoiceById?.name ?? ""}`}
               buttons={
                 <AppBarBackButton archived={data ? data!.invoiceById?.archived : undefined}/>
               }>
    <View invoice={data?.invoiceById ?? undefined} loading={loading}/>
  </Page>
}