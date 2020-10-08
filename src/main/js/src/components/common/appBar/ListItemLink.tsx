import React from "react";
import {NavLink, NavLinkProps} from "react-router-dom";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import Typography from "@material-ui/core/Typography";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import useRedirect from "../../../hooks/useRedirect";

interface AppDrawerItem {
  icon?: React.ReactElement;
  primary: string | React.ReactNode;
  to?: string;
}

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      nonActive: {
        color: theme.palette.grey.A700
      },
      active: {
        color: theme.palette.primary.main
      },
    }));

export default function ListItemLink(props: AppDrawerItem) {
  const classes = useStyles();
  const {icon, primary, to} = props;
  const {component, triggerRedirect} = useRedirect();


  const renderLink = React.useMemo(
      () =>
          to
              ? React.forwardRef<any, Omit<NavLinkProps, 'to'>>((itemProps, ref) => (
                  <>
                    <NavLink activeClassName={classes.active}
                             onClick={() => triggerRedirect(to)}
                             to={to} ref={ref} {...itemProps} />
                    {component}
                  </>
              )) : undefined,
      [to, component, triggerRedirect, classes.active],
  );

  return (
      <li>
        <ListItem className={classes.nonActive} button component={to ? renderLink! : "h1"}>
          {icon ? <ListItemIcon>{icon}</ListItemIcon> : null}
          <ListItemText primary={<Typography>{primary}</Typography>}/>
        </ListItem>
      </li>
  );
}