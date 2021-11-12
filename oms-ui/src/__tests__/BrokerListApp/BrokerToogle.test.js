import {act, render,screen ,waitFor,fireEvent} from "@testing-library/react";
import BrokerToggle from "../../Components/Admin/BrokerListApp/BrokerToggle";
import  ReactDom from 'react-dom';
import OmsAxios from '../../Utils/OmsAxios';

test("Broker List render without crashing",()=>{
    const div=document.createElement("div");
    act(() => {
        // render components
        ReactDom.render( <BrokerToggle/>,div)
    });
});
test("Broker List element render without crashing",()=>{
    act(() => {
        // render components
        render(<BrokerToggle/>)
      });
   
    expect(screen.getByTestId("order-list-id")).toBeVisible();
    expect(screen.getByTestId("table-order-list-id")).toBeVisible();
    expect(screen.getByTestId("pagination-test-id")).toBeVisible();
    expect(screen.getByTestId("thead-test-id")).toBeVisible();
    expect(screen.getByTestId("tbody-test-id")).toBeVisible();
    expect(screen.getByTestId("broker-id")).toBeVisible();
    expect(screen.getByTestId("broker-name")).toBeVisible();
    expect(screen.getByTestId("current-status")).toBeVisible();
    expect(screen.getByTestId("change-status")).toBeVisible(); 
});

test("Broker List promise resolve ",async ()=>{
      OmsAxios.get = jest.fn(() => {
          return Promise.resolve({
            data:  [{"userId": "123", "name":"Batman", "active":false}],
            status:200
          } );
      });
      act(() => {
        // render components
          render(<BrokerToggle/>)
      });
      await waitFor(() => {
          let btn1=screen.getByTestId("pagination-btn2-id1");
          expect(btn1.textContent).toBe("1");
          let btn2=screen.getByTestId("pagination-btn3-id");
          fireEvent.click(btn2);
      });
});
test("Broker List promise reject ",()=>{
    OmsAxios.get = jest.fn(() => {
        return Promise.reject({
          status: 500
         });
    });
    act(() => {
        // render components
        render(<BrokerToggle/>)
    });
});
test("Broker List promise reject ",()=>{
      OmsAxios.get = jest.fn(() => {
        return Promise.resolve({
        status:403
        });
      });
      act(() => {
        // render components
        render(<BrokerToggle/>)
      });
});