import React from "react";
import WithAuth from "./WithAuth";
import {Redirect, Route} from "react-router-dom";
import ErrorBoundary from "../../common/ErrorBoundary";

export default function AuthRoute({children, withAuth, path}: React.PropsWithChildren<{ withAuth?: boolean, path: string }>) {
  let component = children;

  if (withAuth) {
    component = <WithAuth>{component}</WithAuth>;
  }

  return <AuthRouteErrorBoundary>
    <Route path={path}>{component}</Route>
  </AuthRouteErrorBoundary>;
}

function AuthRouteErrorBoundary({children}: React.PropsWithChildren<{}>) {
  return <ErrorBoundary renderError={<Redirect to={"/error"}/>}>
    {children}
  </ErrorBoundary>
}