import axios from "../utils/axios";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

export const STATUSES = Object.freeze({
    IDLE: 'idle',
    ERROR: 'error',
    LOADING: 'loading',
});

// Thunk
export const fetchFiltersDropdown = createAsyncThunk('filters/fetch', async () => {
    const res = await axios.get('/filter-dropdowns');
    console.log(res)
    return res.data; // Return only the data
});

const filterSlice = createSlice({
    name: 'filters',
    initialState: {
        data: [],
        status: STATUSES.IDLE,
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchFiltersDropdown.pending, (state) => {
                state.status = STATUSES.LOADING;
            })
            .addCase(fetchFiltersDropdown.fulfilled, (state, action) => {
                state.data = action.payload;
                state.status = STATUSES.IDLE;
            })
            .addCase(fetchFiltersDropdown.rejected, (state) => {
                state.status = STATUSES.ERROR;
            });
    },
});

 export const { setFiltersType, setStatus } = filterSlice.actions;

export default filterSlice.reducer;
