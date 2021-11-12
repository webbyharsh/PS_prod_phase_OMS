import { render, screen, fireEvent, cleanup, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import OrderDetails from '../../Components/Order/OrderDetails/OrderDetails';
import OmsAxios from '../../Utils/OmsAxios';

describe('Order Details tests', () => {
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
    status: 'CREATED'
  };

  beforeEach(() => {
    OmsAxios.get = jest.fn().mockImplementation(() => {
      return Promise.resolve({
        data: validOrder,
        status: 200
      });
    });
    OmsAxios.post = jest.fn().mockImplementation(() => {
      return Promise.resolve({
          data: { isSendOrderActionAuthorized: true },
          status: 200
      });
    });
  });
  
  afterEach(() => {
    cleanup();
    jest.clearAllMocks();
  })
  
  test('renders App without errors', () => {
    render(<BrowserRouter><OrderDetails/></BrowserRouter>);
    expect(screen.getByTestId('details')).toBeInTheDocument();
  });
  
  test('should render the Order Details page', () => {
    render(<BrowserRouter><OrderDetails/></BrowserRouter>);
    const linkElement = screen.getByText(/order details/i);
    expect(linkElement).toBeInTheDocument();
  
    const linkElement2 = screen.getByText(/StockPrice/i);
    expect(linkElement2).toBeInTheDocument();
  
    const linkElement3 = screen.getByText(/TargetPrice/i);
    expect(linkElement3).toBeInTheDocument();
  });

  test('should render the cancel button', ()=>{
    render(<BrowserRouter><OrderDetails/></BrowserRouter>);
    let element = screen.getByLabelText(/cancel order button/);
    
  })

  
});

describe('Order Details tests reject', () => {
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
    status: 'CREATED'
  };

  beforeEach(() => {
    OmsAxios.get = jest.fn().mockImplementation(() => {
      return Promise.reject({
        data: validOrder,
        status: 400
      });
    });
  })
  
  
  afterEach(() => {
    cleanup();
    jest.clearAllMocks();
  })
  
  test('renders App without errors', () => {
    render(<BrowserRouter><OrderDetails/></BrowserRouter>);
    expect(screen.getByTestId('details')).toBeInTheDocument();
  });
  
  
  
});


describe('Order details handles send order logic', () => {
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

  beforeEach(() => {
    OmsAxios.get = jest.fn().mockImplementation(() => {
      return Promise.resolve({
          data: { listRoles: ["ROLE_ADMIN"] },
          status: 200
      });
    }).mockImplementationOnce(() => {
      return Promise.resolve({
        data: validOrder,
        status: 200
      });
    });
    OmsAxios.post = jest.fn().mockImplementation(() => {
      return Promise.resolve({
          data: { listRoles: ["ROLE_ADMIN"] },
          status: 200
      });
    });
  });
  
  afterEach(() => {
    cleanup();
    jest.clearAllMocks();
  })

  test('checks authorization of user before sending order', async () => {
    render(<BrowserRouter>
        <OrderDetails/>
    </BrowserRouter>);
    let element = screen.getByLabelText(/send order button/);
    await waitFor(() => {
        expect(OmsAxios.get).toHaveBeenCalled();
        expect(element).toBeEnabled();
        fireEvent.click(element);
    });
  });
});

describe('Order details handles cancel order logic', () => {
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
    status: 'CREATED'
  };

  beforeEach(() => {
    OmsAxios.get = jest.fn().mockImplementation(() => {
      return Promise.resolve({
        data: validOrder,
        status: 200
      });
    });
      OmsAxios.delete = jest.fn().mockImplementation(() => {
        return Promise.resolve({
           data: { 
            quantity: 342,
            side: 'buy',
            stockName: 'tcs',
            type: 'market',
            targetPrice: null,
            createdBy: 'Broker',
            lastModifiedBy: 'Broker',
            createdOn: '22/22/22',
            lastModifiedOn: '22/22/22',
            status: 'CANCELLED'
            },
           status: 200
        });
      });
  });
  
  afterEach(() => {
    cleanup();
    jest.clearAllMocks();
  })

  test('checks authorization of user before cancel order', async () => {
    render(<BrowserRouter>
        <OrderDetails/>
    </BrowserRouter>);
    let element = screen.getByLabelText(/cancel order button/);
   
    await waitFor(() => {
      fireEvent.click(element);
        expect(OmsAxios.delete).toHaveBeenCalled();
        expect(element).toBeEnabled();
        
    });
  });
});

