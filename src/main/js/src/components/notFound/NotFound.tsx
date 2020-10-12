import React from "react";
import {Typography} from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import {Theme} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme: Theme) => createStyles({
  grid: {
    height: "100%",
    padding: theme.spacing(2),
    paddingTop: theme.spacing(4),
    paddingBottom: theme.spacing(4)
  },
  message: {
    marginTop: -theme.spacing(8)
  },
}));


export default function NotFound() {
  const classes = useStyles();
  return <>
    <Grid className={classes.grid}
          container
          direction="column"
          justify="center"
          alignContent="center">
      <Typography className={classes.message} variant="h5" color="primary"
                  title="Not Found Message">
        This page does not exist!
      </Typography>
    </Grid>
  </>;
}