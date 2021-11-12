import OrderRow from '../../Components/Order/OrderListApp/OrderRow';
import { act,render, screen,cleanup } from '@testing-library/react';
import React from 'react';
import ReactDom from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import OmsAxios from '../../Utils/OmsAxios';

describe('Order Row tests', () => {
    afterEach(cleanup)
    test("Order row renders  without crashing 1",()=>{
        const div = document.createElement('div');
        act(() => {
            // render components
            ReactDom.render(
                <BrowserRouter>
                <OrderRow key="1"  id="id1"
                stock="1120" count="20" side="buyer" type="reliance" clientId="9" orderDate="2021-01-01" details="view details" status="sold" 
                />
                </BrowserRouter>, 
             div);
          });
    })
    
    test("Order row elements render properly 2",()=>{
        act(() => {
            // render components
            render(
                <BrowserRouter>
                <OrderRow key="1"  id="id1"
                stock="1120" count="20" side="buyer" type="reliance" clientId="9" orderDate="2021-01-01" details="view details" status="sold" 
                />
                </BrowserRouter>);
        });
        const colorBtn=screen.getByTestId("order-row-btn")
        expect(colorBtn).toHaveStyle(`background-color: ButtonFace`);
        expect(screen.getByTestId("order-row-tr")).toBeVisible();
        expect(screen.getByTestId("order-row-th")).toBeVisible();
        expect(screen.getByTestId("order-row-td1")).toBeVisible();
        expect(screen.getByTestId("order-row-td2")).toBeVisible();
        expect(screen.getByTestId("order-row-td3")).toBeVisible();
        expect(screen.getByTestId("order-row-td4")).toBeVisible();
        expect(screen.getByTestId("order-row-td5")).toBeVisible();
        expect(screen.getByTestId("order-row-td6")).toBeVisible();
        expect(screen.getByTestId("order-row-td7")).toBeVisible(); 
        expect(screen.getByTestId("order-row-td8")).toBeVisible(); 
             
    });
});

describe('Order row handles send order logic', () => {
    let validOrder = {
        quantity: 342,
        side: 'buy',
        stock: 'tcs',
        type: 'market',
        targetPrice: null,
        createdBy: 1,
        modifiedBy: 1,
        createdAt: '22/22/22',
        modifiedAt: '22/22/22',
        status: 'CREATED',
        clientId: 1,
        isActive: true
    };

    afterEach(() => {
        jest.clearAllMocks();
        cleanup();
    })

    test('order row testing ', async () => {
        OmsAxios.get = jest.fn().mockImplementation(() => {
            return Promise.resolve({
                data: { listRoles: ["ROLE_ADMIN"] },
                status: 200
            });
        });
        act(() => {
        
            render(<BrowserRouter>
                <OrderRow 
                    id="" 
                    count={validOrder.quantity}
                    clientName=""
                    details=""
                    orderDate={validOrder.createdAt}
                    {...validOrder}
                />
            </BrowserRouter>);
    
          });
 
    });

    test("Order Row testing fetch data resolve ",()=>{
        OmsAxios.get = jest.fn(() => {
        return Promise.resolve({
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
        act(() => {
            render(
            <BrowserRouter>
          
                <OrderRow 
                    id="" 
                    count={validOrder.quantity}
                    clientName=""
                    details=""
                    orderDate={validOrder.createdAt}
                    {...validOrder}
                />
            </BrowserRouter>);
        });
    });
    test("Order Reject testing fetch data reject",()=>{
        OmsAxios.get = jest.fn(() => {
            return Promise.reject({
            status: 400
            }); 
        });
        act(() => {
            render(
            <BrowserRouter>
                <OrderRow 
                    id="" 
                    count={validOrder.quantity}
                    clientName=""
                    details=""
                    orderDate={validOrder.createdAt}
                    {...validOrder}
                />
            </BrowserRouter>);
        });
    });
});


    
  
