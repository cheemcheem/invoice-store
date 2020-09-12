import React, {useCallback, useEffect, useMemo, useState} from "react";
import {useParams} from "react-router-dom";
import {Invoice} from "../common/Types";
import download from "downloadjs";
import * as Cookie from "js-cookie";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import DeleteIcon from '@material-ui/icons/Delete';
import ArchiveIcon from '@material-ui/icons/Archive';
import RestoreIcon from '@material-ui/icons/Restore';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';
import AttachFileIcon from '@material-ui/icons/AttachFile';
import CardContent from "@material-ui/core/CardContent";
import Grid from "@material-ui/core/Grid";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardMedia from "@material-ui/core/CardMedia";
import Button from "@material-ui/core/Button";
import CardActions from "@material-ui/core/CardActions";
import CardHeader from "@material-ui/core/CardHeader";
import Typography from "@material-ui/core/Typography";
import useRedirect from "../hooks/useRedirect";
import {useSnackbar} from "notistack";
import Skeleton from "@material-ui/lab/Skeleton";

const useStyles = makeStyles((theme: Theme) => createStyles({
  grid: {
    height: "100%",
    padding: theme.spacing(2),
    paddingTop: theme.spacing(4),
    paddingBottom: theme.spacing(4)
  },
  media: {
    height: 200
  },
  cardContent: {
    width: "100%",
    height: window.innerHeight - theme.spacing(16),
    overflowY: "scroll",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
  },
  buttons: {
    display: "flex",
    flexDirection: "row",
    justifyContent: "space-between",
    padding: theme.spacing(2),
  },
  backdrop: {
    zIndex: theme.zIndex.drawer + 1,
    color: '#fff',
  },
  fileHeader: {
    display: "flex",
    flexDirection: "row",
    justifyContent: "flex-end",
    alignItems: "flex-start"
  }
}));


export default function ViewInvoicePage() {
  const classes = useStyles();
  const {enqueueSnackbar, closeSnackbar} = useSnackbar();
  const {component, triggerRedirect} = useRedirect();
  const {invoiceId} = useParams();
  const [invoice, setInvoice] = useState(undefined as undefined | Invoice);
  const [canRender, setCanRender] = useState(true);

  const update = useCallback(() => {
    fetch(`/api/invoice/details/${invoiceId}`)
    .then(response => response.text())
    .then(JSON.parse)
    .then(invoice => ({...invoice, invoiceDate: new Date(invoice.invoiceDate)}))
    .then(setInvoice);
  }, [setInvoice, invoiceId]);

  const archiveButton = useCallback(() => {
    fetch(`/api/invoice/${invoice?.invoiceArchived ? "restore" : "archive"}/${invoice?.invoiceDetailsId}`, {
      method: "PUT",
      headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))}
    })
    .then(() => {
      if (invoice?.invoiceArchived) {
        enqueueSnackbar("Restored invoice.", {variant: "success"})
      } else {
        enqueueSnackbar("Archived invoice.", {variant: "warning"})
      }
    })
    .then(update)
    .catch(() => {
      if (invoice?.invoiceArchived) {
        enqueueSnackbar("Failed to restore invoice.", {variant: "error"})
      } else {
        enqueueSnackbar("Failed to archive invoice.", {variant: "error"})
      }
    })
  }, [invoice, update, enqueueSnackbar]);

  const deleteButton = useCallback(() => {
    fetch(`/api/invoice/delete/${invoice?.invoiceDetailsId}`, {
      method: "DELETE",
      headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))}
    })
    .then((response) => {
      if (response.status === 406) {
        enqueueSnackbar("Failed to delete, needs to be archived first.", {variant: "error"})
      } else {
        enqueueSnackbar("Deleted invoice.", {variant: "warning"})
        triggerRedirect("/all");
      }
    })
    .catch(() => {
      enqueueSnackbar("Failed to delete, unknown error.", {variant: "error"})
    })
  }, [invoice, enqueueSnackbar, triggerRedirect]);

  const downloadButton = useCallback(() => {
    if (!invoice?.invoiceFile) {
      return enqueueSnackbar("Can't download, no file.", {variant: "warning"})
    }
    const key = enqueueSnackbar("Starting download...", {variant: "info", persist: true})
    fetch(`/api/invoice/file/${invoice?.invoiceFile?.invoiceFileId}`)
    .then(response => response.blob())
    .then(blob => {
      download(blob, invoice?.invoiceFile?.invoiceFileName, invoice?.invoiceFile?.invoiceFileType)
    })
    .then(() => {
      closeSnackbar(key);
      enqueueSnackbar("Download complete.", {variant: "success"});
    })
    .catch(() => {
      closeSnackbar(key);
      enqueueSnackbar("Failed to download.", {variant: "error"});
    })
  }, [invoice, enqueueSnackbar, closeSnackbar]);

  const hasFile = useMemo(() => {
    return invoice?.invoiceFile !== undefined
        && invoice?.invoiceFile !== null
  }, [invoice]);

  const dateTimeFormat = useMemo(() => Intl.DateTimeFormat("en-GB", {
    year: 'numeric',
    month: 'numeric',
    day: 'numeric'
  }), []);

  useEffect(update, [update]);

  return <>
    <Grid className={classes.grid}
          container
          direction="row"
          justify="center"
          alignContent="flex-start">
      <Card className={classes.cardContent}>
        <CardActionArea onClick={hasFile ? downloadButton : undefined}>
          {hasFile && (canRender
              ? <>
                <CardMedia className={classes.media}
                           image={`/api/invoice/file/${invoice!.invoiceFile!.invoiceFileId}`}
                           title={invoice!.invoiceFile!.invoiceFileName}
                           component="img"
                           alt={invoice!.invoiceFile!.invoiceFileName}
                           onError={() => setCanRender(false)}/>
              </>
              : <>
                <CardHeader
                    title={<Typography color={"textSecondary"} className={classes.fileHeader}>
                      {invoice!.invoiceFile!.invoiceFileName}
                      <AttachFileIcon/>
                    </Typography>}/>
              </>)
          }
        </CardActionArea>
        <CardContent>
          <CardHeader title={"Name"} subheader={invoice ? invoice.invoiceName : <Skeleton/>}/>
          <CardHeader title={"Date"}
                      subheader={invoice ? dateTimeFormat.format(invoice.invoiceDate) :
                          <Skeleton/>}/>
          <CardHeader title={"Total VAT"}
                      subheader={invoice ? `£${invoice.invoiceTotalVAT}` : <Skeleton/>}/>
          <CardHeader title={"Total"}
                      subheader={invoice ? `£${invoice.invoiceTotal}` : <Skeleton/>}/>
          <CardActions className={classes.buttons}>
            <Button disabled={!invoice || !hasFile} color="primary" onClick={downloadButton}
                    startIcon={<CloudDownloadIcon/>}>
              Download
            </Button>
            <Button disabled={!invoice}
                    color={hasFile ? "default" : "primary"}
                    onClick={archiveButton}
                    startIcon={invoice?.invoiceArchived ? <RestoreIcon/> : <ArchiveIcon/>}>
              {!(invoice?.invoiceArchived) ? "Archive" : "Restore"}
            </Button>
            <Button disabled={!invoice || !invoice?.invoiceArchived}
                    color="secondary"
                    onClick={deleteButton}
                    startIcon={<DeleteIcon/>}>
              Delete
            </Button>
          </CardActions>
        </CardContent>

      </Card>

    </Grid>
    {component}
  </>
}