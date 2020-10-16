import React from "react";
import {ApolloQueryResult} from "@apollo/client";
import {NetworkStatus} from "@apollo/client";
import {User} from "./Types";

export const LoggedInContext = React.createContext<{
  loggedIn: "not checked" | boolean,
  refetch: () => Promise<ApolloQueryResult<{ user: User }>>
}>({
  loggedIn: "not checked",
  refetch: () => Promise.resolve({
    data: {user: {id: "", name: "", picture: ""}},
    loading: true,
    networkStatus: NetworkStatus.loading
  })
})