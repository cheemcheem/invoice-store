import Toolbar from "@material-ui/core/Toolbar";
import {Route, Switch} from "react-router-dom";
import Typography from "@material-ui/core/Typography";
import * as MuiAppBar from "@material-ui/core/AppBar";
import React, {ReactNode} from "react";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import MainPageDrawer from "./appBar/MainPageDrawer";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      title: {
        flexGrow: 1,
      },
    })
)
export type MainPageAppBarProps = {
  title: string,
  buttons?: ReactNode
}

export default function AppBar({title, buttons}: MainPageAppBarProps) {
  const classes = useStyles();

  return <>
    <MuiAppBar.default position="static">
      <Toolbar>
        <Switch>
          <Route path={["/error", "/login", "/logout"]}/>
          <Route><MainPageDrawer/></Route>
        </Switch>
        <Typography variant="h6" className={classes.title}>
          {title}
        </Typography>
        {buttons}
      </Toolbar>
    </MuiAppBar.default>
  </>;
}