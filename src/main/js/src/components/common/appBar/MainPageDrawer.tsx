import MenuIcon from "@material-ui/icons/Menu";
import SwipeableDrawer from "@material-ui/core/SwipeableDrawer";
import List from "@material-ui/core/List";
import ListSubheader from "@material-ui/core/ListSubheader";
import ListItemLink from "./ListItemLink";
import DescriptionIcon from "@material-ui/icons/Description";
import ArchiveIcon from "@material-ui/icons/Archive";
import AddIcon from "@material-ui/icons/Add";
import Divider from "@material-ui/core/Divider";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import IconButton from "@material-ui/core/IconButton";
import InfoIcon from '@material-ui/icons/Info';
import React, {useCallback, useState} from "react";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import Avatar from "@material-ui/core/Avatar";
import {useQuery} from '@apollo/client';
import {Skeleton} from "@material-ui/lab";
import {GET_USER} from "../../../utils/Queries";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      menuButton: {
        marginRight: theme.spacing(2),
      },
      list: {
        width: 250,
        backgroundColor: theme.palette.background.paper,
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        height: "100%"
      },
      failedAvatar: {
        height: "60px",
        backgroundColor: theme.palette.warning.dark
      },
      loadingAvatarText: {
        height: "2em"
      },
      // name: {
      //   display: "flex",
      //   flexDirection: "row",
      //   justifyContent: "space-around",
      //   alignItems: "center"
      // },
    })
)

export default function MainPageDrawer({loggedIn}: { loggedIn: boolean }) {
  const classes = useStyles();
  const [menuOpen, setMenuOpen] = useState(false);
  const toggleMenu = useCallback(() => setMenuOpen(!menuOpen), [menuOpen, setMenuOpen]);
  const {loading, error, data} = useQuery(GET_USER);
  return <>
    <IconButton edge="start"
                className={classes.menuButton}
                color="inherit"
                aria-label="menu"
                onClick={toggleMenu}>
      <MenuIcon/>
      <SwipeableDrawer open={menuOpen}
                       onClose={() => setMenuOpen(false)}
                       onOpen={() => setMenuOpen(true)}
                       ModalProps={{keepMounted: true}}>
        <List className={classes.list}>
          <div>
            {
              error
                  ? <ListItemLink
                      primary={<Skeleton className={classes.loadingAvatarText} variant="text"
                                         animation={false}/>}
                      icon={<Avatar><Skeleton variant={"circle"} animation={false}/></Avatar>}/>
                  : loading
                  ? <ListItemLink
                      primary={<Skeleton className={classes.loadingAvatarText} variant="text"/>}
                      icon={<Avatar><Skeleton variant={"circle"}/></Avatar>}/>
                  : <ListItemLink primary={data?.user?.name ?? ""}
                                  icon={<Avatar alt={data?.user?.name} src={data?.user?.picture}>{
                                    data?.user?.name
                                    .split(" ")
                                    .map((s: string) => s[0])
                                    .reduce((s1: string, s2: string) => s1 + s2)
                                  }</Avatar>}/>
            }
            <ListSubheader color="primary">Pages</ListSubheader>
            <Divider variant="fullWidth"/>
            {loggedIn && <>
              <ListItemLink to="/all" primary="Invoices" icon={<DescriptionIcon/>}/>
              <ListItemLink to="/archived" primary="Archive" icon={<ArchiveIcon/>}/>
              <ListItemLink to="/new" primary="Create" icon={<AddIcon/>}/>
              <Divider variant="fullWidth"/>
            </>}
          </div>
          <div>
            <ListItemLink to="/privacy" primary="Privacy Policy" icon={<InfoIcon/>}/>
            {loggedIn
                ? <>
                  <Divider variant="fullWidth"/>
                  <ListItemLink to="/logout" primary="Logout" icon={<ExitToAppIcon/>}/>
                </>
                : <>
                  <Divider variant="fullWidth"/>
                  <ListItemLink to="/login" primary="Login" icon={<ExitToAppIcon/>}/>
                </>}
          </div>
        </List>
      </SwipeableDrawer>
    </IconButton>
  </>;
}