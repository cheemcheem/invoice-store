import React, {useMemo} from "react";
import {useCallback, useContext} from "react";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import {Skeleton} from "@material-ui/lab";
import GoogleButton from 'react-google-button'
import {LoggedInContext} from "../../utils/Providers";
import ListItemLink from "../common/appBar/ListItemLink";
import InfoIcon from "@material-ui/icons/Info";

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
      group: {
        height: "100%",
        display: "flex",
        flexDirection: "column",

      },
      groupInner: {
        height: (window.innerHeight - theme.spacing(10))/2,
        display: "flex",
        flexDirection: "column",
        justifyContent: "flex-end"
      },
      paper: {
        height: "auto",
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

  const logInAndRefresh = useCallback(() => {
    window.location.href = href;
  }, [href]);

  return <div className={classes.root}>
    <Grid className={classes.main}
          container
          direction="column"
          justify="center"
          alignContent="center"
          alignItems="center">
      <div className={classes.group}>
        <div className={classes.groupInner}>
          <Paper className={classes.paper}>
            {(loggedIn === false)
                ? <GoogleButton onClick={logInAndRefresh}
                                className={classes.button}>Log in with Google
                  (OAuth2)</GoogleButton>
                : <Skeleton className={classes.button} variant="rect" animation="wave"/>
            }
          </Paper>
        </div>
        <div className={classes.groupInner}>
          <Paper className={classes.paper}>
            <ListItemLink to="/privacy" primary="Privacy Policy" icon={<InfoIcon/>} nonList/>
          </Paper>
        </div>
      </div>
    </Grid>
  </div>;
}