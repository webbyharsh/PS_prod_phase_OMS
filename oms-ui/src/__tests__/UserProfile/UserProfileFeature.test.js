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
          return Promise.resolve({
            data: {
              response: {"userId": 1,"name": "Bruce Wayne","emailId": "p@gmail.com","enabled": true,"verificationCode": "asa","createdAt": "2021-08-12T10:20:27.120132","lastActiveAt": "2021-08-12T10:20:27.120132","contact": "121212121", "address": "Publicis Sapient, Bangalore, India","contact": "9999999999","emailId": "email@publicisgroupe.com","age": 23,"lastActiveAt": "29-07-2021","createdAt": "12-12-2020","active": false,"roleId": 2},
              status: 200
            }
          });
        });
        let user_id=1
        response = await getUserData(user_id)
      });
      then(/^user profile (.*)$/, async (expectedBody) => {
        let res = JSON.parse(expectedBody);
        let keys = Object.keys(res)
        for(var v of keys){
          expect(response.data.response).toHaveProperty(v)
        }
      });
    });
});