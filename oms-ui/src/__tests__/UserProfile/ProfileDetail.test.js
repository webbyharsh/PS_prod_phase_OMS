import {act, render, screen, fireEvent, cleanup } from '@testing-library/react';
import ProfileDetail from '../../Components/UserProfile/ProfileDetail';

let finalUsers1={
  userId: 2,
    roles: [{
        roleName: "Admin"
    }],
    name: "Riya",
    age: 23,
    contact: "9999999999",
    address: {
        street:"Publicis Sapient",
        city:"Bangalore",
        state:"Karnataka",
        country:"India",
    },
    emailId: "email@publicisgroupe.com",
    lastActiveAt: "29-07-2021",
    createdAt: "12-12-2020",
    isActive: false
}
test("Section3  renders profile details without crashing",()=>{
    act(() => {
        // render components
        render(<ProfileDetail finalUsers={finalUsers1} history="history" />)
      });
    
    
    expect(screen.getByTestId("inner-div1-section3")).toBeVisible();
    expect(screen.getByTestId("inner-div2-section3")).toBeVisible();
    expect(screen.getByTestId("inner-div3-section3")).toBeVisible();
    expect(screen.getByTestId("inner-div4-section3")).toBeVisible();
    expect(screen.getByTestId("inner-div5-section3")).toBeVisible();
    expect(screen.getByTestId("inner-div6-section3")).toBeVisible();
    expect(screen.getByTestId("inner-div9-section3")).toBeVisible();
    expect(screen.getByTestId("inner-div11-section3")).toBeVisible();
    expect(screen.getByTestId("inner-div12-section3")).toBeVisible();
    expect(screen.getByTestId("inner-div13-section3")).toBeVisible();
    expect(screen.getByTestId("inner-div14-section3")).toBeVisible();
    expect(screen.getByTestId("outer-div1-section3")).toBeVisible();
    expect(screen.getByTestId("outer-div2-section3")).toBeVisible();
    expect(screen.getByTestId("outer-div3-section3")).toBeVisible();
    expect(screen.getByTestId("outer-div4-section3")).toBeVisible();
    expect(screen.getByTestId("outer-div6-section3")).toBeVisible();
    expect(screen.getByTestId("outer-div7-section3")).toBeVisible();
    expect(screen.getByTestId("outer-div9-section3")).toBeVisible();
    expect(screen.getByTestId("outer-div1-section3")).toBeVisible();
    expect(screen.getByTestId("order-row-btn1")).toBeVisible();
    expect(screen.getByTestId("order-row-btn2")).toBeVisible();
    //expect(screen.getByTestId("order-row-btn1").textContent).toBe(/edit details/i);
    //expect(screen.getByTestId("order-row-btn2").textContent).toBe(/change password/i);
 })

test("Profile Detail button test",async ()=>{
    let spy = jest.spyOn(console, "error").mockImplementation();
   // const history1 = jest.fn(); 
   const history1={
    action: "PUSH",
    push:jest.fn(),
   
    location: {pathname: "/user-profile", search: "", hash: "", state: undefined, key: "mh5zou"}
   
    
   }
    act(() => {
        // render components
        render(<ProfileDetail finalUsers={finalUsers1} history={history1} />)
      });
   
     
    let btn1 =screen.getByTestId("order-row-btn1")
    expect(btn1).toHaveTextContent('Edit Details')
    fireEvent.click(btn1);
    let btn2 =screen.getByTestId("order-row-btn2")
    expect(btn2).toHaveTextContent('Change Password') 
    fireEvent.click(btn2);

})
 