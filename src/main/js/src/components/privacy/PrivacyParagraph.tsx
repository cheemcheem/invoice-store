import React from "react";
import Card from "@material-ui/core/Card";
import CardHeader from "@material-ui/core/CardHeader";
import CardContent from "@material-ui/core/CardContent";
import {makeStyles} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";

const useStyles = makeStyles(() =>
    createStyles({
      card: {
        width: "100%"
      }
    }),
);
export default function PrivacyParagraph({title, children}: React.PropsWithChildren<{ title: string }>) {
  const classes = useStyles();
  return <Card id={title} key={title} variant="outlined" className={classes.card}>
    <CardHeader title={title}/>
    {
      Array.isArray(children)
          ? children.map((paragraph, index) => <CardContent
              key={String(index)}>{paragraph}</CardContent>)
          : <CardContent key={String(children)}>{children}</CardContent>
    }
  </Card>
}