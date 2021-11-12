import { defineFeature, loadFeature } from 'jest-cucumber';
import { cleanup } from '@testing-library/react';
const feature = loadFeature('src/__tests__/features/ApiCall.feature');
import ApiCall from '../../Components/Order/OrderListApp/ApiCall';
import OmsAxios from '../../Utils/OmsAxios';
defineFeature(feature, test => {

    afterEach(cleanup);
    var id
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
                return Promise.resolve(
                    [{"clientId": 99,"createdAt": "2021-08-20T10:14:41.256461","createdBy": 101,"isActive": true,"modifiedAt": "2021-08-20T10:14:41.256461","modifiedBy": 101,"orderId": 20391,"quantity": 20,"side": "BUY","status": "CREATED","stock": "20","stockPrice": null,"targetPrice": null,"type": "MARKET"}]   
                );
              });
            response = await ApiCall(value)
          });

          then(/^order list (.*)$/, async (expectedBody) => {
              let res = JSON.parse(expectedBody);
              let keys = Object.keys(res)
              for(var v of keys){
                  expect(response[0]).toHaveProperty(v)
              }
          });
      });
  });