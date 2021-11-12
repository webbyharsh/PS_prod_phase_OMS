import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Form from '../../Components/Order/OrderCaptureApp/Form';
import OmsAxios from '../../Utils/OmsAxios';

describe('Order Capture Form', () =>  {

  beforeEach(() => {
    let mock = jest.fn(() => {
      return Promise.resolve({
        data: [{
            clientName: "TestUser",
            clientId: 0
          }],
        ok: true
      });
    });
    OmsAxios.get = mock;
  });

  afterEach(() => {
    cleanup();
    jest.clearAllMocks();
  });

  test('renders without error', () => {
    render(<BrowserRouter><Form /></BrowserRouter>)
    expect(screen.getByTestId('order-capture-form')).toBeTruthy();
  });
  
  test('renders required input elements', () => {
    render(<BrowserRouter><Form /></BrowserRouter>)
    expect(screen.getByLabelText(/stock-name-select/i)).toBeVisible();
    expect(screen.getByTestId(/stock-quantity-input/i)).toBeVisible();
    expect(screen.getByTestId(/stock-side-select/i)).toBeVisible();
    expect(screen.getByTestId(/stock-type-select/i)).toBeVisible();
    expect(screen.getByLabelText(/stock-client-select/i)).toBeVisible();
    expect(screen.getByLabelText(/stock-price-input/i)).toBeVisible();
    expect(screen.getByTestId(/stock-exchange-select/i)).toBeVisible();
    expect(screen.getByLabelText(/proceed button/i)).toBeVisible();
  });  

  test('submit button clears form fields', () => {
    console.error = jest.fn();
    render(<BrowserRouter><Form /></BrowserRouter>)
    fireEvent.click(screen.getByLabelText(/proceed button/i));
    expect(screen.getByTestId('order-capture-form')).toHaveFormValues({});
    console.error.mockClear();
  });

  test('does not render error elements at start', () => {
    render(<BrowserRouter><Form /></BrowserRouter>)
    expect(screen.queryByLabelText(/stock name error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/stock quantity error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/side error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/type error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/price error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/exchange name error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/client id error/i)).not.toBeInTheDocument();
  });

  test('validation errors are raised on submit', async () => {
    let spy = jest.spyOn(console, "error").mockImplementation();
    window.alert = jest.fn();
    render(<BrowserRouter><Form /></BrowserRouter>)
    fireEvent.click(screen.getByLabelText(/proceed button/i));
    await waitFor(() => {
      expect(spy).toHaveBeenCalledTimes(3);
    })
    spy.mockRestore();
    window.alert.mockClear();
  });

  test('validation is performed by stock exchange field', async () => {
    let spy = jest.spyOn(console, "error").mockImplementation()
    render(<BrowserRouter><Form /></BrowserRouter>)
    let element = screen.getByTestId(/stock-exchange-select/i);
    fireEvent.focusIn(element);
    fireEvent.focusOut(element);
    expect(element).toHaveValue("nse"); //Default value checker
    expect(spy).not.toHaveBeenCalled(); //Since never empty
    spy.mockRestore();
  });

  test('validation is performed by stock quantity field', async () => {
    let spy = jest.spyOn(console, "error").mockImplementation()
    render(<BrowserRouter><Form /></BrowserRouter>)
    let element = screen.getByTestId(/stock-quantity-input/i);
    fireEvent.focusIn(element);
    fireEvent.focusOut(element);
    await waitFor(() => {
      expect(spy).toHaveBeenCalled();
      expect(screen.queryByLabelText(/stock quantity error/i)).toBeInTheDocument();
    });
    spy.mockRestore();
  });

  test('validation is performed by side field', async () => {
    let spy = jest.spyOn(console, "error").mockImplementation()
    render(<BrowserRouter><Form /></BrowserRouter>)
    let element = screen.getByTestId(/stock-side-select/i);
    fireEvent.focusIn(element);
    fireEvent.focusOut(element);
    expect(element).toHaveValue("buy"); //Default value checker
    expect(spy).not.toHaveBeenCalled(); //Since never empty
    spy.mockRestore();
  });

  test('validation is performed by type field', async () => {
    let spy = jest.spyOn(console, "error").mockImplementation()
    render(<BrowserRouter><Form /></BrowserRouter>)
    let element = screen.getByTestId(/stock-type-select/i);
    fireEvent.focusIn(element);
    fireEvent.focusOut(element);
    expect(element).toHaveValue("market"); //Default value checker
    expect(spy).not.toHaveBeenCalled(); //Since never empty
    spy.mockRestore();
  });

  test('validation is performed by price field', async () => {
    let spy = jest.spyOn(console, "error").mockImplementation()
    render(<BrowserRouter><Form /></BrowserRouter>)
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

});