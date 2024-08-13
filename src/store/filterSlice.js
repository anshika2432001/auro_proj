import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from '../utils/axios';

export const fetchFiltersDropdown = createAsyncThunk('filters/fetch', async () => {
    const response = await axios.get('/filter-dropdowns');

    // Safely store only the `result` part of the data to localStorage as a stringified JSON
    localStorage.setItem('filterDropdowns', JSON.stringify(response.data.result));
    
    return response.data.result;
});

const initialData = () => {
    try {
        const storedData = localStorage.getItem('filterDropdowns');
        // Parse stored data only if it's a non-empty string
        return storedData ? JSON.parse(storedData) : null;
    } catch (error) {
        console.error('Error parsing JSON from localStorage', error);
        return null;
    }
};

const filterSlice = createSlice({
    name: 'filters',
    initialState: {
        data: initialData() || {}, 
        status: 'idle',
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchFiltersDropdown.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchFiltersDropdown.fulfilled, (state, action) => {
                state.data = action.payload;
                state.status = 'idle';
            })
            .addCase(fetchFiltersDropdown.rejected, (state) => {
                state.status = 'error';
            });
    },
});

export default filterSlice.reducer;
