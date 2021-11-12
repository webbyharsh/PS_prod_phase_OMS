
Feature: Update Order
  Allows broker to update an existing order
 
  Scenario Outline: Update order details
    Given exchange is <excg>, stock name is <name>, quantity is <qty>, side is <side>, type is <type>, clientId is <cid>
    When updated these values
    Then form values are <body>
  
    Examples:
      | excg | name | qty | side | type   | cid   | body                                                                                                                    |
      | nse  | tcs  | 30  | buy  | limit  | 1     | { "exchange": "nse", "stockName": "TCS", "stockQuantity": 30, "side": "buy", "type": "limit", "clientID": "1" } |
      | nse  | tcs  | 30  | buy  | market | 1     | { "exchange": "nse", "stockName": "TCS", "stockQuantity": 30, "side": "buy", "type": "market", "clientID": "1" } |

  
Scenario: Order Updated successfully
    Given I have valid order details
    When I updated them in request body and make API call
    Then Response data contains the parameters I sent