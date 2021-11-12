import { defineFeature, loadFeature } from 'jest-cucumber';
import { cleanup } from '@testing-library/react';
const feature = loadFeature('src/__tests__/features/UpdateBroker.feature');
import UpdateBroker from '../../Components/Admin/BrokerListApp/UpdateBroker';
import OmsAxios from '../../Utils/OmsAxios';
defineFeature(feature, test => {

    afterEach(cleanup);
    var response
    test('Broker Update', ({ given, when, then }) => {

        given(/^jwttoken is (.*)$/, (j) => {
        });
      when(/on click on toggle button/, async () => {
            OmsAxios.put = jest.fn(() => {
              return Promise.resolve(
                {"data": "123", "status":"Batman", "headers":false,"statusText":""}
              );
            });
            response = await UpdateBroker(1,false)
      });
  
      then(/^update status (.*)$/, async (expectedBody) => {
          let res = JSON.parse(expectedBody);
          let keys = Object.keys(res)
          for(var v of keys){
          expect(response).toHaveProperty(v)
          }
      });
    });
  });