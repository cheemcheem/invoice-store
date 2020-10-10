import React from 'react';
import MainPage from "./components/MainPage";
import Box from "@material-ui/core/Box";
import {SnackbarProvider} from 'notistack';
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import {ApolloClient, ApolloProvider, createHttpLink, InMemoryCache} from "@apollo/client";
import {setContext} from "@apollo/client/link/context";
import Cookies from "js-cookie";
import {persistCache} from 'apollo3-cache-persist';
import {useState} from "react";
import {useEffect} from "react";
import {PersistentStorage} from "apollo3-cache-persist";
import {PersistedData} from "apollo3-cache-persist/lib/types";
import {NormalizedCacheObject} from "@apollo/client";
import {Skeleton} from "@material-ui/lab";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      snackbar: {
        marginTop: theme.spacing(6)
      },
    })
)


export default function App() {
  const classes = useStyles();
  const [client, setClient] = useState();

  useEffect(() => {
    const httpLink = createHttpLink({
      uri: '/api/graphql',
    });

    const authLink = setContext((_, {headers}) => {
      const token = Cookies.get("XSRF-TOKEN");
      return {
        headers: {
          ...headers,
          'X-XSRF-TOKEN': token
        }
      }
    });

    const cache = new InMemoryCache();

    persistCache({
      cache,
      storage: window.localStorage as PersistentStorage<PersistedData<NormalizedCacheObject>>,
    }).then(() => {
      setClient(new ApolloClient({
        link: authLink.concat(httpLink),
        cache
      }));
    });
  }, [setClient])

  if (!client) {
    return <Skeleton/>
  }

  return <>
    <SnackbarProvider maxSnack={3}
                      className={classes.snackbar}
                      autoHideDuration={1000}
                      anchorOrigin={{vertical: 'top', horizontal: 'center'}}>
      <ApolloProvider client={client}>
        <Box>
          <MainPage/>
        </Box>
      </ApolloProvider>
    </SnackbarProvider>
  </>;
}