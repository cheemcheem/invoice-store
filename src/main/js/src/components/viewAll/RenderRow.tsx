import {ListChildComponentProps} from "react-window";
import {BasicInvoice} from "../../utils/Types";
import useRedirect from "../../hooks/useRedirect";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import {Theme} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";

export const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      row: {
        padding: theme.spacing(2),
        width: "100%",
        backgroundColor: theme.palette.grey["100"],
        height: 80,
      },
      darkRow: {
        backgroundColor: theme.palette.grey["300"],
        color: theme.palette.text.primary
      },
    })
);
export default function RenderRow(props: ListChildComponentProps) {
  const classes = useStyles();
  const {index, style, data} = props;
  const {allInvoices} = data as { allInvoices: BasicInvoice[] };
  const invoice = allInvoices[index];
  const {component, triggerRedirect} = useRedirect();
  return (
      <ListItem button
                style={style}
                key={index}
                onClick={() => triggerRedirect(`/view/${invoice.id}`)}
                className={classes.row + " " + (index % 2 === 1 ? classes.darkRow : undefined)}>
        <ListItemText key={index}
                      primary={invoice.name}
                      secondary={invoice.date}
        />
        {component}
      </ListItem>
  );
}