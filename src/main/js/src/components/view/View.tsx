import {makeStyles} from "@material-ui/core/styles";
import {Theme} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";
import {useSnackbar} from "notistack";
import useRedirect from "../../hooks/useRedirect";
import {useState} from "react";
import {useCallback} from "react";
import {useMemo} from "react";
import React from "react";
import * as Cookie from "js-cookie";
import download from "downloadjs";
import Grid from "@material-ui/core/Grid";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardMedia from "@material-ui/core/CardMedia";
import CardHeader from "@material-ui/core/CardHeader";
import Typography from "@material-ui/core/Typography";
import AttachFileIcon from "@material-ui/icons/AttachFile";
import CardContent from "@material-ui/core/CardContent";
import Skeleton from "@material-ui/lab/Skeleton";
import FormatDate from "../../utils/DateTimeFormat";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import CloudDownloadIcon from "@material-ui/icons/CloudDownload";
import RestoreIcon from "@material-ui/icons/Restore";
import ArchiveIcon from "@material-ui/icons/Archive";
import DeleteIcon from "@material-ui/icons/Delete";
import {Invoice} from "../../utils/Types";

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

type ViewProps = {
  invoice: Invoice | undefined,
  triggerRefresh: any
}
export default function View({invoice, triggerRefresh}: ViewProps) {
  const classes = useStyles();
  const {enqueueSnackbar, closeSnackbar} = useSnackbar();
  const {component, triggerRedirect} = useRedirect();
  const [canRender, setCanRender] = useState(true);

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
    .then(() => triggerRefresh({}))
    .catch(() => {
      if (invoice?.invoiceArchived) {
        enqueueSnackbar("Failed to restore invoice.", {variant: "error"})
      } else {
        enqueueSnackbar("Failed to archive invoice.", {variant: "error"})
      }
    })
  }, [invoice, triggerRefresh, enqueueSnackbar]);

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
    fetch(`/api/invoice/file/${invoice?.invoiceDetailsId}`)
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
                           image={`/api/invoice/file/${invoice!.invoiceDetailsId!}`}
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
          <CardHeader title={"NAME"} subheader={invoice ? invoice.invoiceName : <Skeleton/>}/>
          <CardHeader title={"DATE"}
                      subheader={invoice ? FormatDate(invoice.invoiceDate) :
                          <Skeleton/>}/>
          <CardHeader title={"VAT TOTAL"}
                      subheader={invoice ? `£${invoice.invoiceTotalVAT}` : <Skeleton/>}/>
          <CardHeader title={"TOTAL"}
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