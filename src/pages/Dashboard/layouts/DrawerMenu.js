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
import { useLocation, Link } from 'react-router-dom';

export default function DrawerMenu() {
  return (
    <List>
      {menu.map((item, key) => (
        <MenuItem key={key} item={item} />
      ))}
    </List>
  );
}

const MenuItem = ({ item }) => {
  const Component = hasChildren(item) ? MultiLevel : SingleLevel;
  return <Component item={item} />;
};

const SingleLevel = ({ item }) => {
  const location = useLocation();
  const isSelected = location.pathname === item.pageLink;

  return (
    <ListItem button component={Link} to={item.pageLink}>
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
    </ListItem>
  );
};

const MultiLevel = ({ item }) => {
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
        <List component="div" disablePadding sx={{ paddingLeft: 4 }}>
          {children.map((child, key) => (
            <MenuItem key={key} item={child} />
          ))}
        </List>
      </Collapse>
    </React.Fragment>
  );
};
