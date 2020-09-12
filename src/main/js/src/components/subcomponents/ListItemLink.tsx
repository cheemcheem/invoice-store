import React from "react";
import useRedirect from "../../hooks/useRedirect";
import {NavLinkProps} from "react-router-dom";
import {NavLink} from "react-router-dom";
import {ListItem} from "@material-ui/core";
import {ListItemIcon} from "@material-ui/core";
import {ListItemText} from "@material-ui/core";

interface ListItemLinkProps {
  icon?: React.ReactElement;
  primary: string;
  to: string;
}

export default function ListItemLink(props: ListItemLinkProps) {
  const {icon, primary, to} = props;
  const {component, triggerRedirect} = useRedirect();

  const renderLink = React.useMemo(
      () =>
          React.forwardRef<any, Omit<NavLinkProps, 'to'>>((itemProps, ref) => (
              <>
                <NavLink onClick={() => triggerRedirect(to)} to={to} ref={ref} {...itemProps} />
                {component}
              </>
          )),
      [to, component, triggerRedirect],
  );

  return (
      <li>
        <ListItem button component={renderLink}>
          {icon ? <ListItemIcon>{icon}</ListItemIcon> : null}
          <ListItemText primary={primary}/>
        </ListItem>
      </li>
  );
}