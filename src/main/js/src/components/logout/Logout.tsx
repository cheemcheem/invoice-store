import {useSnackbar} from "notistack";
import {useEffect} from "react";
import {useContext} from "react";
import * as Cookie from "js-cookie";
import useRedirect from "../../hooks/useRedirect";
import {LoggedInContext} from "../../utils/Providers";

export default function Logout() {
  const {enqueueSnackbar} = useSnackbar();
  const {refetch} = useContext(LoggedInContext);
  const {component, triggerRedirect} = useRedirect();

  useEffect(() => {
    let rendered = true;
    const redirectIfRendered = (to: string) => rendered && triggerRedirect(to);
    fetch(`/logout`, {
      method: "POST",
      headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))}
    })
    .then(refetch)
    .then(() => redirectIfRendered("/login"))
    .catch(() => {
      enqueueSnackbar("Failed to log out.", {variant: "error"});
      redirectIfRendered("/all");
      return refetch();
    });
    return () => {
      rendered = false
    };
  }, [enqueueSnackbar, triggerRedirect, refetch]);

  return component;
}