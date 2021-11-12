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
              return Promise.reject(
                {} 
              );
            });
            response = await ClientApi(id1)  
        });
  
        then(/^return (.*)$/, (arg0) =>{
          expect(response).toEqual("NULL")
        });
    });
});