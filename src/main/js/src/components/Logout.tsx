import {useSnackbar} from "notistack";
import {useCallback} from "react";
import React from "react";
import {useEffect} from "react";
import * as Cookie from "js-cookie";

export default function Logout() {
  const {enqueueSnackbar} = useSnackbar();

  const logout = useCallback(() => {
    fetch(`/logout`, {
      method: "POST",
      headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))}
    })
    .then(() => {
      enqueueSnackbar("Logged out.", {variant: "info"});
      window.location.reload()
    })
    .catch(() => {
      // enqueueSnackbar("Failed to log out.", {variant: "error"});
      window.location.reload()
    })
  }, [enqueueSnackbar]);

  useEffect(logout, [logout]);

  return <></>;
}