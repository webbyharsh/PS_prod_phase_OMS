import {act, render, screen,cleanup } from '@testing-library/react';
import React from 'react';
import EditForm from '../../Components/UserProfile/EditForm';
import { createMemoryHistory } from 'history';
import routeData from 'react-router';

const mockLocation = {
    pathname: "/edit-user-profile",
    state: { finalUsers : {
                "userId": 2,
                "roleId": 1,
                "name": "Bruce Wayne",
                "address": "Publicis Sapient, Bangalore, India",
                "contact": "9999999999",
                "emailId": "email@publicisgroupe.com",
                "password": "1234xxxx",
                "age": 23,
                "lastActiveAt": "29-07-2021",
                "createdAt": "12-12-2020",
                "isActive": false
          }
    }
}

beforeEach(() => {
    jest.spyOn(routeData, 'useLocation').mockReturnValue(mockLocation)
});

const history=createMemoryHistory('/user-profile')

test("Edit form renders order row without crashing",()=>{
    act(() => {
        // render components
        render(<EditForm location={location} history={history}/>)
      });
   
    expect(screen.getByTestId("edit-profile-container-outer")).toBeVisible();
    expect(screen.getByTestId("edit-profile-container")).toBeVisible();
 })