Feature: Broker Update
  Updating Broker

  Scenario Outline: Broker Update
    Given jwttoken is <jwt>
    When on click on toggle button
    Then update status <response>
  
    Examples:
        | jwt      | response                                                   |                                                                                                                                                                    
        | jwttoken | {"data": "123", "status":"Batman", "headers":false,"statusText":""}|
  
      