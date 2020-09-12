import React, {useMemo} from "react";
import {Grid} from "@material-ui/core";
import {Paper} from "@material-ui/core";
import {Button} from "@material-ui/core";
import {Theme} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      root: {
        flexGrow: 1,
        height: "100vh",
        overflowY: "hidden"
      },
      menuButton: {
        marginRight: theme.spacing(2),
      },
      title: {
        flexGrow: 1,
      },
      main: {
        flexGrow: 1,
        height: "100%"
      }
    }),
);

export default function LoginPage() {
  const classes = useStyles();

  const href = useMemo(() => {
    let port = (window.location.port ? ':' + window.location.port : '');
    // handle localhost dev case
    if (port === ':3000') {
      port = ':8080';
    }
    return window.location.protocol + '//' + window.location.hostname + port + '/oauth2/authorization/google';
  }, []);

  return <div className={classes.root}>
    <Grid className={classes.main}
          container
          direction="column"
          justify="center"
          alignContent="center"
          alignItems="center"
    >
      <Paper>
        <Button variant="contained" color="primary" onClick={() => window.location.href = href}
                className="content-rounded-border-box content-inner">
          Log in with Google (OAuth2)
        </Button>
      </Paper>
    </Grid>
  </div>;
}