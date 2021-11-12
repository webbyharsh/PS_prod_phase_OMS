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

describe("Login Form", () => {
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
    expect(screen.getByTestId("login-form")).toBeTruthy();
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
    expect(screen.getByTestId(/email-input/i)).toBeVisible();
    expect(screen.getByTestId(/password-input/i)).toBeVisible();
    expect(screen.getByLabelText(/sign-in button/i)).toBeVisible();
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
    expect(screen.queryByLabelText(/email error/i)).not.toBeInTheDocument();
    expect(screen.queryByLabelText(/password error/i)).not.toBeInTheDocument();
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
    let element = screen.getByTestId(/email-input/i);
    fireEvent.focusIn(element);
    fireEvent.focusOut(element);
    await waitFor(() => {
      expect(spy).toHaveBeenCalled();
      expect(screen.queryByLabelText(/email error/i)).toBeInTheDocument();
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
    let element = screen.getByTestId(/password-input/i);
    fireEvent.focusIn(element);
    fireEvent.focusOut(element);
    await waitFor(() => {
      expect(spy).toHaveBeenCalled();
      expect(screen.queryByLabelText(/password error/i)).toBeInTheDocument();
    });
    spy.mockRestore();
  });

  test("validation errors are raised on submit", async () => {
    let userName, role, setUserName, setRole;
    setUserName = (myUserName) => {
      userName=myUserName;
    }
    setRole = (myRole) => {
      role=myRole;
    }
    let spy = jest.spyOn(console, "error").mockImplementation();
    window.alert = jest.fn();
    render(
      <BrowserRouter>
        <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
          <LoginForm />
        </AuthContext.Provider>
      </BrowserRouter>
    );
    fireEvent.click(screen.getByLabelText(/sign-in button/i));
    await waitFor(() => {
      expect(spy).toHaveBeenCalledTimes(2);
    });
    spy.mockRestore();
    window.alert.mockClear();
  });

  test("switches from login to register", async () => {
    render(
      <BrowserRouter>
        <AuthContext.Provider>
          <LoginForm />
        </AuthContext.Provider>
      </BrowserRouter>
    );

    let signUpButton = screen.getByLabelText("sign up button");
    let element = screen.getByTestId("login-container");
    let signInButton = screen.getByLabelText("login_button");

    // Sign Up Button is visible
    expect(element).not.toHaveClass("right-panel-active");

    // I click on it
    fireEvent.click(signUpButton);

    // Now SignUp button is hidden and SignIn button is visible
    await waitFor(() => {
      expect(element).toHaveClass("right-panel-active");
    })

    fireEvent.click(signInButton);

    await waitFor(() => {
      expect(element).not.toHaveClass("right-panel-active");
    })
  })
});
