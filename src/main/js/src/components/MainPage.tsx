import React, {useCallback, useState} from "react";
import {BrowserRouter, NavLink, NavLinkProps, Redirect, Route, Switch} from "react-router-dom";
import ViewAllInvoices from "./ViewAllInvoices";
import NewInvoicePage from "./NewInvoicePage";
import ViewInvoicePage from "./ViewInvoicePage";
import * as Cookie from "js-cookie";
import {
  AppBar,
  createStyles,
  Divider,
  Fab,
  IconButton,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  SwipeableDrawer,
  Theme,
  Toolbar,
  Typography
} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import MenuIcon from '@material-ui/icons/Menu';
import AddIcon from '@material-ui/icons/Add';
import ArchiveIcon from '@material-ui/icons/Archive';
import DescriptionIcon from '@material-ui/icons/Description';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import useRedirect from "../hooks/useRedirect";

interface ListItemLinkProps {
  icon?: React.ReactElement;
  primary: string;
  to: string;
}

function ListItemLink(props: ListItemLinkProps) {
  const {icon, primary, to} = props;
  const {component, triggerRedirect} = useRedirect(to);

  const renderLink = React.useMemo(
      () =>
          React.forwardRef<any, Omit<NavLinkProps, 'to'>>((itemProps, ref) => (
              <>
                <NavLink onClick={triggerRedirect} to={to} ref={ref} {...itemProps} />
                {component}
              </>
          )),
      [to],
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
      },
      list: {
        width: 250,
      },
      fullList: {
        width: 'auto',
      },
      fab: {
        position: 'absolute',
        bottom: theme.spacing(2),
        right: theme.spacing(2),
      },
      drawer: {
        width: 200
      }
    }),
);

function AddCreate(props: React.PropsWithChildren<any>) {
  const classes = useStyles();
  const {component, triggerRedirect} = useRedirect("/new");
  return <>
    {props.children}
    <Fab variant="extended" size="medium" aria-label={"Create"} className={classes.fab}
         color={"secondary"}
         onClick={triggerRedirect}>
      <AddIcon/>
      Create
    </Fab>
    {component}
  </>
}

export default function MainPage() {

  const logout = useCallback(
      () => fetch(`/logout`, {
        method: "POST",
        headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))}
      })
      .then(() => window.location.pathname = "/")
      , []);
  const [menuOpen, setMenuOpen] = useState(false);
  const toggleMenu = useCallback(() => setMenuOpen(!menuOpen), [menuOpen, setMenuOpen]);
  const classes = useStyles();

  return <div className={classes.root}>
    <BrowserRouter>
      <AppBar position="static">
        <Toolbar>
          <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu"
                      onClick={toggleMenu}>
            <MenuIcon/>
            <SwipeableDrawer
                open={menuOpen}
                onClose={() => setMenuOpen(false)}
                onOpen={() => setMenuOpen(true)}
                className={classes.drawer}
                ModalProps={{keepMounted: true}}
            >
              <div role="presentation">
                <List>
                  <ListItem key="Title">
                    <ListItemText primary="Pages"/>
                  </ListItem>
                </List>
                <Divider/>
                <List>
                  <ListItemLink to="/all" primary="Invoices" icon={<DescriptionIcon/>}/>
                  <ListItemLink to="/archived" primary="Archive" icon={<ArchiveIcon/>}/>
                  <ListItemLink to="/new" primary="Create" icon={<AddIcon/>}/>
                </List>
                <Divider variant={"fullWidth"}/>
                <List>
                  <ListItem key="LogOut" onClick={logout}>
                    <ListItemIcon><ExitToAppIcon/></ListItemIcon>
                    <ListItemText primary="Logout"/>
                  </ListItem>
                </List>
              </div>
            </SwipeableDrawer>
          </IconButton>
          <Typography variant="h6" className={classes.title}>
            <Switch>
              <Route path="/all">Invoices</Route>
              <Route path="/archived">Archive</Route>
              <Route path="/new">Create</Route>
              <Route path="/view/:invoiceId">View</Route>
            </Switch>
          </Typography>
        </Toolbar>
      </AppBar>

      <Switch>
        <Route path="/all"><AddCreate><ViewAllInvoices/></AddCreate></Route>
        <Route path="/archived"><AddCreate><ViewAllInvoices archived/></AddCreate></Route>
        <Route path="/new"><NewInvoicePage/></Route>
        <Route path="/view/:invoiceId"><AddCreate><ViewInvoicePage/></AddCreate></Route>
        <Route path="/"><Redirect to={"/all"}/></Route>
      </Switch>
    </BrowserRouter>
  </div>
}