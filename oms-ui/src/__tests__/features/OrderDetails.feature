
Feature: Order details
  Showing details to the user
 
  Scenario Outline: Show order details
    Given jwttoken is <jwt>,orderid is <id>
    When on component mount
    Then order details <response>
  
    Examples:
      | jwt      | id | response                                                                                                                                                                         |
      | jwttoken | 1  | {"stockPrice": 1200,"targetPrice": 1100,"createdAt": null,"modifiedAt": null,"orderId": 1,"quantity": 100,"side": "BUY", "status": "ACCEPTED","stock": "Reliance","type": "LIMIT"}|

      