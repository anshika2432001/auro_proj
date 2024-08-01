import React, { useState } from 'react';
import { Autocomplete, Box, Button, Checkbox, Grid, TextField, Table, TableBody, TableCell, TableHead, TableRow, Typography } from '@mui/material';

const roles = [
  { id: 1, value: 'role1' },
  { id: 2, value: 'role2' },
  { id: 3, value: 'role3' }
];

const tableData = [
  { access: 'Dashboard', view: false, edit: false, download: false },
  { access: 'Student Learning Outcomes(R1)', view: false, edit: false, download: false },
  { access: 'Student School Attributes(R2)', view: false, edit: false, download: false },
  { access: 'Student School Attributes(R3)', view: false, edit: false, download: false },
  { access: 'Teacher Attributes', view: false, edit: false, download: false },
  { access: 'Student Career Growth Outlook(R4)', view: false, edit: false, download: false },
  { access: 'School Infrastructure', view: false, edit: false, download: false },
  { access: 'Parental and Household Engagement', view: false, edit: false, download: false },
  { access: 'Budget State', view: false, edit: false, download: false }
];

const UserManagement = () => {
  const [selectedRole, setSelectedRole] = useState(null);
  const [data, setData] = useState(tableData);

  const handleCheckboxChange = (index, field) => (event) => {
    const newData = [...data];
    newData[index][field] = event.target.checked;
    setData(newData);
  };

  return (
    <div>
      <h2>
        Role Management
      </h2>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={4}>
          <Autocomplete
            options={roles}
            getOptionLabel={(option) => option.value}
            renderInput={(params) => <TextField {...params} label="Select Role" variant="outlined" size="small" />}
            value={selectedRole}
            onChange={(event, newValue) => setSelectedRole(newValue)}
          />
        </Grid>
      </Grid>
      <Box mt={4}>
        <Table>
          <TableHead>
            <TableRow>
            <TableCell className="TableHeading">
            <b>Access Grant</b>
            </TableCell>
            <TableCell className="TableHeading">
            <p className="HeadingData">View</p> 
            </TableCell>
            <TableCell className="TableHeading">
            <p className="HeadingData">Edit</p>
            </TableCell>
            <TableCell className="TableHeading">
            <p className="HeadingData">Download</p>
            </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((row, index) => (
              <TableRow key={row.access}>
                 <TableCell className="BodyBorder">
                {row.access}
                    </TableCell>
                    <TableCell className="BodyBorder">
                    <p className="TableData"><Checkbox
                    checked={row.view}
                    onChange={handleCheckboxChange(index, 'view')}
                  /></p>
                </TableCell>
                <TableCell className="BodyBorder">
                <p className="TableData"><Checkbox
                    checked={row.edit}
                    onChange={handleCheckboxChange(index, 'edit')}
                  /></p>
                </TableCell>
                <TableCell className="BodyBorder">
                <p className="TableData"><Checkbox
                    checked={row.download}
                    onChange={handleCheckboxChange(index, 'download')}
                  /></p>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Box>
      <Box mt={4} display="flex" justifyContent="center">
        <Button variant="contained" color="primary" style={{ marginRight: 8 }}>
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

