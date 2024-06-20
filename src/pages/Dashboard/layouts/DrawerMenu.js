import React, { useState } from "react";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Collapse from "@mui/material/Collapse";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

import { menu } from "./Menu";
import { hasChildren } from "../../../utils/MenuUtils";

import {Routes, Route, useNavigate, Link} from 'react-router-dom';

export default function DrawerMenu() {
  return menu.map((item, key) => <MenuItem key={key} item={item} />);
}

const MenuItem = ({ item }) => {
  const Component = hasChildren(item) ? MultiLevel : SingleLevel;
  return <Component item={item} />;
};

const SingleLevel = ({ item }) => {
  return (
    <ListItem button>
        <ListItemIcon>{item.icon}</ListItemIcon>
        <Link to={item.pageLink} style={{textDecoration:'none',display:'block',color:"inherit"}}>
        <ListItemText primary={item.title} />
      </Link>
    </ListItem>
  );
};

const MultiLevel = ({ item }) => {
  const navigate = useNavigate();

  const { items: children } = item;
  const [open, setOpen] = useState(false);

  const handleClick = () => {
    setOpen((prev) => !prev);
  };

  const handlenavigate = (link) => {
    navigate(link);
  };

  return (
    <React.Fragment >
      <ListItem button onClick={handleClick} >
        <ListItemIcon>{item.icon}</ListItemIcon>
        <ListItemText primary={item.title} />
        {open ? <ExpandLessIcon /> : <ExpandMoreIcon />}
      </ListItem>
      <Collapse in={open} timeout="auto" unmountOnExit>
        <List component="div" disablePadding >
          {children.map((child, key) => (
            <Link to={child.pageLink} style={{textDecoration:'none',display:'block',color:"inherit"}}>
            <MenuItem key={key} item={child} href={child.pageLink} component={child.pageLink}/>
            </Link>
          ))}
        </List>
      </Collapse>
    </React.Fragment>
  );
};
