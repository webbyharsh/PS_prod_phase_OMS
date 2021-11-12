import { defineFeature, loadFeature } from 'jest-cucumber';
import { cleanup } from '@testing-library/react';
const feature = loadFeature('src/__tests__/features/ApiCall.feature');
import ApiCall from '../../Components/Order/OrderListApp/ApiCall';
import OmsAxios from '../../Utils/OmsAxios';
defineFeature(feature, test => {

    afterEach(cleanup);
    var response
    var value
    test('Show order list', ({ given, when, then }) => {
        given(/^broker is (.*)$/, (j) => {  
        });
       
      
    
        when(/on component mount/, async () => {
              value={"clientEmail": null,"clientName": null,"endDate": null,
              "startDate": null,
              "stock": null,
              "type": null}
              OmsAxios.put = jest.fn((value) => {
                  return Promise.reject(
                      "ERROR"
                  );
              });
              response = await ApiCall(value)
        });
  
        then(/^order list (.*)$/, async (expectedBody) => {
          expect(response).toEqual("ERROR")
        });
    });
  });