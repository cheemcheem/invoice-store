import React, {useCallback, useEffect, useMemo, useState} from "react";
import {FixedSizeList, ListChildComponentProps} from 'react-window';
import {
  Card,
  CardContent,
  CardHeader,
  createStyles,
  ListItem,
  ListItemText,
  Theme,
} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      root: {
        width: '100%',
        height: '100%',
        backgroundColor: theme.palette.background.paper,
        // padding: theme.spacing(2),
        boxSizing: "border-box"
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
      }
      // padding: theme.spacing(2),

    }),
);

function RenderRow(props: ListChildComponentProps) {
  const classes = useStyles();
  const {index, style, data} = props;
  const {allInvoices, dateTimeFormat} = data;
  const [listItem, setListItem] = useState({invoiceName: "", invoiceDate: ""});
  const onClick = useCallback(() => {
    window.location.href = `/view/${allInvoices[index]}`
  }, [allInvoices, index]);
  useEffect(() => {
    fetch(`/api/invoice/details/${allInvoices[index]}`)
    .then(response => response.text())
    .then(JSON.parse)
    .then(({invoiceName, invoiceDate}) => ({
      invoiceName,
      invoiceDate: dateTimeFormat.format(new Date(invoiceDate))
    }))
    .then(setListItem)
  }, [allInvoices, setListItem, dateTimeFormat, index]);
  return (
      <ListItem button
                key={index}
                onClick={onClick}
                className={classes.row + " " + (index % 2 === 1 ? classes.darkRow : undefined)}>
        <ListItemText primary={listItem.invoiceName} secondary={listItem.invoiceDate}/>
      </ListItem>
  );
}


export default function ViewAllInvoices({archived}: { archived?: boolean }) {
  const classes = useStyles();

  const [allInvoices, setAllInvoices] = useState([] as string[]);
  const dateTimeFormat = useMemo(() => Intl.DateTimeFormat("en-GB", {
    year: 'numeric',
    month: 'numeric',
    day: 'numeric'
  }), []);
  useEffect(() => {
    fetch(`/api/invoice/${archived ? "archived" : "all"}`)
    .then(response => response.text())
    .then(JSON.parse)
    .then(setAllInvoices);
  }, [archived, setAllInvoices]);


  return <div className={classes.root}>
    {allInvoices.length === 0
        ? <Card>
          <CardContent>
            <CardHeader title={"Empty!"}
                        subheader={`Looks like there are no${archived ? " archived" : ""} invoices`}/>
          </CardContent>
        </Card>
        : <FixedSizeList height={600}
                         width={"100%"}
                         itemSize={40}
                         itemCount={allInvoices.length}
                         itemData={{allInvoices, dateTimeFormat}}
                         className={classes.list}
        >
          {RenderRow}
        </FixedSizeList>
    }
  </div>
}