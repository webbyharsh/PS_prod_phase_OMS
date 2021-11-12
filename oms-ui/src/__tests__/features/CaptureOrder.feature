
Feature: Capture Order
  Allows broker to create a new order
 
  Scenario Outline: Captures order details
    Given exchange is <excg>, stock name is <name>, quantity is <qty>, side is <side>, type is <type>, clientId is <cid>
    When entered these values
    Then form values are <body>
  
    Examples:
      | excg | name | qty | side | type   | cid   | body                                                                                                                    |
      | nse  | tcs  | 30  | buy  | limit  | 1     | { "exchange": "nse", "stockName": "TCS", "stockQuantity": 30, "side": "buy", "type": "limit", "clientID": "1" } |
      | nse  | tcs  | 30  | buy  | market | 1     | { "exchange": "nse", "stockName": "TCS", "stockQuantity": 30, "side": "buy", "type": "market", "clientID": "1" } |

  
  Scenario: Order Capture successful
    Given I have valid order details
    And login page has loaded
    When I enter them in form and submit
    Then order is created with the parameters I sent

  Scenario: Order Capture unsuccessful
    Given I have valid order details and poor network
    And login page has loaded
    When I enter them in form and submit
    Then Some error occurs and I get a notification for this failure

