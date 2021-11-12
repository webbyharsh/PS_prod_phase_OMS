import { defineFeature, loadFeature } from 'jest-cucumber';
import { render, screen, fireEvent, cleanup, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { AuthContext } from '../../Contexts/AuthContext';
import { toast } from 'react-toastify';
import OmsAxios from '../../Utils/OmsAxios';
import LoginForm from '../../Components/Login-Register/LoginForm';
jest.mock('react-toastify');

const feature = loadFeature('src/__tests__/features/Register.feature');

defineFeature(feature, test => {

  afterEach(() => {
    jest.clearAllMocks();
    cleanup();
  });

  test('Register successfully', ({ given, and, when, then }) => {
    let name,email, password,contactNumber,age,street,city,state,country;
    let errorSpy;
    let userName, role, setUserName, setRole;
    setUserName = (myUserName) => {
      userName=myUserName;
    }
    setRole = (myRole) => {
      role=myRole;
    }

    given('Register page has loaded', () => {
      errorSpy = jest.spyOn(console, "error").mockImplementation((err) => console.log(err));
      OmsAxios.post = jest.fn(() => {
        return Promise.resolve({
          data: {
            message:"We have sent you a verification email. Click on it to enable you account."
          },
          ok: true, 
        });
      });
      render(<BrowserRouter>
        <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
          <LoginForm />
        </AuthContext.Provider>
      </BrowserRouter>);
    });

    and('I have valid credentials', () => {
      name="pushpit";
      email="test5@gmail.com"
      password = "Password@123";
      age=23;
      contactNumber="9872323669";
      street="test1";
      city="xyz";
      state="punjab";
      country="india";
    });

    when('I try to Register', () => {
      fireEvent.change(screen.getByTestId(/signin-name/),
        { target: { value: name } });
        fireEvent.change(screen.getByTestId(/signin-email/),
        { target: { value: email } });
        fireEvent.change(screen.getByTestId(/signin-password/),
        { target: { value: password } });
        fireEvent.change(screen.getByTestId(/signin-contactNumber/),
        { target: { value: contactNumber } });
        fireEvent.change(screen.getByTestId(/signin-age/),
        { target: { value: age } });
        fireEvent.change(screen.getByTestId(/signin-street/),
        { target: { value: street } });
        fireEvent.change(screen.getByTestId(/signin-city/),
        { target: { value: city} });
        fireEvent.change(screen.getByTestId(/signin-state/),
        { target: { value: state } });
        fireEvent.change(screen.getByTestId(/signin-country/),
        { target: { value: country } });
      
      fireEvent.submit(screen.getByTestId(/signup-form/));
    });

    then('it should be successful', async () => {
      await waitFor(() => {
        expect(errorSpy).not.toHaveBeenCalled();
      });
    });
  });

  test('Register failure', ({ given, and, when, then }) => {
    let name,email, password,contactNumber,age,street,city,state,country;
    let userName, role, setUserName, setRole;
    setUserName = (myUserName) => {
      userName=myUserName;
    }
    setRole = (myRole) => {
      role=myRole;
    }

    given('Register page has loaded', () => {
      OmsAxios.post = jest.fn(() => {
        return Promise.reject("Network Error")
      });
      render(<BrowserRouter>
        <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
          <LoginForm />
        </AuthContext.Provider>
      </BrowserRouter>);
    });

    and('I have invalid credentials', () => {
        name="pushpit";
        email="test5@gmail.com"
        password = "Password@123";
        age=23;
        contactNumber="9872323669";
        street="test1";
        city="xyz";
        state="punjab";
        country="india";
    });

    when('I try to Register', () => {
        fireEvent.change(screen.getByTestId(/signin-name/),
        { target: { value: name } });
        fireEvent.change(screen.getByTestId(/signin-email/),
        { target: { value: email } });
        fireEvent.change(screen.getByTestId(/signin-password/),
        { target: { value: password } });
        fireEvent.change(screen.getByTestId(/signin-contactNumber/),
        { target: { value: contactNumber } });
        fireEvent.change(screen.getByTestId(/signin-age/),
        { target: { value: age } });
        fireEvent.change(screen.getByTestId(/signin-street/),
        { target: { value: street } });
        fireEvent.change(screen.getByTestId(/signin-city/),
        { target: { value: city} });
        fireEvent.change(screen.getByTestId(/signin-state/),
        { target: { value: state } });
        fireEvent.change(screen.getByTestId(/signin-country/),
        { target: { value: country } });
      
        console.log(fireEvent.click(screen.getByTestId(/signup-button/)));
    });

    then('it should fail to Register', async () => {
      await waitFor(() => {
        expect(toast.error).toBeCalledWith("Network Error");
      });
    });
  });
 });