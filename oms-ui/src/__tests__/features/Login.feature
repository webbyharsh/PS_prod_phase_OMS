Feature: Login

    Scenario: Login successfully
        Given Login page has loaded
        And I have valid credentials
        When I try to login
        Then it should be successful

    Scenario: Login failure
        Given Login page has loaded
        And I have invalid credentials
        When I try to login
        Then it should fail to login
