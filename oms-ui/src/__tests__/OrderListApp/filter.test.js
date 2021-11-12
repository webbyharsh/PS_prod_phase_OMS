//import OrderList from "../../OrderListApp/Components/Order/OrderList";
import { act,render, screen,cleanup,fireEvent } from '@testing-library/react';
import React from 'react';
import Filter from '../../Components/Order/OrderListApp/filter';
afterEach(cleanup)



test("Order List renders order row without crashing",()=>{
      let form={
        "clientEmail": null,
        "clientName": null,
        "stock": null,
        "type": null,
        "startDate": null,
        "endDate": null
      }
      const setForm= jest.fn();  
      act(() => {
            // render components
            render(<Filter formData={form} setFormData={setForm}/>)
      }); 
      expect(screen.getByTestId("form-email").textContent).toBe("Client's Email ");;
      expect(screen.getByTestId("form-client-name").textContent).toBe("Client Name");;
      expect(screen.getByTestId("form-stock-name").textContent).toBe("Stock Name");;
      expect(screen.getByTestId("form-order-type").textContent).toBe("Order Type");;
      expect(screen.getByTestId("select-market-target").textContent).toBe("Market / Limit");;
      expect(screen.getByTestId("select-market-price").textContent).toBe("Market Price");;
      expect(screen.getByTestId("select-target-price").textContent).toBe("Limit Price");
      expect(screen.getByTestId("start-date").textContent).toBe("Start Date");;
      expect(screen.getByTestId("end-date").textContent).toBe("End Date");;

      fireEvent.change(screen.getByTestId("email-control"), {target: {value: ''}})
      fireEvent.change(screen.getByTestId("client-name-control"), {target: {value: ''}})
      fireEvent.change(screen.getByTestId("stock-name-control"), {target: {value: ''}})
      fireEvent.change(screen.getByTestId("start-date-control"), {target: {value: ''}})
      fireEvent.change(screen.getByTestId("end-date-control"), {target: {value: ''}})

      fireEvent.blur(screen.getByTestId("email-control"))
})



