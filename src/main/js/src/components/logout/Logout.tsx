import {useSnackbar} from "notistack";
import {useCallback, useEffect} from "react";
import {useContext} from "react";
import * as Cookie from "js-cookie";
import useRedirect from "../../hooks/useRedirect";
import {LoggedInContext} from "../../utils/Providers";

export default function Logout() {
  const {enqueueSnackbar} = useSnackbar();
  const {refetch} = useContext(LoggedInContext);
  const {component, triggerRedirect} = useRedirect();

  const logout = useCallback(() => {
    let rendered = true;
    fetch(`/logout`, {
      method: "POST",
      headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))}
    })
    .then(() => {
      if (rendered) {
        return refetch();
      }
    })
    .catch(() => {
      if (rendered) {
        enqueueSnackbar("Failed to log out.", {variant: "error"});
        triggerRedirect("/all")
      }
    });
    return () => {
      rendered = false
    };
  }, [enqueueSnackbar, triggerRedirect, refetch]);

  useEffect(logout, [logout]);

  return component;
}