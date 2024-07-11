// ** Toolkit imports
import { configureStore } from '@reduxjs/toolkit'

// ** Reducers


import filterSliceReducer from './filterSlice';



export const store = configureStore({
  reducer: {

    
    filterDropdown: filterSliceReducer,

    
  },
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: false
    })
})
