Feature: Register

    Scenario: Register successfully
        Given Register page has loaded
        And I have valid credentials
        When I try to Register
        Then it should be successful

    Scenario: Register failure
        Given Register page has loaded
        And I have invalid credentials
        When I try to Register
        Then it should fail to Register