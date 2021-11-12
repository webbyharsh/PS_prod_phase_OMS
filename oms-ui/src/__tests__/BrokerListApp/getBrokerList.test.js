import { defineFeature, loadFeature } from 'jest-cucumber';
import { cleanup } from '@testing-library/react';
const feature = loadFeature('src/__tests__/features/BrokerList.feature');

import getBrokerList from '../../Components/Admin/BrokerListApp/getBrokerList';
import OmsAxios from '../../Utils/OmsAxios';
defineFeature(feature, test => {

    afterEach(cleanup);
    var response
  test('Show broker list', ({ given, when, then }) => {
      given(/^jwttoken is (.*)$/, (j) => {   
      });
       
      when(/on component mount/, async () => {
          OmsAxios.get = jest.fn((value) => {
            return Promise.resolve({
              
              data:  [{"userId": "123", "name":"Batman", "active":false}]
              , status: 200
            });
          });
          response = await getBrokerList()
      });
  
      then(/^broker list (.*)$/, async (expectedBody) => {
          let res = JSON.parse(expectedBody);
          let keys = Object.keys(res)
          for(var v of keys){
          expect(response.data[0]).toHaveProperty(v)
          }
      });
    });
  });