import IconButton from "@material-ui/core/IconButton";
import React from "react";
import AddIcon from "@material-ui/icons/Add";
import useRedirect from "../../hooks/useRedirect";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      menuButton: {
        marginRight: -theme.spacing(1),
      },
    })
)

export default function NavBarCreateButton() {
  const classes = useStyles();
  const {component, triggerRedirect} = useRedirect();
  return <>
    <IconButton edge="start"
                className={classes.menuButton}
                color="inherit"
                aria-label="menu"
                onClick={() => triggerRedirect("/new")}>
      <AddIcon/>
    </IconButton>
    {component}
  </>
}