import React from "react";
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import CreatePage from "./CreatePage";
import ViewPage from "./ViewPage";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import LoginPage from "./LoginPage";
import LogoutPage from "./LogoutPage";
import AuthRoute from "./main/AuthRoute";
import ErrorPage from "./ErrorPage";
import ErrorBoundary from "./common/ErrorBoundary";
import ViewAllPage from "./ViewAllPage";

const useStyles = makeStyles(() =>
    createStyles({
      root: {
        flexGrow: 1,
        height: "100vh",
        overflowY: "hidden"
      },
      main: {
        flexGrow: 1,
        height: "100%"
      },
    }),
);

export default function MainPage() {
  const classes = useStyles();

  return <MainPageErrorBoundary>
    <div className={classes.root}>
      <BrowserRouter>
        <Switch>
          <AuthRoute path="/all"><ViewAllPage/></AuthRoute>
          <AuthRoute path="/archived"><ViewAllPage archived/></AuthRoute>
          <AuthRoute path="/new"><CreatePage/></AuthRoute>
          <AuthRoute path="/logout"><LogoutPage/></AuthRoute>
          <Route path="/view/:invoiceId"><ViewPage/></Route>
          <Route path="/login"><LoginPage/></Route>
          <Route path="/error"><ErrorPage/></Route>
          <Route path="/"><Redirect to={"/all"}/></Route>
        </Switch>
      </BrowserRouter>
    </div>
  </MainPageErrorBoundary>
}

function MainPageErrorBoundary({children}: React.PropsWithChildren<{}>) {
  return <ErrorBoundary renderError={<ErrorPage
      message={"App failed to load."}
      hideButton
      fullPage
  />}>
    {children}
  </ErrorBoundary>
}