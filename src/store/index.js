// ** Toolkit imports
import { configureStore } from '@reduxjs/toolkit'

// ** Reducers


import filterSliceReducer from './filterSlice';
import loginUserSliceReducer from './loginUserSlice';



export const store = configureStore({
  reducer: {

    
    filterDropdown: filterSliceReducer,
    loginUser: loginUserSliceReducer,

    
  },
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: false
    })
})
