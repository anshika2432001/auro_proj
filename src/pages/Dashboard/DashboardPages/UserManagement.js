import React, { useState, useEffect } from 'react';
import { Autocomplete, Box, Button, Checkbox, Grid, TextField, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';
import { useSelector } from "react-redux";
import { useSnackbar } from "../../uiComponents/Snackbar";
import axios from '../../../utils/axios';

const UserManagement = () => {
  const selectedRoleId = localStorage.getItem('roleId');
  const { showSnackbar } = useSnackbar();
  const roleDropdownOptions = useSelector((state) => state.roleDropdown.data.result);
  const [selectedRole, setSelectedRole] = useState(null);
  const [data, setData] = useState([]);
  const [originalData, setOriginalData] = useState([]);

  // Set the default selected role when the component mounts
  useEffect(() => {
    const defaultRole = roleDropdownOptions.find(role => role.roleId === Number(selectedRoleId));
    if (defaultRole) {
      setSelectedRole(defaultRole);
    }
  }, [roleDropdownOptions, selectedRoleId]);

  // Fetch table data when selectedRole changes
  useEffect(() => {
    if (selectedRole) {
      handleRoleBasedDashboards();
    }
  }, [selectedRole]);

  const handleCheckboxChange = (index, field) => (event) => {
    const newData = data.map((item, idx) =>
      idx === index ? { ...item, [field]: event.target.checked } : item
    );
    setData(newData);
  };

  const handleRoleBasedDashboards = async () => {
    let payload = {
      "roleId": selectedRole.roleId,
    };
    try {
      const res = await axios.post("/user/role-access-fetch", payload);
      if (res.data.status === true && res.data.statusCode === 200) {
        const fetchedData = res.data.result.map(item => ({
          access: item.dashboardName,
          grantAccess: item.grantAccess === 1
        }));
        setData(fetchedData);
        setOriginalData(fetchedData); 
        showSnackbar(res.data.message, "success");
      } else {
        showSnackbar(res.data.message, "warning");
      }
    } catch (error) {
      console.log(error);
      showSnackbar("Error", "error");
    }
  };

  const handleUpdatedDashboards = async () => {
    const updatedPayload = data.reduce((acc, curr, index) => {
      if (curr.grantAccess !== originalData[index].grantAccess) {
        acc.push({
          roleId: selectedRole.roleId,
          dashboardName: curr.access,
          grantAccess: curr.grantAccess ? 1 : 0
        });
      }
      return acc;
    }, []);
    console.log(updatedPayload);

    if (updatedPayload.length > 0) {
      try {
        const res = await axios.post("/user/update-role-access", updatedPayload);
        if (res.data.status === true) {
          showSnackbar("Role access updated successfully", "success");
        } else {
          showSnackbar("Failed to update role access", "warning");
        }
      } catch (error) {
        console.log(error);
        showSnackbar("Error", "error");
      }
    } else {
      showSnackbar("No changes made to update", "info");
    }
  };

  return (
    <div>
      <h2>Role Management</h2>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={4} md={4} lg={4}>
          <Autocomplete
            options={roleDropdownOptions}
            getOptionLabel={(option) => option.roleName}
            renderInput={(params) => <TextField {...params} label="Select Role" variant="outlined" size="small" />}
            value={selectedRole}
            onChange={(event, newValue) => setSelectedRole(newValue)}
          />
        </Grid>
        {/* <Grid item xs={12} sm={2} md={2} lg={2} textAlign="center">
          <Button variant="contained" sx={{ m: 0 }} color="primary" onClick={handleRoleBasedDashboards}>Search</Button>
        </Grid> */}
      </Grid>
      <Box mt={4}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><b>Dashboard</b></TableCell>
              <TableCell><b>Grant Access</b></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((row, index) => (
              <TableRow key={row.access}>
                <TableCell>{row.access}</TableCell>
                <TableCell>
                  <Checkbox
                    checked={row.grantAccess}
                    onChange={handleCheckboxChange(index, 'grantAccess')}
                  />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Box>
      <Box mt={4} display="flex" justifyContent="center">
        <Button variant="contained" color="primary" style={{ marginRight: 8 }} onClick={handleUpdatedDashboards}>
          Save
        </Button>
        <Button variant="contained" color="primary">
          Cancel
        </Button>
      </Box>
    </div>
  );
};

export default UserManagement;
