import React, {lazy, Suspense} from "react";
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import ErrorBoundary from "./common/ErrorBoundary";
import AuthRoute from "./main/AuthRoute";
import {useMemo} from "react";
import Page from "./common/Page";

const CreatePage = lazy(() => import("./CreatePage"));
const ViewPage = lazy(() => import("./ViewPage"));
const LoginPage = lazy(() => import("./LoginPage"));
const LogoutPage = lazy(() => import("./LogoutPage"));
const ErrorPage = lazy(() => import("./ErrorPage"));
const ViewAllPage = lazy(() => import("./ViewAllPage"));

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
  const fallback = useMemo(() => <Page title={""}/>, [])
  return <MainPageErrorBoundary>
    <div className={classes.root}>
      <BrowserRouter>
        <Switch>
          <AuthRoute path="/all"><Suspense fallback={fallback}><ViewAllPage/></Suspense></AuthRoute>
          <AuthRoute path="/archived"><Suspense fallback={fallback}><ViewAllPage archived/></Suspense></AuthRoute>
          <AuthRoute path="/new"><Suspense fallback={fallback}><CreatePage/></Suspense></AuthRoute>
          <AuthRoute path="/logout"><Suspense fallback={fallback}><LogoutPage/></Suspense></AuthRoute>
          <Route path="/view/:invoiceId"><Suspense fallback={fallback}><ViewPage/></Suspense></Route>
          <Route path="/login"><Suspense fallback={fallback}><LoginPage/></Suspense></Route>
          <Route path="/error"><Suspense fallback={fallback}><ErrorPage/></Suspense></Route>
          <Route path="/"><Redirect to={"/all"}/></Route>
        </Switch>
      </BrowserRouter>
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