import React from "react";
import AddIcon from "@material-ui/icons/Add";
import useRedirect from "../../hooks/useRedirect";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      menuButton: {
        marginLeft: theme.spacing(2),
        width: 100,
        marginRight: -theme.spacing(1),
      },
    })
)

export default function AppBarCreateButton() {
  const classes = useStyles();
  const {component, triggerRedirect} = useRedirect();
  return <>
    <Button color="secondary"
            size="small"
            variant="contained"
            aria-label="menu"
            className={classes.menuButton}
            onClick={() => triggerRedirect("/new")}
            endIcon={<AddIcon/>}>New</Button>
    {component}
  </>
}