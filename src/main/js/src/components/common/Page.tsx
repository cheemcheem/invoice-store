import React from "react";
import {MainPageAppBarProps} from "./AppBar";
import AppBar from "./AppBar";

export default function Page(props: React.PropsWithChildren<MainPageAppBarProps>) {
  return <>
    {AppBar(props)}
    {props.children}
  </>
}