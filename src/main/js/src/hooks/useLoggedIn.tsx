import {useQuery} from "@apollo/client";
import {GET_USER} from "../utils/Queries";
import {User} from "../utils/Types";
import {useEffect} from "react";

export default function useLoggedIn() {
  const {data, loading, error, refetch} = useQuery<{ user: User }>(GET_USER, {
    fetchPolicy: "cache-first"
  });

  let loggedIn: "not checked" | boolean;
  let userId = undefined;
  if (error) {
    loggedIn = false;
  } else if (loading) {
    loggedIn = "not checked";
  } else {
    loggedIn = true;
    userId = data!.user.id;
  }

  useEffect(() => {
    // this keeps the logged in status updated on page refresh
    refetch();
  }, [refetch]);

  return {loggedIn, refetch, userId};

}