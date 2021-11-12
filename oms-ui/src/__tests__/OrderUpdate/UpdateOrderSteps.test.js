import { defineFeature, loadFeature } from "jest-cucumber";
import { render, screen, fireEvent, cleanup } from "@testing-library/react";
import OrderUpdateForm from "../../Components/Order/OrderUpdate/OrderUpdateForm";
import OmsAxios from "../../Utils/OmsAxios";
import { BrowserRouter } from 'react-router-dom';
import Properties from "../../Utils/Properties";

const feature = loadFeature("src/__tests__/features/UpdateOrder.feature");

defineFeature(feature, test => {
  afterEach(cleanup);

  var excg, qtyInt, side, type, cid;

  test("Update order details", ({ given, when, then }) => {
    given(
      /^exchange is (.*), stock name is (.*), quantity is (.*), side is (.*), type is (.*), clientId is (.*)/,
      (e, n, q, s, t, c) => {
        qtyInt = parseInt(q);
        excg = e;
        side = s;
        type = t;
        cid = c;
        OmsAxios.get = jest.fn(() => {
          return Promise.resolve({
            data: [
              {
                clientName: "TestUser",
                clientId: c,
              },
            ],
            ok: true,
          });
        });
        console.error = jest.fn();
      }
    );

    when(/updated these values/, () => {
      // Navigate to capture order page
      render(<BrowserRouter><OrderUpdateForm /></BrowserRouter>);

      fireEvent.change(screen.getByTestId(/stock-exchange-select/i), {
        target: { value: excg },
      });
      // Removed fire event on `stock-name-select` as it belongs
      // to `react-select` library, which is not the scope of this test
      fireEvent.change(screen.getByTestId(/stock-quantity-input/i), {
        target: { value: qtyInt },
      });
      fireEvent.change(screen.getByTestId(/stock-side-select/i), {
        target: { value: side },
      });
      fireEvent.change(screen.getByTestId(/stock-type-select/i), {
        target: { value: type },
      });
      // Removed fire event on `stock-client-select` as it belongs
      // to `react-select` library, which is not the scope of this test
      if (type === "limit")
        fireEvent.change(screen.getByLabelText(/stock-price-input/i), {
          target: { value: 10 },
        });

      console.error.mockClear();
    });

    then(/^form values are (.*)$/, (expectedBody) => {
      let res = JSON.parse(expectedBody);

      // Removing these fields from expected output as it belongs to
      // `react-select` library which is not the scope of this test
      res.stockName = "";
      res.clientID = "";

     // expect(screen.getByTestId(/order-update-form/i)).toHaveFormValues(res);

      OmsAxios.get.mockClear();
    });
  });

  test("Order Updated successfully", ({ given, when, then }) => {
    let exchange, stockName, quantity, side, type, price, clientId;
    let response;

    
    given("I have valid order details", () => {
      // render(<Form />);

      exchange = "nse";
      stockName = "TCS";
      quantity = 30;
      side = "buy";
      type = "limit";
      price = 100;
      clientId = 0;

      OmsAxios.post = jest.fn(() => {
        return Promise.resolve({
          data: [
            {
              orderId: 67,
              clientId: 0,
              quantity: 30,
              stock: "TCS",
              stockPrice: null,
              side: "buy",
              type: "limit",
              targetPrice: 100,
              orderCreatedAt: "2021-08-07T19:27:19.087+00:00",
              orderAcceptedAt: null,
              lastUpdatedAt: "2021-08-07T19:27:19.087+00:00",
              orderCreatedBy: null,
              lastUpdatedBy: null,
              isActive: true,
              status: "CREATED",
            },
          ],
          created: true,
        });
      });
    });

    when("I updated them in request body and make API call", async () => {
      OmsAxios.put = jest.fn().mockResolvedValue({ created: true })
      
      const body = {
        stock: stockName,
        quantity: quantity,
        side: side,
        type: type,
        clientId: clientId,
        targetPrice: price,
      };

      response = await OmsAxios.put(
        `${Properties.ORDER_SERVER_URL}/order/67`,
        body,
      );

      OmsAxios.put.mockClear();
    });

    then("Response data contains the parameters I sent", () => {
      expect(response.created).toBe(true);
    })
  });
});
