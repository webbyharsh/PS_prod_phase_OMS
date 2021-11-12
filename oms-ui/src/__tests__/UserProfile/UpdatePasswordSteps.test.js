import { defineFeature, loadFeature } from 'jest-cucumber';
import { render, screen, fireEvent, cleanup, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { jest } from '@jest/globals';
import Email from '../../Components/Login-Register/Email';
import OmsAxios from '../../Utils/OmsAxios.js';
import UpdatePassword from '../../Components/UserProfile/UpdatePassword';
import { AuthContext } from '../../Contexts/AuthContext';


const feature = loadFeature('src/__tests__/features/UpdatePassword.feature');

defineFeature(feature, test => {

  afterEach(() => {
    jest.clearAllMocks();
    cleanup();
  });

  test('Password updated successfully', ({ given,and, when, then }) => {
    let email,password;
    let oldpassword = "password";
    let newpassword = "newpassword";
    let confirmpassword="newpassword";
    
    jest.spyOn(window, 'alert').mockImplementation(() => {});
    
    let userName, role, setUserName, setRole;
    setUserName = (myUserName) => {
      userName=myUserName;
    }
    setRole = (myRole) => {
      role=myRole;
    }



    given('UpdatePassword page has loaded', () => {
        OmsAxios.post = jest.fn(() => {
            alert("Successfully Updated!")
            return Promise.resolve({data : {}});
          });
          render(<BrowserRouter>
            <AuthContext.Provider value={{userName, role, setUserName, setRole}}>
              <UpdatePassword />
            </AuthContext.Provider>
          </BrowserRouter>);
    });

    and('I have valid credentials', () => {
        email = "validCredential@validDomain.com";
        password = "password";
      });

    when('I UpdatePassword', () => {
        fireEvent.change(screen.getByTestId(/input1/),
        { target: { value: oldpassword } });
        fireEvent.change(screen.getByTestId(/input2/),
        { target: { value: newpassword } });
        fireEvent.change(screen.getByTestId(/input3/),
        { target: { value: confirmpassword } });

      fireEvent.submit(screen.getByTestId(/update-password-form/));
    });

    then('it should update successful', async () => {
      await waitFor(() => {
        expect(window.alert).toHaveBeenCalled();
      });
    });
  });

})