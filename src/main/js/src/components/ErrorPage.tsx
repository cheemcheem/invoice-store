import Container from "@material-ui/core/Container";
import Typography from "@material-ui/core/Typography";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import CardHeader from "@material-ui/core/CardHeader";
import CardActions from "@material-ui/core/CardActions";
import React from "react";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import BackspaceIcon from '@material-ui/icons/Backspace';
import useRedirect from "../hooks/useRedirect";

const useStyles = makeStyles(() =>
    createStyles({
      root: {
        height: "100%",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center"
      },
      card: {
        width: "100%",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center"
      }
    }),
);

export default function ErrorPage({message, hideButton, fullPage}: { message?: string, hideButton?: boolean, fullPage?: boolean }) {
  const classes = useStyles();
  const {component, triggerRedirect} = useRedirect();
  return <Container className={classes.root} style={fullPage ? {height: "100vh"} : undefined}>
    <Card className={classes.card}>
      <CardHeader title={<Typography variant="h5" color="error">Error Page</Typography>}/>
      <CardContent>
        <Typography variant="body1" color="error">
          {message || "Something has gone wrong"}
        </Typography>
      </CardContent>
      <CardActions>
        {!hideButton && <>
          <Button variant="outlined"
                  startIcon={<BackspaceIcon/>}
                  color="primary"
                  onClick={() => triggerRedirect("/")}>
            Return
          </Button>
        </>}
      </CardActions>
    </Card>
    {component}
  </Container>
}