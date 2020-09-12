import {useSnackbar} from "notistack";
import {useCallback, useEffect} from "react";
import * as Cookie from "js-cookie";
import useRedirect from "../hooks/useRedirect";

export default function Logout() {
  const {enqueueSnackbar} = useSnackbar();
  let {component, triggerRedirect} = useRedirect();

  const logout = useCallback(() => {
    fetch(`/logout`, {
      method: "POST",
      headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))}
    })
    .then(() => {
      triggerRedirect("/login")
    })
    .catch(() => {
      enqueueSnackbar("Failed to log out.", {variant: "error"});
      triggerRedirect("/all")
    })
  }, [enqueueSnackbar, triggerRedirect]);

  useEffect(logout, [logout]);

  return component;
}