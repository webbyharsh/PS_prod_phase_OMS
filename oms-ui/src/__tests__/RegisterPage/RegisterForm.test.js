import {
    cleanup,
    fireEvent,
    render,
    screen,
    waitFor,
  } from "@testing-library/react";
  import { BrowserRouter } from "react-router-dom";
  import LoginForm from "../../Components/Login-Register/LoginForm";
  import { AuthContext } from "../../Contexts/AuthContext";
  
  describe("Register Form", () => {
    afterEach(() => {
      cleanup();
      jest.clearAllMocks();
    });
  
    test("renders without error", () => {
      let userName, role, setUserName, setRole;
      setUserName = (myUserName) => {
        userName=myUserName;
      }
      setRole = (myRole) => {
        role=myRole;
      }
      render(
        <BrowserRouter>
          <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
            <LoginForm />
          </AuthContext.Provider>
        </BrowserRouter>
      );
      expect(screen.getByTestId("signup-form")).toBeTruthy();
    });
  
    test("renders required input elements", () => {
      let userName, role, setUserName, setRole;
      setUserName = (myUserName) => {
        userName=myUserName;
      }
      setRole = (myRole) => {
        role=myRole;
      }
      render(
        <BrowserRouter>
          <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
            <LoginForm />
          </AuthContext.Provider>
        </BrowserRouter>
      );
      expect(screen.getByTestId(/signin-name/)).toBeVisible();
      expect(screen.getByTestId(/signin-email/)).toBeVisible();
      expect(screen.getByTestId(/signin-password/)).toBeVisible();
      expect(screen.getByTestId(/signin-contactNumber/)).toBeVisible();
      expect(screen.getByTestId(/signin-age/)).toBeVisible();
      expect(screen.getByTestId(/signin-street/)).toBeVisible();
      expect(screen.getByTestId(/signin-city/)).toBeVisible();
      expect(screen.getByTestId(/signin-state/)).toBeVisible();
      expect(screen.getByTestId(/signin-country/)).toBeVisible();
    });
  
    test("does not render error elements at start", () => {
      let userName, role, setUserName, setRole;
      setUserName = (myUserName) => {
        userName=myUserName;
      }
      setRole = (myRole) => {
        role=myRole;
      }
      render(
        <BrowserRouter>
          <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
            <LoginForm />
          </AuthContext.Provider>
        </BrowserRouter>
      );
      expect(screen.queryByLabelText(/name_error/i)).not.toBeInTheDocument();
      expect(screen.queryByLabelText(/email_error/i)).not.toBeInTheDocument();
      expect(screen.queryByLabelText(/password_error/i)).not.toBeInTheDocument();
      expect(screen.queryByLabelText(/age_error/i)).not.toBeInTheDocument();
      expect(screen.queryByLabelText(/contactNumber_error/i)).not.toBeInTheDocument();
    });
  
    test("validation is performed by email field", async () => {
      let userName, role, setUserName, setRole;
      setUserName = (myUserName) => {
        userName=myUserName;
      }
      setRole = (myRole) => {
        role=myRole;
      }
      let spy = jest.spyOn(console, "error").mockImplementation();
      render(
        <BrowserRouter>
          <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
            <LoginForm />
          </AuthContext.Provider>
        </BrowserRouter>
      );
      let element = screen.getByTestId(/signin-email/i);
      fireEvent.focusIn(element);
      fireEvent.focusOut(element);
      await waitFor(() => {
        expect(spy).toHaveBeenCalled();
        expect(screen.queryByLabelText(/email_error/i)).toBeInTheDocument();
      });
      spy.mockRestore();
    });
  
    test("validation is performed by password field", async () => {
      let userName, role, setUserName, setRole;
      setUserName = (myUserName) => {
        userName=myUserName;
      }
      setRole = (myRole) => {
        role=myRole;
      }
      let spy = jest.spyOn(console, "error").mockImplementation();
      render(
        <BrowserRouter>
          <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
            <LoginForm />
          </AuthContext.Provider>
        </BrowserRouter>
      );
      let element = screen.getByTestId(/signin-password/i);
      fireEvent.focusIn(element);
      fireEvent.focusOut(element);
      await waitFor(() => {
        expect(spy).toHaveBeenCalled();
        expect(screen.queryByLabelText(/password_error/i)).toBeInTheDocument();
      });
      spy.mockRestore();
    });

    test("validation is performed by name field", async () => {
        let userName, role, setUserName, setRole;
        setUserName = (myUserName) => {
          userName=myUserName;
        }
        setRole = (myRole) => {
          role=myRole;
        }
        let spy = jest.spyOn(console, "error").mockImplementation();
        render(
          <BrowserRouter>
            <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
              <LoginForm />
            </AuthContext.Provider>
          </BrowserRouter>
        );
        let element = screen.getByTestId(/signin-name/i);
        fireEvent.focusIn(element);
        fireEvent.focusOut(element);
        await waitFor(() => {
          expect(spy).toHaveBeenCalled();
          expect(screen.queryByLabelText(/name_error/i)).toBeInTheDocument();
        });
        spy.mockRestore();
      });

      test("validation is performed by age field", async () => {
        let userName, role, setUserName, setRole;
        setUserName = (myUserName) => {
          userName=myUserName;
        }
        setRole = (myRole) => {
          role=myRole;
        }
        let spy = jest.spyOn(console, "error").mockImplementation();
        render(
          <BrowserRouter>
            <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
              <LoginForm />
            </AuthContext.Provider>
          </BrowserRouter>
        );
        let element = screen.getByTestId(/signin-age/i);
        fireEvent.focusIn(element);
        fireEvent.focusOut(element);
        await waitFor(() => {
          expect(spy).toHaveBeenCalled();
          expect(screen.queryByLabelText(/age_error/i)).toBeInTheDocument();
        });
        spy.mockRestore();
      });

      test("validation is performed by contactNumber field", async () => {
        let userName, role, setUserName, setRole;
        setUserName = (myUserName) => {
          userName=myUserName;
        }
        setRole = (myRole) => {
          role=myRole;
        }
        let spy = jest.spyOn(console, "error").mockImplementation();
        render(
          <BrowserRouter>
            <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
              <LoginForm />
            </AuthContext.Provider>
          </BrowserRouter>
        );
        let element = screen.getByTestId(/signin-contactNumber/i);
        fireEvent.focusIn(element);
        fireEvent.focusOut(element);
        await waitFor(() => {
          expect(spy).toHaveBeenCalled();
          expect(screen.queryByLabelText(/contactNumber_error/i)).toBeInTheDocument();
        });
        spy.mockRestore();
      });

      
    
  });
  