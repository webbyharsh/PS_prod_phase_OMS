import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import OrderUpdateForm from '../../Components/Order/OrderUpdate/OrderUpdateForm';
import OmsAxios from '../../Utils/OmsAxios';
import SendOrderService from "../../Utils/SendOrderService";
import { error } from '../../Components/Order/OrderUpdate/OrderUpdateForm';


 describe('Order Update Form', () =>  {

  let validOrder = {
    quantity: 342,
    side: 'buy',
    stockName: 'tcs',
    type: 'market',
    targetPrice: null,
    createdBy: 'Broker',
    lastModifiedBy: 'Broker',
    createdOn: '22/22/22',
    lastModifiedOn: '22/22/22',
    status: 'CREATED',
    clients: [{ value: 0, label: "TestUser 0" }],
  };

  let invalidOrder = {
    quantity: 3421,
    side: 'buy',
    stockName: 'tcs',
    targetPrice: null,
    createdBy: 'Broker',
    lastModifiedBy: 'Broker',
    createdOn: '22/22/22',
    lastModifiedOn: new Date(),
};

  let clientData = {
    clientId : 0,
    clientName : 'ABC',
  }

  beforeEach(() => {
    OmsAxios.get = jest.fn().mockImplementation(() => {
      return Promise.resolve({
        data: validOrder,
        status: 200
      });
    });
   
  });

 

  afterEach(() => {
    cleanup();
    jest.clearAllMocks();
  });

  test('renders without error', () => {
    render(<BrowserRouter><OrderUpdateForm /></BrowserRouter>)
    expect(screen.getByTestId('order-update-form')).toBeTruthy();
  });
  
  test('renders required input elements', () => {
    render(<BrowserRouter><OrderUpdateForm /></BrowserRouter>)
    expect(screen.getByLabelText(/stock-name-select/i)).toBeVisible();
    expect(screen.getByTestId(/stock-quantity-input/i)).toBeVisible();
    expect(screen.getByTestId(/stock-side-select/i)).toBeVisible();
    expect(screen.getByTestId(/stock-type-select/i)).toBeVisible();
    expect(screen.getByLabelText(/stock-client-select/i)).toBeVisible();
    expect(screen.getByLabelText(/stock-price-input/i)).toBeVisible();
    expect(screen.getByTestId(/stock-exchange-select/i)).toBeVisible();
    expect(screen.getByLabelText(/proceed button/i)).toBeVisible();
    expect(screen.getByLabelText(/back button/i)).toBeVisible();
  });  

  test('submit button clears form fields', () => {
    console.error = jest.fn();
    render(<BrowserRouter><OrderUpdateForm /></BrowserRouter>)
    fireEvent.click(screen.getByLabelText(/proceed button/i));
    expect(screen.getByTestId('order-update-form')).toHaveFormValues({});
    console.error.mockClear();
  });

  test('does not render error elements at start', () => {
    render(<BrowserRouter><OrderUpdateForm /></BrowserRouter>)
    expect(screen.queryByLabelText(/stock name error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/stock quantity error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/side error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/type error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/price error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/exchange name error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/client id error/i)).not.toBeInTheDocument();
  });
  

  test('validation is performed by stock exchange field', async () => {
    let spy = jest.spyOn(console, "error").mockImplementation()
    render(<BrowserRouter><OrderUpdateForm /></BrowserRouter>)
    let element = screen.getByTestId(/stock-exchange-select/i);
    fireEvent.focusIn(element);
    fireEvent.focusOut(element);
    expect(element).toHaveValue("nse"); //Default value checker
    expect(spy).not.toHaveBeenCalled(); //Since never empty
    spy.mockRestore();
  });

  



  test('validation is performed by side field', async () => {
    let spy = jest.spyOn(console, "error").mockImplementation()
    render(<BrowserRouter><OrderUpdateForm /></BrowserRouter>)
    let element = screen.getByTestId(/stock-side-select/i);
    fireEvent.focusIn(element);
    fireEvent.focusOut(element);
    expect(element).toHaveValue("Sell"); //Default value checker
    expect(spy).not.toHaveBeenCalled(); //Since never empty
    spy.mockRestore();
  });

  test('validation is performed by type field', async () => {
    let spy = jest.spyOn(console, "error").mockImplementation()
    render(<BrowserRouter><OrderUpdateForm /></BrowserRouter>)
    let element = screen.getByTestId(/stock-type-select/i);
    fireEvent.focusIn(element);
    fireEvent.focusOut(element);
    expect(element).toHaveValue("default"); //Default value checker
    expect(spy).not.toHaveBeenCalled(); //Since never empty
    spy.mockRestore();
  });

  test('validation is performed by price field', async () => {
    let spy = jest.spyOn(console, "error").mockImplementation()
    render(<BrowserRouter><OrderUpdateForm /></BrowserRouter>)
    let element = screen.getByLabelText(/stock-price-input/i);
    if (!element.hasAttribute('readonly')) {
      fireEvent.focusIn(element);
      fireEvent.focusOut(element);
      await waitFor(() => {
        expect(spy).toHaveBeenCalled();
        expect(screen.queryByLabelText(/price error/i)).toBeInTheDocument();
      });
    }
    spy.mockRestore();
  });

 

  test('should validate order', () => {
    let sendOrderServiceInstance = new SendOrderService(invalidOrder);
    expect(sendOrderServiceInstance.isOrderValid).toBeFalsy();
  });
 
});