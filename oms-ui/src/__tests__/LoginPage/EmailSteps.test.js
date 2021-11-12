import { defineFeature, loadFeature } from 'jest-cucumber';
import { render, screen, fireEvent, cleanup, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { jest } from '@jest/globals';
import Email from '../../Components/Login-Register/Email';
import OmsAxios from '../../Utils/OmsAxios.js';


const feature = loadFeature('src/__tests__/features/Email.feature');

defineFeature(feature, test => {

  afterEach(() => {
    jest.clearAllMocks();
    cleanup();
  });

  test('Email send successfully', ({ given, when, then }) => {
    let email = "test@gmail.com";
    jest.spyOn(window, 'alert').mockImplementation(() => {});
   



    given('Email page has loaded', () => {
        OmsAxios.post = jest.fn(() => {
            alert("Email sent!")
            return Promise.resolve({data : {}});
          });
      render(<BrowserRouter>
          <Email />
      </BrowserRouter>);
    });

    when('I try to Fill the Email', () => {
      fireEvent.change(screen.getByTestId(/input1/),
        { target: { value: email } });
      fireEvent.submit(screen.getByTestId(/email-form/));
    });

    then('it should send the Email to me successful', async () => {
      await waitFor(() => {
        expect(window.alert).toHaveBeenCalled();
      });
    });
  });

})