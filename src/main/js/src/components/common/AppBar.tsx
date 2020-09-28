import Toolbar from "@material-ui/core/Toolbar";
import {Route, Switch} from "react-router-dom";
import Typography from "@material-ui/core/Typography";
import * as MuiAppBar from "@material-ui/core/AppBar";
import React, {ReactNode} from "react";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import MainPageDrawer from "./appBar/MainPageDrawer";
import useLoggedIn from "../../hooks/useLoggedIn";

const useStyles = makeStyles(() =>
    createStyles({
      title: {
        flexGrow: 1,
        whiteSpace: "nowrap",
        overflow: "hidden",
        textOverflow: "ellipsis"
      },
    })
)
export type MainPageAppBarProps = {
  title: string,
  buttons?: ReactNode
}

export default function AppBar({title, buttons}: MainPageAppBarProps) {
  const classes = useStyles();
  const loggedIn = useLoggedIn();
  return <>
    <MuiAppBar.default position="static">
      <Toolbar>
        <Switch>
          <Route path={["/error", "/logout"]}/>
          <Route><MainPageDrawer loggedIn={loggedIn === "not checked" ? false : loggedIn}/></Route>
        </Switch>
        <Typography variant="h6" className={classes.title}>
          {title}
        </Typography>
        {buttons}
      </Toolbar>
    </MuiAppBar.default>
  </>;
}