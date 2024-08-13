// ** Toolkit imports
import { configureStore } from '@reduxjs/toolkit'

// ** Reducers


import filterSliceReducer from './filterSlice';
import roleDropdownSliceReducer from './roleDropdownSlice';
import loginUserSliceReducer from './loginUserSlice';



export const store = configureStore({
  reducer: {

    
    filterDropdown: filterSliceReducer,
    loginUser: loginUserSliceReducer,
    roleDropdown: roleDropdownSliceReducer,

    
  },
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: false
    })
})

export default store;
