import React, {useEffect, useState} from "react";
import {FixedSizeList, ListChildComponentProps} from 'react-window';
import {Theme} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";
import {makeStyles} from "@material-ui/core/styles";
import {useTheme} from "@material-ui/core/styles";
import {CardContent} from "@material-ui/core";
import {Card} from "@material-ui/core";
import {CardHeader} from "@material-ui/core";
import {ListItem} from "@material-ui/core";
import {ListItemText} from "@material-ui/core";
import {CircularProgress} from "@material-ui/core";
import {Backdrop} from "@material-ui/core";
import useRedirect from "../hooks/useRedirect";
import {useSnackbar} from "notistack";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      root: {
        width: '100%',
        height: '100%',
        backgroundColor: theme.palette.background.paper,
        // padding: theme.spacing(2),
        // boxSizing: "border-box"
      },
      row: {
        padding: theme.spacing(2),
        width: "100%",
        backgroundColor: theme.palette.grey["100"],
      },
      darkRow: {
        backgroundColor: theme.palette.grey["300"],
        color: theme.palette.text.primary
      },
      list: {
        boxSizing: "border-box",
        scrollbarWidth: "none"
      },
      backdrop: {
        zIndex: theme.zIndex.drawer + 1,
        color: '#fff',
      },
      cardContent: {
        padding: 0,
        paddingBottom: 0
      },
    }),
);

const dateTimeFormat = Intl.DateTimeFormat("en-GB", {
  year: 'numeric',
  month: 'numeric',
  day: 'numeric'
})

function RenderRow(props: ListChildComponentProps) {
  const classes = useStyles();
  const {index, style, data} = props;
  const {allInvoices} = data;
  const invoice = allInvoices[index];
  const {component, triggerRedirect} = useRedirect();

  return (
      <ListItem button
                style={style}
                key={index}
                onClick={() => triggerRedirect(`/view/${invoice.invoiceId}`)}
                className={classes.row + " " + (index % 2 === 1 ? classes.darkRow : undefined)}>
        <ListItemText primary={invoice.invoiceName}
                      secondary={dateTimeFormat.format(new Date(invoice.invoiceDate))}
        />
        {component}
      </ListItem>
  );
}


export default function ViewAllInvoices({archived}: { archived?: boolean }) {
  const classes = useStyles();
  const {enqueueSnackbar} = useSnackbar();

  const [allInvoices, setAllInvoices] = useState(undefined as undefined | any[]);

  useEffect(() => {
    fetch(`/api/invoice/${archived ? "archived" : "all"}`)
    .then(response => response.text())
    .then(JSON.parse)
    .then(setAllInvoices)
    .catch(() => {
      enqueueSnackbar("Failed to retrieve invoices...", {variant: "error"});
      setAllInvoices([])
    });
  }, [archived, setAllInvoices, enqueueSnackbar]);

  const theme = useTheme();
  return <div className={classes.root}>
    <Card>
      {allInvoices
          ? allInvoices.length === 0
              ? <CardContent>
                <CardHeader title={"Empty!"}
                            subheader={`Looks like there are no${archived ? " archived" : ""} invoices`}/>
              </CardContent>
              : <FixedSizeList height={window.innerHeight - theme.spacing(6)}
                               width={"100%"}
                               itemSize={80}
                               itemCount={allInvoices.length}
                               itemData={{allInvoices}}
                               className={classes.list}
                               overscanCount={10}
              >
                {RenderRow}
              </FixedSizeList>
          : <Backdrop className={classes.backdrop} open={!allInvoices}>
            <CircularProgress color="inherit"/>
          </Backdrop>
      }
    </Card>
  </div>
}