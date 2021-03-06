import React from "react";
import useRedirect from "../../hooks/useRedirect";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import ArrowBackOutlinedIcon from '@material-ui/icons/ArrowBackOutlined';
import Skeleton from "@material-ui/lab/Skeleton";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      menuButton: {
        marginRight: -theme.spacing(1),
        width: 100,
      },
      skeleton: {
        background: theme.palette.primary.light,
        borderRadius: theme.shape.borderRadius,
        width: theme.spacing(12.5),
        height: theme.spacing(4.5)
      }
    })
)
type NavBarBackButtonProps = {
  archived: boolean | undefined
}
export default function AppBarBackButton({archived}: NavBarBackButtonProps) {
  const classes = useStyles();
  const {component, triggerRedirect} = useRedirect();

  return <>
    {archived === undefined
        ? <Skeleton className={classes.skeleton + " " + classes.menuButton} variant="rect"/>
        : <Button color="default"
                  size="medium"
                  variant="contained"
                  aria-label="menu"
                  className={classes.menuButton}
                  onClick={() => triggerRedirect(archived ? "/archived" : "/all")}
                  startIcon={<ArrowBackOutlinedIcon/>}>
          {archived ? "Archive" : "All"}
        </Button>}
    {component}
  </>
}