import {useQuery} from "@apollo/client";
import {GET_USER} from "../utils/Queries";
import {User} from "../utils/Types";

export default function useLoggedIn() {
  const {loading, error, refetch} = useQuery<{ user: User}>(GET_USER, {
    fetchPolicy: "cache-first",
    pollInterval: 10000
  });

  let loggedIn: "not checked" | boolean;

  if (error) {
    loggedIn = false;
  } else if (loading) {
    loggedIn = "not checked";
  } else {
    loggedIn = true
  }

  return {loggedIn, refetch};

}