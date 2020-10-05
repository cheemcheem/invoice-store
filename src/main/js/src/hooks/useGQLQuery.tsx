import {useEffect} from "react";
import {useState} from "react";
import Cookies from "js-cookie";

export default function useGQLQuery(query: string) {
  const [result, setResult] = useState({});

  useEffect(() => {
    let isMounted = true;

    fetch("/api/graphql", {
      method: "POST",
      keepalive: true,
      headers: {
        'Content-Type': 'application/json',
        'X-XSRF-TOKEN': Cookies.get("XSRF-TOKEN")!
      },
      body: query
    })
    .then(response => response.text())
    .then(JSON.parse)
    .then(value => isMounted && setResult(value.data));

    return () => {
      isMounted = false
    };
  }, [query, setResult]);

  return result;
}