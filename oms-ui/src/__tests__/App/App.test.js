import { cleanup, render, screen, waitFor } from "@testing-library/react";
import App from "../../App";
import history from "../../Utils/History";
import * as constants from "../../Utils/Constants";
import { persistState } from "../../Utils/PersistState";
import OmsAxios from "../../Utils/OmsAxios";
import AuthenticateService from "../../Utils/AuthenticateService";

describe("Application tests", () => {
  afterEach(() => {
    cleanup();
    jest.clearAllMocks();
  });

  test("app renders without errors", () => {
    let spy = jest.spyOn(console, "error").mockImplementation();
    render(<App />);
    expect(spy).not.toHaveBeenCalled();
  });

  test("app default location is Login", async () => {
    render(<App />);
    await waitFor(() => {
      expect(history.location.pathname).toBe("/login");
    })
  });

  test(
    "Other pages timeout to login on inactivity",
    async () => {
      constants.InactivityTimeout = 5;
      render(<App />);
      history.push("/user-profile");
      await waitFor(
        () => {
          expect(history.location.pathname).toBe("/login");
        },
        {
          interval: constants.InactivityTimeout * 1000,
          timeout: constants.InactivityTimeout * 2 * 1000,
        }
      );
    },
    constants.InactivityTimeout * 2 * 1000
  );

  test(
    "Timeout to login on different route load",
    async () => {
      let mock = jest.fn().mockImplementationOnce(() => {
        return Promise.resolve({
          data: {
            isAuthenticated: true,
          },
          ok: true,
          status: 200
        });
      }).mockImplementation(() => {
        return Promise.resolve({
          data: {
            response: {}
          },
          status: 200
        });
      });
      OmsAxios.get = mock;
      OmsAxios.post = mock;
      console.error = jest.fn();

      constants.InactivityTimeout = 5;
      history.push("/user-profile");
      render(<App />);
      await waitFor(
        () => {
          expect(history.location.pathname).toBe("/login");
        },
        {
          interval: constants.InactivityTimeout * 1000,
          timeout: constants.InactivityTimeout * 2 * 1000,
        }
      );
    },
    constants.InactivityTimeout * 2 * 1000
  );
});
