Feature: Broker List View
  Showing list of broker

  Scenario Outline: Show broker list
    Given jwttoken is <jwt>
    When on component mount
    Then broker list <response>
  
    Examples:
        | jwt      | response                                                   |                                                                                                                                                                    
        | jwttoken | {"userId": "123", "name":"Batman", "active":false}|
  
      