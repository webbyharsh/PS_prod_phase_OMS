import { cleanup } from '@testing-library/react';
import { defineFeature, loadFeature } from 'jest-cucumber';
const feature = loadFeature('src/__tests__/features/OrderListView.feature');
import getData from "../../Components/Order/OrderListApp/getData"
import OmsAxios from '../../Utils/OmsAxios';

defineFeature(feature, test => {
    afterEach(cleanup);
    var id;
    var response;
    test('Show order list', ({ given, when, then }) => {
      given(/^broker is (.*)$/, (d) => {
          id = parseInt(d);
      });
  
      when(/on component mount/, async () => {
        OmsAxios.get = jest.fn(() => {
          return Promise.resolve({
            data: {
              orders:[ {"stock": "Tata","quantity": 23,"clientId": 1,"createdAt": "11-12-2021"}],
            },
            status: 200
          });
        });
        response = await getData()
      });
  
      then(/^order list (.*)$/, async (expectedBody) => {
          let res = JSON.parse(expectedBody);
          let keys = Object.keys(res)
          for(var v of keys){
              expect(response.data.orders[0]).toHaveProperty(v)
          }
      });
    });
});