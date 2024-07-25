import React from 'react'
import HomePage from './HomePage'
import OfferingsPage from './OfferingsPage'
import Benefits from './Benefits'
import AboutAuro from './AboutAuro'
import Testimonials from './Testimonials'
import Footer from './Footer'
import {fetchFiltersDropdown} from '../../store/filterSlice';
import { useDispatch, useSelector } from "react-redux";



const LandingPage =() => {
  const dispatch = useDispatch();

  
dispatch(fetchFiltersDropdown());

  return (
    <div>
      <HomePage/>
      <OfferingsPage/>
      <Benefits/>
      <AboutAuro/>
      <Testimonials/>
      
    </div>
  )
}

export default LandingPage
