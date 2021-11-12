import Navbar from '../../Components/Navbar/Navbar';
import { act,render, screen,cleanup } from '@testing-library/react';

import React from 'react';
import ReactDOM from 'react-dom';
import {Router, MemoryRouter} from 'react-router-dom';
afterEach(cleanup)

test("Navbar renders order row without crashing",()=>{
    const nav=document.createElement("nav");
    act(() => {
        // render components
        ReactDOM.render(() => {return (<Router><Navbar/></Router>); }
        ,nav);
    });
});
test("Navbar renders element without crashing",()=>{
    act(() => {
        // render components
        render(<Navbar/>,
            {wrapper: MemoryRouter}
        );
    });
    const navBarTitle=screen.getByTestId("order-nav-title-id")
    expect(navBarTitle.textContent).toBe("Order Management System");

    expect(screen.getByTestId("order-nav-id")).toBeVisible();
    expect(screen.getByTestId("order-nav-title-id")).toBeVisible();
    expect(screen.getByTestId("login-nav-button")).toBeVisible();
})
