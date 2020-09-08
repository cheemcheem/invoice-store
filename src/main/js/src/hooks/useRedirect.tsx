import React, {useCallback, useState} from "react";
import {Redirect} from "react-router-dom";

export default function useRedirect(to: string) {
  const [component, setComponent] = useState(<></>)
  const triggerRedirect = useCallback(() => {
    setComponent(<Redirect to={to}/>);
  }, [setComponent]);

  return {component, triggerRedirect}
}