import React from "react";
import {useContext} from "react";
import {Redirect, Route} from "react-router-dom";
import ErrorBoundary from "../common/ErrorBoundary";
import {LoggedInContext} from "../../utils/Providers";

export default function AuthRoute({children, path}: React.PropsWithChildren<{ path: string }>) {
  const {loggedIn} = useContext(LoggedInContext);

  return <AuthRouteErrorBoundary>
    <Route path={path}>
      {loggedIn ? children : <Redirect to="/login"/>}
    </Route>
  </AuthRouteErrorBoundary>;
}

function AuthRouteErrorBoundary({children}: React.PropsWithChildren<{}>) {
  return <ErrorBoundary renderError={<Redirect to={"/error"}/>}>
    {children}
  </ErrorBoundary>
}