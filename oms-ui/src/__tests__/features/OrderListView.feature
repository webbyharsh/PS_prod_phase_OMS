Feature: Order List View
  Showing list of order for broker
 
  Scenario Outline: Show order list
    Given broker is <id>
    When on component mount
    Then order list <response>
  
    Examples:
      | id | response                                                                                                                                                                         |
      | 1  | {"stock": "Tata","quantity": 23,"clientId": 1,"createdAt": "11-12-2021"}|

      