import { defineFeature, loadFeature } from "jest-cucumber";
import { render, screen, fireEvent, cleanup, waitFor } from "@testing-library/react";
import Form from "../../Components/Order/OrderCaptureApp/Form";
import OmsAxios from "../../Utils/OmsAxios";
import { BrowserRouter } from 'react-router-dom';
import selectEvent from 'react-select-event';
import { toast } from 'react-toastify';
import Properties from "../../Utils/Properties";
jest.mock('react-toastify')

const feature = loadFeature("src/__tests__/features/CaptureOrder.feature");

defineFeature(feature, test => {
  afterEach(cleanup);

  var excg, qtyInt, side, type, cid;

  test("Captures order details", ({ given, when, then }) => {
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

    when(/entered these values/, () => {
      // Navigate to capture order page
      render(<BrowserRouter><Form /></BrowserRouter>);

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

      expect(screen.getByTestId(/order-capture-form/i)).toHaveFormValues(res);

      OmsAxios.get.mockClear();
    });
  });

  test("Order Capture successful", ({ given, and, when, then }) => {
    let exchange, stockName, quantity, side, type, price, clientId;
    let errorSpy;

    given("I have valid order details", () => {

      exchange = "nse";
      stockName = "AAON, Inc.";
      quantity = 30;
      side = "buy";
      type = "limit";
      price = 100;
      clientId = 0;
      errorSpy = jest.spyOn(console, "error").mockImplementation(value => console.log(value));
      OmsAxios.get = jest.fn(() => {
        return Promise.resolve({
          data: [{ id: clientId, name: "Test User"}],
          ok: true
        })
      });
      OmsAxios.post = jest.fn(() => {
        return Promise.resolve({
          data: [
            {
              orderId: 67,
              clientId: clientId,
              quantity: quantity,
              stock: stockName,
              stockPrice: price,
              side: side,
              type: type,
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

    and('login page has loaded', () => {
      render(<BrowserRouter><Form /></BrowserRouter>);
    })

    when("I enter them in form and submit", async () => {

      fireEvent.change(screen.getByTestId(/stock-exchange-select/i), {
        target: { value: excg },
      });

      fireEvent.change(screen.getByTestId(/stock-quantity-input/i), {
        target: { value: qtyInt },
      });
      fireEvent.change(screen.getByTestId(/stock-side-select/i), {
        target: { value: side },
      });
      fireEvent.change(screen.getByTestId(/stock-type-select/i), {
        target: { value: type },
      });

      fireEvent.change(screen.getByLabelText(/stock-name-select/), { target: { value: "AAON, Inc."}})
      await selectEvent.select(screen.getByLabelText(/stock-name-select/), /AAON, Inc./i)

      fireEvent.change(screen.getByLabelText(/stock-client-select/), { target: { value: "Test"}})
      await selectEvent.select(screen.getByLabelText(/stock-client-select/), /Test/i)

      if (type === "limit")
        fireEvent.change(screen.getByLabelText(/stock-price-input/i), {
          target: { value: 10 },
        });

      fireEvent.click(screen.getByText(/Proceed/));
    });

    then("order is created with the parameters I sent", async () => {
      await waitFor(() => {
        expect(errorSpy).not.toBeCalled();
        expect(toast.success).toBeCalled();
      })
    })
  });

  test("Order Capture unsuccessful", ({ given, and, when, then }) => {
    let exchange, stockName, quantity, side, type, price, clientId;
    let errorSpy;

    given("I have valid order details and poor network", () => {

      exchange = "nse";
      stockName = "AAON, Inc.";
      quantity = 30;
      side = "buy";
      type = "limit";
      price = 100;
      clientId = 0;
      errorSpy = jest.spyOn(console, "error").mockImplementation(value => console.log(value));
      OmsAxios.get = jest.fn(() => {
        return Promise.resolve({
          data: [{ id: clientId, name: "Test User"}],
          ok: true
        })
      });
      OmsAxios.post = jest.fn(() => {
        return Promise.reject({
          data: [],
          status: 404,
          message: "Dummy Network Error"
        });
      });
    });

    and('login page has loaded', () => {
      render(<BrowserRouter><Form /></BrowserRouter>);
    })

    when("I enter them in form and submit", async () => {

      fireEvent.change(screen.getByTestId(/stock-exchange-select/i), {
        target: { value: excg },
      });

      fireEvent.change(screen.getByTestId(/stock-quantity-input/i), {
        target: { value: qtyInt },
      });
      fireEvent.change(screen.getByTestId(/stock-side-select/i), {
        target: { value: side },
      });
      fireEvent.change(screen.getByTestId(/stock-type-select/i), {
        target: { value: type },
      });

      fireEvent.change(screen.getByLabelText(/stock-name-select/), { target: { value: "AAON, Inc."}})
      await selectEvent.select(screen.getByLabelText(/stock-name-select/), /AAON, Inc./i)

      fireEvent.change(screen.getByLabelText(/stock-client-select/), { target: { value: "Test"}})
      await selectEvent.select(screen.getByLabelText(/stock-client-select/), /Test/i)

      if (type === "limit")
        fireEvent.change(screen.getByLabelText(/stock-price-input/i), {
          target: { value: 10 },
        });

      fireEvent.click(screen.getByText(/Proceed/));
    });

    then("Some error occurs and I get a notification for this failure", async () => {
      await waitFor(() => {
        expect(errorSpy).not.toBeCalled();
        expect(toast.warn).toBeCalled();
      })
    })
  });
});
