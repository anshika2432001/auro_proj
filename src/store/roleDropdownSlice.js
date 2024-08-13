import axios from "../utils/axios";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

export const STATUSES = Object.freeze({
    IDLE: 'idle',
    ERROR: 'error',
    LOADING: 'loading',
});

// Thunk
export const fetchRoleDropdown = createAsyncThunk('roles/fetch', async () => {
    const res = await axios.get('user/role-dropdown');
    return res.data; 
});

const initialState = {
    data: JSON.parse(localStorage.getItem('roleDropdownData')) || [],
    status: STATUSES.IDLE,
};

const roleDropdownSlice = createSlice({
    name: 'roleDropdown',
    initialState,
    extraReducers: (builder) => {
        builder
            .addCase(fetchRoleDropdown.pending, (state) => {
                state.status = STATUSES.LOADING;
            })
            .addCase(fetchRoleDropdown.fulfilled, (state, action) => {
                state.data = action.payload;
                state.status = STATUSES.IDLE;
                localStorage.setItem('roleDropdownData', JSON.stringify(action.payload));
            })
            .addCase(fetchRoleDropdown.rejected, (state) => {
                state.status = STATUSES.ERROR;
            });
    },
});

export const { setRoleDropdownType, setStatus } = roleDropdownSlice.actions;

export default roleDropdownSlice.reducer;
