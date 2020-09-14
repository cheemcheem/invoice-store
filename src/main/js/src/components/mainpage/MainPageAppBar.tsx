import Toolbar from "@material-ui/core/Toolbar";
import {Route, Switch} from "react-router-dom";
import Typography from "@material-ui/core/Typography";
import AppBar from "@material-ui/core/AppBar";
import React from "react";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import MainPageDrawer from "./MainPageDrawer";
import NavBarCreateButton from "./NavBarCreateButton";
import NavBarBackButton from "./NavBarBackButton";
import SimpleNavBarBackButton from "./SimpleNavBarBackButton";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      title: {
        flexGrow: 1,
      },
    })
)

export default function MainPageAppBar() {
  const classes = useStyles();

  return <>
    <AppBar position="static">
      <Toolbar>
        <Switch>
          <Route path={["/error", "/login", "/logout"]}/>
          <Route><MainPageDrawer/></Route>
        </Switch>
        <Typography variant="h6" className={classes.title}>
          <Switch>
            <Route path="/all">View All Invoices</Route>
            <Route path="/archived">View Archived Invoices</Route>
            <Route path="/new">Create New Invoice</Route>
            <Route path="/view">View Invoice</Route>
            <Route path="/login">Login to Invoice Store</Route>
            <Route path="/logout">Logout from Invoice Store</Route>
            <Route path="/error">Invoice Store</Route>
          </Switch>
        </Typography>
        <Switch>
          <Route path={["/create"]}><SimpleNavBarBackButton/></Route>
          <Route path={["/view/:invoiceId"]}><NavBarBackButton/></Route>
        </Switch>
        <Switch>
          <Route path={["/error", "/login", "/logout", "/new", "/archived"]}/>
          <Route><NavBarCreateButton/></Route>
        </Switch>
      </Toolbar>
    </AppBar>
  </>;
}