import React, {lazy, Suspense} from "react";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import ErrorBoundary from "./common/ErrorBoundary";
import SuspenseRoute from "./main/SuspenseRoute";
import HomePage from "./HomePage";
import {LoggedInContext} from "../utils/Providers";
import useLoggedIn from "../hooks/useLoggedIn";

const CreatePage = lazy(() => import("./CreatePage"));
const ViewPage = lazy(() => import("./ViewPage"));
const LoginPage = lazy(() => import("./LoginPage"));
const PrivacyPolicyPage = lazy(() => import("./PrivacyPolicyPage"));
const LogoutPage = lazy(() => import("./LogoutPage"));
const ErrorPage = lazy(() => import("./ErrorPage"));
const ViewAllPage = lazy(() => import("./ViewAllPage"));
const NotFoundPage = lazy(() => import ("./NotFoundPage"));

const useStyles = makeStyles(() =>
    createStyles({
      root: {
        flexGrow: 1,
        height: window.innerHeight,
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
  const loggedIn = useLoggedIn();

  return <MainPageErrorBoundary>
    <div className={classes.root}>
      <LoggedInContext.Provider value={loggedIn}>
        <BrowserRouter>
          <Switch>
            <SuspenseRoute auth path="/all"><ViewAllPage/></SuspenseRoute>
            <SuspenseRoute auth path="/archived"><ViewAllPage archived/></SuspenseRoute>
            <SuspenseRoute auth path="/new"><CreatePage/></SuspenseRoute>
            <SuspenseRoute auth path="/logout"><LogoutPage/></SuspenseRoute>
            <SuspenseRoute auth path="/view/:invoiceId"><ViewPage/></SuspenseRoute>
            <SuspenseRoute path="/login"><LoginPage/></SuspenseRoute>
            <SuspenseRoute path="/privacy"><PrivacyPolicyPage/></SuspenseRoute>
            <SuspenseRoute path="/error"><ErrorPage/></SuspenseRoute>
            <Route exact path="/"><HomePage/></Route>
            <SuspenseRoute auth path="/:invalidPath"><NotFoundPage/></SuspenseRoute>
          </Switch>
        </BrowserRouter>
      </LoggedInContext.Provider>
    </div>
  </MainPageErrorBoundary>
}

function MainPageErrorBoundary({children}: React.PropsWithChildren<{}>) {
  return <ErrorBoundary renderError={<Suspense fallback={<></>}>
    <ErrorPage
        message={"App failed to load."}
        hideButton
        fullPage
    />
  </Suspense>}>
    {children}
  </ErrorBoundary>
}