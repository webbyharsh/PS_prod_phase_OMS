import { act,render, screen,cleanup } from '@testing-library/react';

import React from 'react';
import UserProfile from '../../Components/UserProfile/UserProfile';

test("User profile renders order row without crashing",()=>{
    act(() => {
        // render components
        render(<UserProfile/>)
    });
    const outerDiv=screen.getByTestId("outer-div-user-profile-id")
    expect(outerDiv).toBeInTheDocument;
    expect(outerDiv).toHaveStyle(`margin: 2% 8% 5%`);
    const innerDiv=screen.getByTestId("inner-div-user-profile-id")
    expect(innerDiv).toBeInTheDocument;
    expect(innerDiv).toHaveStyle(`display: block`);
 })