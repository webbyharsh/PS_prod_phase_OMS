Feature:Email

    Scenario: Email send successfully
        Given Email page has loaded
        When I try to Fill the Email
        Then it should send the Email to me successful