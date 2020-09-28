import React, {Suspense, useMemo} from "react";
import Page from "../common/Page";
import AuthRoute from "./AuthRoute";
import {Route} from "react-router-dom";

export default function SuspenseRoute({auth, path, children}: React.PropsWithChildren<{ auth?: boolean, path: string }>) {
  const fallback = useMemo(() => <Page title={""}/>, [])
  if (auth) {
    return <AuthRoute path={path}><Suspense fallback={fallback}>{children}</Suspense></AuthRoute>
  }
  return <Route path={path}><Suspense fallback={fallback}>{children}</Suspense></Route>
}
