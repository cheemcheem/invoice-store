import React, {useMemo} from "react";
import {useContext} from "react";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import {Skeleton} from "@material-ui/lab";
import {Redirect} from "react-router-dom";
import GoogleButton from 'react-google-button'
import {LoggedInContext} from "../../utils/Providers";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      root: {
        flexGrow: 1,
        height: "100vh",
        overflowY: "hidden",
      },
      button: {
        backgroundColor: theme.palette.primary.main + "!important",
        width: window.innerWidth - theme.spacing(4),
      },
      title: {
        flexGrow: 1,
      },
      paper: {
        marginTop: -theme.spacing(16)
      },
      main: {
        flexGrow: 1,
        height: "100%",
      }
    }),
);

export default function Login() {
  const classes = useStyles();
  const {loggedIn} = useContext(LoggedInContext);

  const href = useMemo(() => {
    let port = (window.location.port ? ':' + window.location.port : '');
    // handle localhost dev case
    if (port === ':3000') {
      port = ':8080';
    }
    return window.location.protocol + '//' + window.location.hostname + port + '/oauth2/authorization/google';
  }, []);

  if (loggedIn === true) {
    return <Redirect to="/"/>
  }

  return <div className={classes.root}>
    <Grid className={classes.main}
          container
          direction="column"
          justify="center"
          alignContent="center"
          alignItems="center">
      <Paper className={classes.paper}>
        {(loggedIn === false)
            ? <GoogleButton onClick={() => window.location.href = href}
                            className={classes.button}>Log in with Google (OAuth2)</GoogleButton>
            : <Skeleton className={classes.button} variant="rect" animation="wave"/>
        }
      </Paper>
    </Grid>
  </div>;
}