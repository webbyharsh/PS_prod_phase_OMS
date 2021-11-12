Feature: UpdatePassword

    Scenario: Password updated successfully
        Given UpdatePassword page has loaded
        And I have valid credentials
        When I UpdatePassword
        Then it should update successful


