import React from "react";
import {WithCreateButton} from "./WithCreateButton";
import WithAuth from "./WithAuth";
import {Route} from "react-router-dom";

export default function CustomRoute({children, withAuth, withCreate, path}: React.PropsWithChildren<any> & { withAuth: boolean, withCreateButton: boolean, path: string }) {
  let component = children;

  if (withCreate) {
    component = <WithCreateButton>{component}</WithCreateButton>;
  }

  if (withAuth) {
    component = <WithAuth>{component}</WithAuth>;
  }

  return <Route path={path}>{component}</Route>;
}