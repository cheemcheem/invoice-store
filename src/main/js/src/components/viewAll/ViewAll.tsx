import React from "react";
import {FixedSizeList} from 'react-window';
import {createStyles, makeStyles, Theme, useTheme} from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardHeader from "@material-ui/core/CardHeader";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Skeleton from "@material-ui/lab/Skeleton";
import List from "@material-ui/core/List";
import {BasicInvoice} from "../../utils/Types";
import RenderRow, {useStyles as useRenderRowStyles} from "../viewAll/RenderRow";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      root: {
        width: '100%',
        height: '100%',
        backgroundColor: theme.palette.background.paper,
      },
      primaryRow: {
        width: 160
      },
      secondaryRow: {
        width: "100%"
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

export type ViewAllProps = {
  archived?: boolean,
  allInvoices: BasicInvoice[],
  loading: boolean,
};

export default function ViewAll({archived, allInvoices, loading}: ViewAllProps) {
  const classes = useStyles();
  const rowClasses = useRenderRowStyles();
  const theme = useTheme();

  return <div className={classes.root}>
    <Card>
      {!loading
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
                        className={rowClasses.row + " " + (index % 2 === 1 && rowClasses.darkRow)}>
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