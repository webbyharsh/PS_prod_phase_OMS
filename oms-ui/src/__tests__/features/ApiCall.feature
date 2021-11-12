Feature: Order List View
  Showing list of order for broker
 
  Scenario Outline: Show order list
    Given broker is <id>
    When on component mount
    Then order list <response>
  
    Examples:
      | id | response                                                                                                                                                                         |
      | 1  | {"clientId": 99,"createdAt": "2021-08-20T10:14:41.256461","createdBy": 101,"isActive": true,"modifiedAt": "2021-08-20T10:14:41.256461","modifiedBy": 101,"orderId": 20391,"quantity": 20,"side": "BUY","status": "CREATED","stock": "20","stockPrice": null,"targetPrice": null,"type": "MARKET"}|

      