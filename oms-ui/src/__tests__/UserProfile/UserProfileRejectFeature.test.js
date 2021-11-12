import { defineFeature, loadFeature } from 'jest-cucumber';
import { render, screen,cleanup } from '@testing-library/react';
const feature = loadFeature('src/__tests__/features/UserProfile.feature');
import getUserData from "../../Components/UserProfile/getUserData"
import OmsAxios from '../../Utils/OmsAxios';
defineFeature(feature, test => {
    afterEach(cleanup);
    var id
    var response
    test('Show user profile', ({ given, when, then }) => {

      given(/^broker is (.*)$/, (d) => {
        id = parseInt(d);
      });
      when(/on component mount/, async () => {
        OmsAxios.get = jest.fn(() => {
          return Promise.reject(
           
            "ERROR"
          );
        });
        let user_id=1
        response = await getUserData(user_id)
      });
      then(/^user profile (.*)$/, async (expectedBody) => {
        let res = JSON.parse(expectedBody);
        let keys = Object.keys(res)
        expect(response).toEqual("ERROR")
      });
    });
});