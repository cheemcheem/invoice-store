import React from "react";
import {WithCreateButton} from "./WithCreateButton";
import WithAuth from "./WithAuth";
import {Route} from "react-router-dom";

export default function CustomRoute({children, withAuth, withCreateButton, path}: React.PropsWithChildren<{ withAuth?: boolean, withCreateButton?: boolean, path: string }>) {
  let component = children;

  if (withCreateButton) {
    component = <WithCreateButton>{component}</WithCreateButton>;
  }

  if (withAuth) {
    component = <WithAuth>{component}</WithAuth>;
  }

  return <Route path={path}>{component}</Route>;
}