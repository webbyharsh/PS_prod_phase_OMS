import { defineFeature, loadFeature } from 'jest-cucumber';
import { cleanup } from '@testing-library/react';
const feature = loadFeature('src/__tests__/features/ClientNameById.feature');
import OmsAxios from '../../Utils/OmsAxios';
import ClientApi from '../../Components/Order/OrderListApp/ClientApi';
defineFeature(feature, test => {
    afterEach(cleanup);
    var id1;
    var response;
  test('get client name', ({ given, when, then }) => {
        given(/^client id (.*)$/, (id) => {
        id1=id;
    });
    
    when(/on api call/, async () => {
            OmsAxios.get = jest.fn(() => {
            return Promise.resolve(
              {
                  "clientId": 32,
                  "name": "testsajjme2",
                  "emailId": "dddddddskkddd@gmail.com",
                  "createdAt": "2021-08-23T09:35:47.698803",
                  "createdBy": 12,
                  "lastModifiedAt": "2021-08-23T09:35:47.698803",
                  "lastModifiedBy": 12,
                  "address": {
                    "street": "abc",
                    "city": "abc",
                    "state": "abc",
                    "country": "abc"
                  },
                  "contact": "1234567890",
                  "isActive": true
              }   
            );
            });
            response = await ClientApi(id1) 
    });
  
    then(/^return (.*)$/, (arg0) =>{
       expect(response.name).toEqual("testsajjme2")
    });
  });
});