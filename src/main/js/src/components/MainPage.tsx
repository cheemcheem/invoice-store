import React from "react";
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import ViewAllInvoices from "./ViewAllInvoices";
import NewInvoicePage from "./NewInvoicePage";
import ViewInvoicePage from "./ViewInvoicePage";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import LoginPage from "./LoginPage";
import Logout from "./Logout";
import AuthRoute from "./mainpage/AuthRoute";
import ErrorPage from "./ErrorPage";
import ErrorBoundary from "../common/ErrorBoundary";
import MainPageAppBar from "./mainpage/MainPageAppBar";

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
        <MainPageAppBar/>
        <Switch>
          <AuthRoute withAuth path="/all"><ViewAllInvoices/></AuthRoute>
          <AuthRoute withAuth path="/archived"><ViewAllInvoices archived/></AuthRoute>
          <AuthRoute withAuth path="/new"><NewInvoicePage/></AuthRoute>
          <AuthRoute withAuth path="/logout"><Logout/></AuthRoute>
          <Route path="/view/:invoiceId"><ViewInvoicePage/></Route>
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