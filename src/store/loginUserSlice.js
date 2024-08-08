import axios from "../utils/axios";
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

export const STATUSES = Object.freeze({
    IDLE: 'idle',
    ERROR: 'error',
    LOADING: 'loading',
});

// Thunk
export const userLogin = createAsyncThunk('login/api', async (request) => {
    const res = await axios.post('/auth/authenticate', request);
    return res.data; // Return only the data
});

const loginUserSlice = createSlice({
    name: 'userLogin',
    initialState: {
        data: {},
        status: STATUSES.IDLE,
    },
    extraReducers: (builder) => {
        builder
            .addCase(userLogin.pending, (state) => {
                state.status = STATUSES.LOADING;
            })
            .addCase(userLogin.fulfilled, (state, action) => {
                state.data = action.payload;
                state.status = STATUSES.IDLE;
            })
            .addCase(userLogin.rejected, (state) => {
                state.status = STATUSES.ERROR;
            });
    },
});

export const { setLoginType, setStatus } = loginUserSlice.actions;

export default loginUserSlice.reducer;
