//import OrderList from "../../OrderListApp/Components/Order/OrderList";
import { act,render,waitFor, screen,cleanup ,fireEvent} from '@testing-library/react';
import OrderList from '../../Components/Order/OrderListApp/OrderList';
import React from 'react';
import ReactDom from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import OrderRow from '../../Components/Order/OrderListApp/OrderRow';
import OmsAxios from '../../Utils/OmsAxios';
afterEach(cleanup)

test("Order List renders order row without crashing1",()=>{
  
   const div = document.createElement('div');
    act(() => {
      // render components
      ReactDom.render(
         <BrowserRouter>
         <OrderList/>
         </BrowserRouter>, 
       div);
    });  
})

test("Order List renders order row without crashing2",()=>{
  
    act(() => {
    render(
      <BrowserRouter>
      <OrderList/>
      </BrowserRouter>);
    });
    expect(screen.getByTestId("order-list-id")).toBeVisible();
    expect(screen.getByTestId("table-order-list-id")).toBeVisible();
    expect(screen.getByText("Order No.")).toBeInTheDocument;
    expect(screen.getByText("Stock Name")).toBeInTheDocument;
    expect(screen.getByText("Stocks Count")).toBeInTheDocument;
    expect(screen.getByText("Side")).toBeInTheDocument;
    expect(screen.getByText("Type")).toBeInTheDocument;
    expect(screen.getByText("Client Name")).toBeInTheDocument;
    expect(screen.getByText("Date Created")).toBeInTheDocument;
    expect(screen.getByText("View Details")).toBeInTheDocument;
    expect(screen.getByText("Order Status")).toBeInTheDocument;
})

it("renders order data", async () => {
        OmsAxios.get = jest.fn(() => {
        return Promise.resolve(
          {
              "clientId": 32,
              "name": "testsajjme2",
              "emailId": "dddddddskkddd@gmail.com",
              "createdAt": "2021-08-23T09:35:47.698803",
              "createdBy": 12,
              "lastModifiedAt": "2021-08-23T09:35:47.698803",
              "lastModifiedBy": 12,
              "address": {
                "street": "abc",
                "city": "abc",
                "state": "abc",
                "country": "abc"
              },
              "contact": "1234567890",
              "isActive": true
          });
        });

        // Use the asynchronous version of act to apply resolved promises
        await act(async () => { 
          let container = document.createElement("div");
          act(() => {
                render( <BrowserRouter>
                <OrderRow key="1"  id="id1"
                stock="1120" count="20" side="buyer" type="reliance" clientId="2" orderDate="2021-01-01" details="view details" status="sold" 
                /> </BrowserRouter>, container);
          });
        }); 
        expect(screen.getByTestId("order-row-th").textContent).toBe("id1");
        expect(screen.getByTestId("order-row-td1").textContent).toBe(" 1120");
        expect(screen.getByTestId("order-row-td2").textContent).toBe(" 20");
        expect(screen.getByTestId("order-row-td3").textContent).toBe("buyer ");
        expect(screen.getByTestId("order-row-td4").textContent).toBe("reliance");
      // expect(screen.getByTestId("order-row-td5").textContent).toBe("testsajjme2");
        expect(screen.getByTestId("order-row-td7").textContent).toBe("view details");
        expect(screen.getByTestId("order-row-td8").textContent).toBe(" sold ");
      
});

test("Order List testing fetch data resolve ",async ()=>{
  let pageNo=0;
   OmsAxios.get = jest.fn((pageNo) => {
      return Promise.resolve({
        data: {
          orders:[ {"stock": "Tata","quantity": 23,"clientId": 1,"createdAt": "11-12-2021"}]
       
        },
        status: 200
      });
    });
    act(() => {
    render(
      <BrowserRouter>
      <OrderList/>
      </BrowserRouter>);
 });

 await waitFor(() => {
  let btn1=screen.getByTestId("pagination-btn2-id1");
  expect(btn1.textContent).toBe("1");
  let btn2=screen.getByTestId("pagination-btn3-id");
 fireEvent.click(btn2);
});
 
});
test("Order List testing fetch data reject",()=>{
    OmsAxios.get = jest.fn(() => {
      return Promise.reject({
        status: 400
      }); 
    });
    act(() => {
        render(
        <BrowserRouter>
        <OrderList/>
        </BrowserRouter>);
    });
});
  
