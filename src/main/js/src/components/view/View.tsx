import {makeStyles} from "@material-ui/core/styles";
import {Theme} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";
import {useState} from "react";
import React from "react";
import Grid from "@material-ui/core/Grid";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardMedia from "@material-ui/core/CardMedia";
import CardHeader from "@material-ui/core/CardHeader";
import Typography from "@material-ui/core/Typography";
import AttachFileIcon from "@material-ui/icons/AttachFile";
import CardContent from "@material-ui/core/CardContent";
import Skeleton from "@material-ui/lab/Skeleton";
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
  loading: boolean
  archiveButton: () => void,
  deleteButton: () => void,
  downloadButton: () => void
}

export default function View({invoice, loading, archiveButton, deleteButton, downloadButton}: ViewProps) {
  const classes = useStyles();

  const [canRender, setCanRender] = useState(true);
  const hasFile = !loading && invoice!.invoiceFile !== undefined && invoice!.invoiceFile !== null;

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
                           image={`/api/invoice/file/${invoice!.id!}`}
                           title={invoice!.invoiceFile!.name}
                           component="img"
                           alt={invoice!.invoiceFile!.name}
                           onError={() => setCanRender(false)}/>
              </>
              : <>
                <CardHeader
                    title={<Typography color={"textSecondary"} className={classes.fileHeader}>
                      {invoice!.invoiceFile!.name}
                      <AttachFileIcon/>
                    </Typography>}/>
              </>)
          }
        </CardActionArea>
        <CardContent>
          <CardHeader title={"NAME"} subheader={loading ? <Skeleton/> : invoice!.name}/>
          <CardHeader title={"DATE"}
                      subheader={loading ? <Skeleton/> : invoice!.date}/>
          <CardHeader title={"VAT TOTAL"}
                      subheader={loading ? <Skeleton/> : `£${invoice!.vatTotal}`}/>
          <CardHeader title={"TOTAL"}
                      subheader={loading ? <Skeleton/> : `£${invoice!.total}`}/>
          <CardActions className={classes.buttons}>
            <Button disabled={loading || !hasFile} color="primary" onClick={downloadButton}
                    startIcon={<CloudDownloadIcon/>}>
              Download
            </Button>
            <Button disabled={loading}
                    color={hasFile ? "default" : "primary"}
                    onClick={archiveButton}
                    startIcon={invoice?.archived ? <RestoreIcon/> : <ArchiveIcon/>}>
              {loading ? "Archive" : !(invoice!.archived) ? "Archive" : "Restore"}
            </Button>
            <Button disabled={loading || !invoice!.archived}
                    color="secondary"
                    onClick={deleteButton}
                    startIcon={<DeleteIcon/>}>
              Delete
            </Button>
          </CardActions>
        </CardContent>
      </Card>
    </Grid>
  </>
}