import React from "react";

export const LoggedInContext = React.createContext({
  loggedIn: "not checked" as "not checked" | boolean,
  refetch: () => {
  }
})