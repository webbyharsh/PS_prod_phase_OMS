import { defineFeature, loadFeature } from 'jest-cucumber';
import { render, screen, fireEvent, cleanup, waitFor } from '@testing-library/react';
import LoginForm from '../../Components/Login-Register/LoginForm';
import { BrowserRouter } from 'react-router-dom';
import { AuthContext } from '../../Contexts/AuthContext';
import OmsAxios from '../../Utils/OmsAxios';

const feature = loadFeature('src/__tests__/features/Login.feature');

defineFeature(feature, test => {

  afterEach(() => {
    jest.clearAllMocks();
    cleanup();
  });

  test('Login successfully', ({ given, and, when, then }) => {
    let email, password;
    let errorSpy;
    let userName, role, setUserName, setRole;
    setUserName = (myUserName) => {
      userName=myUserName;
    }
    setRole = (myRole) => {
      role=myRole;
    }

    given('Login page has loaded', () => {
      errorSpy = jest.spyOn(console, "error");
      OmsAxios.post = jest.fn(() => {
        return Promise.resolve({
          data: {
            jwtAccessToken: "dummyToken"
          },
          status: 200
        });
      });
      OmsAxios.get = jest.fn(() => {
        return Promise.resolve({
          data: {
            username: "Dummy User",
            userId: 1,
            listRoles: ["ROLE_ADMIN"]
          },
          status: 200
        });
      });
      render(<BrowserRouter>
        <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
          <LoginForm />
        </AuthContext.Provider>
      </BrowserRouter>);
    });

    and('I have valid credentials', () => {
      email = "validCredential@validDomain.com";
      password = "password";
    });

    when('I try to login', () => {
      fireEvent.change(screen.getByTestId(/email-input/),
        { target: { value: email } });
      fireEvent.change(screen.getByTestId(/password-input/),
        { target: { value: password } });
      let result = fireEvent.click(screen.getByLabelText(/sign-in button/));
      console.log(result);
    });

    then('it should be successful', () => {
        expect(errorSpy).not.toBeCalled();
    });
  });

  test('Login failure', ({ given, and, when, then }) => {
    let email, password;
    let errorSpy = jest.spyOn(console, "error").mockImplementation((e) => {
      console.log(e);
    });

    let userName, role, setUserName, setRole;
    setUserName = (myUserName) => {
      userName=myUserName;
    }
    setRole = (myRole) => {
      role=myRole;
    }

    given('Login page has loaded', () => {
      render(<BrowserRouter>
        <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
          <LoginForm />
        </AuthContext.Provider>
      </BrowserRouter>);
    });

    and('I have invalid credentials', () => {
      email = "invalidCredentialinvalidDomain.com";
      password = "password";
    });

    when('I try to login', () => {
      fireEvent.change(screen.getByTestId(/email-input/),
        { target: { value: email } });
      fireEvent.change(screen.getByTestId(/password-input/),
        { target: { value: password } });
      fireEvent.submit(screen.getByTestId(/login-form/));
    });

    then('it should fail to login', async () => {
      await waitFor(() => {
        expect(errorSpy).toHaveBeenCalledTimes(1);
      });
    });
  });
});