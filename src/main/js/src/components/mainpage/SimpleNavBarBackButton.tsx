import React from "react";
import useRedirect from "../../hooks/useRedirect";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import ArrowBackOutlinedIcon from '@material-ui/icons/ArrowBackOutlined';
import { useLocation } from "react-router-dom";
import {useState} from "react";
import {useParams} from "react-router-dom";
import useInvoice from "../../hooks/useInvoice";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      menuButton: {
        marginRight: theme.spacing(1),
      },
    })
)

export default function SimpleNavBarBackButton() {
  const classes = useStyles();
  const target ={path: "/all", name: "All"};
  const {component, triggerRedirect} = useRedirect();

  return <>
    <Button color="default"
            size="small"
            variant="contained"
            aria-label="menu"
            className={classes.menuButton}
            onClick={() => triggerRedirect(target.path)}
            startIcon={<ArrowBackOutlinedIcon/>}>
      {target.name}
    </Button>
    {component}
  </>
}