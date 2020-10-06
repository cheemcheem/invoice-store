import React from 'react';
import MainPage from "./components/MainPage";
import Box from "@material-ui/core/Box";
import {SnackbarProvider} from 'notistack';
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import {ApolloClient, InMemoryCache} from '@apollo/client';
import {ApolloProvider} from "@apollo/client";
import {createHttpLink} from "@apollo/client";
import {setContext} from "@apollo/client/link/context";
import Cookies from "js-cookie";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      snackbar: {
        marginTop: theme.spacing(6)
      },
    })
)

const httpLink = createHttpLink({
  uri: '/api/graphql',
});

const authLink = setContext((_, { headers }) => {
  const token = Cookies.get("XSRF-TOKEN");
  return {
    headers: {
      ...headers,
      'X-XSRF-TOKEN': token
    }
  }
});


const client = new ApolloClient({
  link: authLink.concat(httpLink),
  cache: new InMemoryCache()
});

export default function App() {
  const classes = useStyles();
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