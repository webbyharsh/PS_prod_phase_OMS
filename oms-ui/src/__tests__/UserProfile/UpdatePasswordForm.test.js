import {act, render, screen,cleanup } from '@testing-library/react';
import React from 'react';
import UpdatePasswordForm from '../../Components/UserProfile/UpdatePasswordForm';
import { createMemoryHistory } from 'history';
import routeData from 'react-router';

const mockLocation = {
    pathname: "/update-password",
}

beforeEach(() => {
    jest.spyOn(routeData, 'useLocation').mockReturnValue(mockLocation)
});

const history=createMemoryHistory('/user-profile')

test("Update password form renders without crashing",()=>{
    act(() => {
        // render components
        render(<UpdatePasswordForm location={location} history={history}/>)
      });
   
    expect(screen.getByTestId("update-password-container")).toBeVisible();
    expect(screen.getByTestId("update-password-container-inner")).toBeVisible();
 
 })