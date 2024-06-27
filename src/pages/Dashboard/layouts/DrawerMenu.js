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

import { useNavigate, Link, useLocation } from 'react-router-dom';

export default function DrawerMenu() {
  return menu.map((item, key) => <MenuItem key={key} item={item} />);
}

const MenuItem = ({ item }) => {
  const Component = hasChildren(item) ? MultiLevel : SingleLevel;
  return <Component item={item} />;
};

const SingleLevel = ({ item }) => {
  const location = useLocation();
  const isSelected = location.pathname === item.pageLink;

  return (
    <ListItem button>
      <ListItemIcon sx={{ color: isSelected ? 'lightblue' : 'inherit' }}>
        {item.icon}
      </ListItemIcon>
      <Link to={item.pageLink} style={{ textDecoration: 'none', display: 'block', color: "inherit", width: '100%' }}>
        <ListItemText
          primary={item.title}
          sx={{
            color: isSelected ? 'lightblue' : 'inherit',
            '&:hover': {
              color: 'lightblue',
            },
          }}
        />
      </Link>
    </ListItem>
  );
};

const MultiLevel = ({ item }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { items: children } = item;
  const [open, setOpen] = useState(false);

  const handleClick = () => {
    setOpen((prev) => !prev);
  };

  const isSelected = children.some(child => location.pathname === child.pageLink);

  return (
    <React.Fragment>
      <ListItem button onClick={handleClick}>
        <ListItemIcon sx={{ color: isSelected ? 'lightblue' : 'inherit' }}>
          {item.icon}
        </ListItemIcon>
        <ListItemText
          primary={item.title}
          sx={{
            color: isSelected ? 'lightblue' : 'inherit',
            '&:hover': {
              color: 'lightblue',
            },
          }}
        />
        {open ? <ExpandLessIcon sx={{ color: isSelected ? 'lightblue' : 'inherit' }} /> : <ExpandMoreIcon sx={{ color: isSelected ? 'lightblue' : 'inherit' }} />}
      </ListItem>
      <Collapse in={open} timeout="auto" unmountOnExit>
        <List component="div" disablePadding>
          {children.map((child, key) => (
            <Link to={child.pageLink} key={key} style={{ textDecoration: 'none', display: 'block', color: "inherit" }}>
              <MenuItem item={child} />
            </Link>
          ))}
        </List>
      </Collapse>
    </React.Fragment>
  );
};
