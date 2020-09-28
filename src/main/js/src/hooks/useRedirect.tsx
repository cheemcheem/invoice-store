import React, {useMemo, useState} from "react";
import {Redirect} from "react-router-dom";

export default function useRedirect() {
  const [component, setComponent] = useState(<></>)

  const triggerRedirect = useMemo(
      () =>
          (to: string) =>
              setComponent(<Redirect push to={to}/>)
      , [setComponent]);

  return {component, triggerRedirect}
}