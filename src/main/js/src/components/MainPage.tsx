import React, {useCallback, useState} from "react";
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import ViewAllInvoices from "./ViewAllInvoices";
import NewInvoicePage from "./NewInvoicePage";
import ViewInvoicePage from "./ViewInvoicePage";
import {makeStyles} from "@material-ui/core/styles";
import {Theme} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";
import MenuIcon from '@material-ui/icons/Menu';
import AddIcon from '@material-ui/icons/Add';
import ArchiveIcon from '@material-ui/icons/Archive';
import DescriptionIcon from '@material-ui/icons/Description';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import {ListItem} from "@material-ui/core";
import {ListItemText} from "@material-ui/core";
import {SwipeableDrawer} from "@material-ui/core";
import {IconButton} from "@material-ui/core";
import {Toolbar} from "@material-ui/core";
import {AppBar} from "@material-ui/core";
import {List} from "@material-ui/core";
import {Divider} from "@material-ui/core";
import {Typography} from "@material-ui/core";
import ListItemLink from "./subcomponents/ListItemLink";
import LoginPage from "./LoginPage";
import Logout from "./Logout";
import CustomRoute from "./subcomponents/CustomRoute";

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
      drawer: {
        width: 200
      }
    }),
);


export default function MainPage() {
  const classes = useStyles();

  const [menuOpen, setMenuOpen] = useState(false);
  const toggleMenu = useCallback(() => setMenuOpen(!menuOpen), [menuOpen, setMenuOpen]);

  return <div className={classes.root}>
    <BrowserRouter>
      <AppBar position="static">
        <Toolbar>
          <IconButton edge="start"
                      className={classes.menuButton}
                      color="inherit"
                      aria-label="menu"
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
                  <ListItemLink to="/logout" primary="Logout" icon={<ExitToAppIcon/>}/>
                </List>
              </div>
            </SwipeableDrawer>
          </IconButton>
          <Typography variant="h6" className={classes.title}>
            <Switch>
              <Route path="/all">View All Invoices</Route>
              <Route path="/archived">View Archived Invoices</Route>
              <Route path="/new">Create New Invoice</Route>
              <Route path="/view/:invoiceId">View Invoice</Route>
              <Route path="/login">Login to Invoice Store</Route>
              <Route path="/logout">Logout from Invoice Store</Route>
            </Switch>
          </Typography>
        </Toolbar>
      </AppBar>

      <Switch>
        <CustomRoute withAuth withCreateButton path="/all"><ViewAllInvoices/></CustomRoute>
        <CustomRoute withCreateButton path="/archived"><ViewAllInvoices archived/></CustomRoute>
        <CustomRoute withAuth path="/new"><NewInvoicePage/></CustomRoute>
        <CustomRoute withCreateButton path="/view/:invoiceId"><ViewInvoicePage/></CustomRoute>
        <CustomRoute withAuth path="/logout"><Logout/></CustomRoute>
        <Route path="/login"><LoginPage/></Route>
        <Route path="/"><Redirect to={"/all"}/></Route>
      </Switch>
    </BrowserRouter>
  </div>
}