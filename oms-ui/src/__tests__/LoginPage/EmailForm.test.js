import {act, render, screen,cleanup } from '@testing-library/react';
import React from 'react';
import EmailForm from "../../Components/Login-Register/EmailForm";
import { createMemoryHistory } from 'history';
import routeData from 'react-router';

const mockLocation = {
    pathname: "/forget-password-email",
}

beforeEach(() => {
    jest.spyOn(routeData, 'useLocation').mockReturnValue(mockLocation)
});

const history=createMemoryHistory('/login')

test("Update password form renders without crashing",()=>{
    act(() => {
        // render components
        render(<EmailForm location={location} history={history}/>)
      });
   
    expect(screen.getByTestId("email-container")).toBeVisible();
    expect(screen.getByTestId("email-container-inner")).toBeVisible();
 
 })