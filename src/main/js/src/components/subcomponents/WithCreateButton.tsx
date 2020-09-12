import React from "react";
import useRedirect from "../../hooks/useRedirect";
import Fab from "@material-ui/core/Fab/Fab";
import {createStyles} from "@material-ui/core/styles";
import {Theme} from "@material-ui/core/styles";
import {makeStyles} from "@material-ui/core/styles";
import AddIcon from "@material-ui/icons/Add";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      fab: {
        position: 'absolute',
        bottom: theme.spacing(2),
        right: theme.spacing(2),
      },
    })
);

export function WithCreateButton(props: React.PropsWithChildren<any>) {
  const classes = useStyles();

  const {component, triggerRedirect} = useRedirect();
  return <>
    {props.children}
    <Fab variant="extended" size="medium" aria-label={"Create"} className={classes.fab}
         color={"secondary"}
         onClick={() => triggerRedirect("/new")}>
      <AddIcon/>
      Create
    </Fab>
    {component}
  </>
}