import React from "react";
import useRedirect from "../../hooks/useRedirect";
import {NavLink, NavLinkProps} from "react-router-dom";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import Typography from "@material-ui/core/Typography";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";

interface ListItemLinkProps {
  icon?: React.ReactElement;
  primary: string;
  to: string;
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

export default function ListItemLink(props: ListItemLinkProps) {
  const classes = useStyles();

  const {icon, primary, to} = props;
  const {component, triggerRedirect} = useRedirect();

  const renderLink = React.useMemo(
      () =>
          React.forwardRef<any, Omit<NavLinkProps, 'to'>>((itemProps, ref) => (
              <>
                <NavLink activeClassName={classes.active}
                         onClick={() => triggerRedirect(to)}
                         to={to} ref={ref} {...itemProps} />
                {component}
              </>
          )),
      [to, component, triggerRedirect, classes.active],
  );

  return (
      <li>
        <ListItem className={classes.nonActive} button component={renderLink}>
          {icon ? <ListItemIcon>{icon}</ListItemIcon> : null}
          <ListItemText primary={<Typography>{primary}</Typography>}/>
        </ListItem>
      </li>
  );
}