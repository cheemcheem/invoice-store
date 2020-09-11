import React, {useCallback, useEffect, useMemo, useState} from "react";
import {useParams} from "react-router-dom";
import {Invoice} from "../common/Types";
import download from "downloadjs";
import * as Cookie from "js-cookie";
import {makeStyles} from "@material-ui/core/styles";
import {Theme} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";
import DeleteIcon from '@material-ui/icons/Delete';
import ArchiveIcon from '@material-ui/icons/Archive';
import RestoreIcon from '@material-ui/icons/Restore';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';
import AttachFileIcon from '@material-ui/icons/AttachFile';
import {CardContent} from "@material-ui/core";
import {Grid} from "@material-ui/core";
import {Card} from "@material-ui/core";
import {CardActionArea} from "@material-ui/core";
import {CardMedia} from "@material-ui/core";
import {Button} from "@material-ui/core";
import {CardActions} from "@material-ui/core";
import {CardHeader} from "@material-ui/core";
import {Typography} from "@material-ui/core";
import {CircularProgress} from "@material-ui/core";
import {Backdrop} from "@material-ui/core";
import useRedirect from "../hooks/useRedirect";
import {useSnackbar} from "notistack";

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
    height: window.innerHeight - theme.spacing(16)
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
}));


export default function ViewInvoicePage() {
  const classes = useStyles();
  const {enqueueSnackbar, closeSnackbar} = useSnackbar();
  const {invoiceId} = useParams();
  const [invoice, setInvoice] = useState(undefined as undefined | Invoice);

  const update = useCallback(() => {
    fetch(`/api/invoice/details/${invoiceId}`)
    .then(response => response.text())
    .then(JSON.parse)
    .then(invoice => ({
      ...invoice,
      invoiceDate: new Date(invoice.invoiceDate)
    }))
    .then(setInvoice);
  }, [setInvoice, invoiceId]);

  useEffect(update, [update]);
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
  }, [invoice, update, enqueueSnackbar])

  const {component, triggerRedirect} = useRedirect();
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
  }, [invoice, enqueueSnackbar, triggerRedirect])

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
    ;

  }, [invoice, enqueueSnackbar])

  const hasFile = useMemo(() => invoice?.invoiceFile !== undefined && invoice?.invoiceFile !== null, [invoice])

  const dateTimeFormat = useMemo(() => Intl.DateTimeFormat("en-GB", {
    year: 'numeric',
    month: 'numeric',
    day: 'numeric'
  }), []);

  const [pdf, setIsPdf] = useState(false);


  return <>
    <Grid className={classes.grid}
          container
          direction="row"
          justify="center"
          alignContent="flex-start"
    >
      {invoice
          ? <Card className={classes.cardContent}>
            <CardActionArea>
              {hasFile && !pdf && <CardMedia className={classes.media}
                                             image={`/api/invoice/file/${invoice.invoiceFile!.invoiceFileId}`}
                                             title={invoice.invoiceFile!.invoiceFileName}
                                             component="img"
                                             alt={invoice.invoiceFile!.invoiceFileName}
                                             onError={() => setIsPdf(true)}/>
              }
              {hasFile && pdf && <CardHeader title="File Name" subheader={<>
                <Typography variant={"body2"}
                            color={"textSecondary"}>{invoice.invoiceFile!.invoiceFileName}
                  <AttachFileIcon/></Typography>
              </>}/>}
              <CardContent>
                <CardHeader title={"Name"} subheader={invoice.invoiceName}/>
                <CardHeader title={"Date"} subheader={dateTimeFormat.format(invoice.invoiceDate)}/>
                <CardHeader title={"Total VAT"} subheader={`£${invoice.invoiceTotalVAT}`}/>
                <CardHeader title={"Total"} subheader={`£${invoice.invoiceTotal}`}/>
              </CardContent>
            </CardActionArea>
            <CardActions className={classes.buttons}>
              <Button disabled={!hasFile} color="primary" onClick={downloadButton}
                      startIcon={<CloudDownloadIcon/>}>
                Download
              </Button>
              <Button color={hasFile ? "default" : "primary"}
                      onClick={archiveButton}
                      startIcon={invoice.invoiceArchived ? <RestoreIcon/> : <ArchiveIcon/>}
              >
                {invoice.invoiceArchived ? "Restore" : "Archive"}
              </Button>
              <Button disabled={!invoice.invoiceArchived}
                      color="secondary"
                      onClick={deleteButton}
                      startIcon={<DeleteIcon/>}
              >
                Delete
              </Button>
            </CardActions>
          </Card>
          : <Backdrop className={classes.backdrop} open={!invoice}>
            <CircularProgress color="inherit"/>
          </Backdrop>
      }
    </Grid>
    {component}
  </>
}