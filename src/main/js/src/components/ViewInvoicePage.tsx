import React, {useCallback, useEffect, useMemo, useState} from "react";
import {useParams} from "react-router-dom";
import {Invoice} from "../common/Types";
import download from "downloadjs";
import * as Cookie from "js-cookie";
import {
  Button,
  Card,
  CardActionArea,
  CardActions,
  CardContent,
  CardHeader,
  CardMedia,
  createStyles,
  Grid,
  Theme,
  Typography
} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import DeleteIcon from '@material-ui/icons/Delete';
import ArchiveIcon from '@material-ui/icons/Archive';
import RestoreIcon from '@material-ui/icons/Restore';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';
import AttachFileIcon from '@material-ui/icons/AttachFile';

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
  card: {
    width: "100%",
    height: window.innerHeight - theme.spacing(16)
  },
  buttons: {
    display: "flex",
    flexDirection: "row",
    justifyContent: "space-between",
    padding: theme.spacing(2),
  }
}));
export default function ViewInvoicePage() {
  const classes = useStyles();

  const {invoiceId} = useParams();
  const [invoice, setInvoice] = useState({} as Invoice);

  const update = useCallback(() => {
    fetch(`/api/invoice/details/${invoiceId}`)
    .then(response => response.text())
    .then(JSON.parse)
    .then(invoice => ({...invoice, invoiceDate: new Date(invoice.invoiceDate)}))
    .then(setInvoice);
  }, [setInvoice, invoiceId]);

  useEffect(update, [update]);
  const archiveButton = useCallback(() => {
    fetch(`/api/invoice/${invoice.invoiceArchived ? "restore" : "archive"}/${invoice.invoiceDetailsId}`, {
      method: "PUT",
      headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))}
    })
    .then(update)
  }, [invoice.invoiceArchived, invoice.invoiceDetailsId, update])

  const deleteButton = useCallback(() => {
    fetch(`/api/invoice/delete/${invoice.invoiceDetailsId}`, {
      method: "DELETE",
      headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))}
    })
    .then(() => window.location.pathname = "/all")
  }, [invoice.invoiceDetailsId])

  const downloadButton = useCallback(() => {
    if (!invoice.invoiceFile) {
      return;
    }

    fetch(`/api/invoice/file/${invoice.invoiceFile.invoiceFileId}`)
    .then(response => response.blob())
    .then(blob => {
      download(blob, invoice.invoiceFile?.invoiceFileName, invoice.invoiceFile?.invoiceFileType)
    });

  }, [invoice.invoiceFile])

  const hasFile = useMemo(() => invoice.invoiceFile !== undefined && invoice.invoiceFile !== null, [invoice.invoiceFile])

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
      <Card className={classes.card}>
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
          {/*<ButtonGroup size="medium">*/}
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
          {/*</ButtonGroup>*/}
        </CardActions>
      </Card>

    </Grid>
  </>
}