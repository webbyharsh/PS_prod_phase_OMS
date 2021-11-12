Feature: GET CLIENT NAME
  Showing list of order for broker
 
  Scenario Outline: get client name
    Given client id is <id>
    When on api call
    Then return <response>
  
    Examples:
      | id | response                                                                                                                                                                         |
      | 1  | testsajjme2|

      