import {useState} from "react";
import {useCallback} from "react";
import {useEffect} from "react";

export default function useLoggedIn() {
  const [loggedIn, setIsLoggedIn] = useState("not checked" as "not checked" | boolean);

  const checkLoggedIn = useCallback(() => {
    fetch("/api/user", {keepalive: true})
    .then(response => {
      if (response.redirected) {
        return setIsLoggedIn(false);
      }
      if (response.headers.has('Content-Length')
          && Number(response.headers.get('Content-Length')) === 0) {
        return setIsLoggedIn(false);
      }

      return setIsLoggedIn(true);

    })
    .catch(() => setIsLoggedIn(false))
  }, [setIsLoggedIn]);

  useEffect(checkLoggedIn, [checkLoggedIn]);

  return loggedIn;
}