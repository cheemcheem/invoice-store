import React from "react";
import useRedirect from "../../hooks/useRedirect";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import ArrowBackOutlinedIcon from '@material-ui/icons/ArrowBackOutlined';
import {useParams} from "react-router-dom";
import useInvoice from "../../hooks/useInvoice";
import {Skeleton} from "@material-ui/lab";
import {useTheme} from "@material-ui/core/styles";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      menuButton: {
        marginRight: theme.spacing(1),
      },
    })
)

export default function NavBarBackButton() {
  const classes = useStyles();
  const {component, triggerRedirect} = useRedirect();
  const theme = useTheme();
  const {invoiceId} = useParams();
  const invoice = useInvoice(invoiceId);

  return <>
    {invoice === undefined
        ? <Skeleton width={theme.spacing(4)} variant="rect"/>
        : <Button color="default"
                  size="small"
                  variant="contained"
                  aria-label="menu"
                  className={classes.menuButton}
                  onClick={() => triggerRedirect(invoice?.invoiceArchived ? "/archived" : "/all")}
                  startIcon={<ArrowBackOutlinedIcon/>}>
          {invoice.invoiceArchived ? "Archive" : "All"}
        </Button>}
    {component}
  </>
}