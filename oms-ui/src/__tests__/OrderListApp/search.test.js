import { act, render, screen,  fireEvent,waitFor } from '@testing-library/react';
import Search from '../../Components/Order/OrderListApp/search';
import OmsAxios from '../../Utils/OmsAxios';

afterEach(() => {
  jest.clearAllMocks();
})

test("search-btn btn click resolve", () => {

  OmsAxios.put = jest.fn((value) => {
    return Promise.resolve(
      {
        data: {

          orders: [{ "clientId": 99, "createdAt": "2021-08-20T10:14:41.256461", "createdBy": 101, "isActive": true, "modifiedAt": "2021-08-20T10:14:41.256461", "modifiedBy": 101, "orderId": 20391, "quantity": 20, "side": "BUY", "status": "CREATED", "stock": "20", "stockPrice": null, "targetPrice": null, "type": "MARKET" }]
     
        }
        , status:200
      }
    );
  });
  OmsAxios.get = jest.fn();
  OmsAxios.post = jest.fn();
  const { queryByTestId } = render(<Search />)

  expect(screen.getByTestId("search-order").textContent).toBe("Search Order ");
  expect(queryByTestId("modal-visible")).toBeNull();
  expect(queryByTestId("modal-body")).toBeNull();
  expect(queryByTestId("modal-footer")).toBeNull();
  fireEvent.click(screen.getByTestId("search-order"));

  expect(screen.getByTestId("modal-visible")).toBeVisible();
  expect(screen.getByTestId("modal-body")).toBeVisible();
  expect(screen.getByTestId("modal-footer")).toBeVisible();
  expect(screen.getByTestId("close-btn")).toBeVisible()
  expect(screen.getByTestId("search-btn")).toBeVisible()
  expect(screen.getByTestId("search-btn").textContent).toBe("Search");
  expect(screen.getByTestId("close-btn").textContent).toBe("Close");
  fireEvent.click(screen.getByTestId("close-btn"));
  fireEvent.click(screen.getByTestId("search-btn"));

});

test("search-btn btn click resolve", () => {
let formValue1={ clientEmail: null, clientName: null, stock: null, type: null, startDate: null, endDate: null }
 let pageNumber=0;
  const searchOrder = jest.fn((formValue1,pageNumber) => {
     return Promise.resolve({
        data: {
          orders: [{ "clientId": 99, "createdAt": "2021-08-20T10:14:41.256461", "createdBy": 101, "isActive": true, "modifiedAt": "2021-08-20T10:14:41.256461", "modifiedBy": 101, "orderId": 20391, "quantity": 20, "side": "BUY", "status": "CREATED", "stock": "20", "stockPrice": null, "targetPrice": null, "type": "MARKET" }]
          }, status:200
      });
  });
  OmsAxios.put = jest.fn(() => {
    return Promise.resolve({
      data: {
        orders: [{ "clientId": 99, "createdAt": "2021-08-20T10:14:41.256461", "createdBy": 101, "isActive": true, "modifiedAt": "2021-08-20T10:14:41.256461", "modifiedBy": 101, "orderId": 20391, "quantity": 20, "side": "BUY", "status": "CREATED", "stock": "20", "stockPrice": null, "targetPrice": null, "type": "MARKET" }]
        }, status:200
      });
  });
  act(() => {
    // render components
    render(  <Search searchOrder= {searchOrder} fetchData = {jest.fn()} setAllOrderCall={jest.fn()} setFormValue= {jest.fn()} filteredOrdersHandler={jest.fn()}/>)
  });
  fireEvent.click(screen.getByTestId("search-order"));
  fireEvent.click(screen.getByTestId("search-btn"));
});
test("cancel search btn click resolve", async () => {
  let formValue1={ clientEmail: null, clientName: null, stock: null, type: null, startDate: null, endDate: null }
   let pageNumber=0;
    const searchOrder = jest.fn((formValue1,pageNumber) => {
       return Promise.resolve({
          data: {
            orders: [{ "clientId": 99, "createdAt": "2021-08-20T10:14:41.256461", "createdBy": 101, "isActive": true, "modifiedAt": "2021-08-20T10:14:41.256461", "modifiedBy": 101, "orderId": 20391, "quantity": 20, "side": "BUY", "status": "CREATED", "stock": "20", "stockPrice": null, "targetPrice": null, "type": "MARKET" }]
            }, status:200
        });
    });
    act(() => {
      // render components
      render(  <Search searchOrder= {searchOrder} fetchData = {jest.fn()} setAllOrderCall={jest.fn()} setFormValue= {jest.fn()} filteredOrdersHandler={jest.fn()}/>)
    });
    await waitFor(() => {
      fireEvent.click(screen.getByTestId("cancel-search"));
    });
    
   // fireEvent.click(screen.getByTestId("search-btn"));
  });

test("search-btn btn click reject", async () => {
    let formValue1={ clientEmail: null, clientName: null, stock: null, type: null, startDate: null, endDate: null }
    let pageNumber=0;
    const searchOrder = jest.fn((formValue1,pageNumber) => {
    return Promise.reject({
        status:403 
        });
    });
    act(() => {
    // render components
        render(  <Search searchOrder= {searchOrder} fetchData = {jest.fn()} setAllOrderCall={jest.fn()} setFormValue= {jest.fn()} filteredOrdersHandler={jest.fn()}/>)
    });
    await waitFor(() => {
        fireEvent.click(screen.getByTestId("search-order"));
    });
    await waitFor(() => {
        fireEvent.click(screen.getByTestId("search-btn"));
    });
 
});