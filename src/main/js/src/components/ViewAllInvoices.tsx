import React, {useEffect, useState} from "react";
import {FixedSizeList, ListChildComponentProps} from 'react-window';
import {createStyles, makeStyles, Theme, useTheme} from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardHeader from "@material-ui/core/CardHeader";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import useRedirect from "../hooks/useRedirect";
import {useSnackbar} from "notistack";
import Skeleton from "@material-ui/lab/Skeleton";
import List from "@material-ui/core/List";
import {BasicInvoice} from "../common/Types";
import FormatDate from "../common/DateTimeFormat";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      root: {
        width: '100%',
        height: '100%',
        backgroundColor: theme.palette.background.paper,
      },
      row: {
        padding: theme.spacing(2),
        width: "100%",
        backgroundColor: theme.palette.grey["100"],
        height: 80,
      },
      primaryRow: {
        width: 160
      },
      secondaryRow: {
        width: "100%"
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
      loadingList: {
        padding: 0,
        overflowY: "hidden"
      }
    }),
);

function RenderRow(props: ListChildComponentProps) {
  const classes = useStyles();
  const {index, style, data} = props;
  const {allInvoices} = data as { allInvoices: BasicInvoice[] };
  const invoice = allInvoices[index];
  const {component, triggerRedirect} = useRedirect();
  return (
      <ListItem button
                style={style}
                key={index}
                onClick={() => triggerRedirect(`/view/${invoice.invoiceId}`)}
                className={classes.row + " " + (index % 2 === 1 ? classes.darkRow : undefined)}>
        <ListItemText key={index}
                      primary={invoice.invoiceName}
                      secondary={invoice.invoiceDate}
        />
        {component}
      </ListItem>
  );
}

export default function ViewAllInvoices({archived}: { archived?: boolean }) {
  const classes = useStyles();
  const {enqueueSnackbar} = useSnackbar();

  const [allInvoices, setAllInvoices] = useState(undefined as undefined | BasicInvoice[]);

  useEffect(() => {
    setAllInvoices(undefined);
    fetch(`/api/invoice/${archived ? "archived" : "all"}`)
    .then(response => response.text())
    .then(JSON.parse)
    .then((invoices: BasicInvoice[]) => invoices.map(invoice => ({
      ...invoice,
      invoiceDate: FormatDate(new Date(invoice.invoiceDate))
    })))
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
              ? <CardHeader title={"Empty!"}
                            subheader={`Looks like there are no${archived ? " archived" : ""} invoices`}/>
              : <FixedSizeList height={window.innerHeight - theme.spacing(6)}
                               width={"100%"}
                               itemSize={80}
                               itemCount={allInvoices.length}
                               itemData={{allInvoices}}
                               className={classes.list}
                               overscanCount={10}>
                {RenderRow}
              </FixedSizeList>
          : <List className={classes.loadingList}>
            {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17].map((index) => <>
              <ListItem key={index}
                        className={classes.row + " " + (index % 2 === 1 && classes.darkRow)}>
                <ListItemText primary={<Skeleton/>}
                              secondary={<Skeleton animation="wave"/>}
                              classes={{
                                primary: classes.primaryRow,
                                secondary: classes.secondaryRow
                              }}/>
              </ListItem>
            </>)}
          </List>
      }
    </Card>
  </div>
}