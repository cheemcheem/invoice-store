import React from "react";
import IconButton from "@material-ui/core/IconButton";
import MoreIcon from '@material-ui/icons/MoreVert';
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      icon: {
        color: theme.palette.background.default,
        marginRight: -theme.spacing(1)
      },
      menuItem: {
        display: "flex",
        flexDirection: "row",
        justifyContent: "space-between",
        width: "20ch"
      },
    })
)
type AppBarMoreButtonProps = {
  options: { option: string, icon: React.ReactNode, onClick: () => void }[]
}

export default function AppBarMoreButton({options}: AppBarMoreButtonProps) {
  const classes = useStyles();
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);

  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = (onClick: () => void) => () => {
    setAnchorEl(null);
    onClick();
  };

  return <>
    <IconButton aria-label="more"
                aria-controls="long-menu"
                aria-haspopup="true"
                className={classes.icon}
                onClick={handleClick}>
      <MoreIcon/>
    </IconButton>
    <Menu
        id="long-menu"
        anchorEl={anchorEl}
        keepMounted
        open={open}
        onClose={handleClose(() => {
        })}>
      {options.map(({option, icon, onClick}) =>
          <MenuItem key={option}
                    className={classes.menuItem}
                    onClick={handleClose(onClick)}>
            {option}{icon}
          </MenuItem>)}
    </Menu>
  </>
}