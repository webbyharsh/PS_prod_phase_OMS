Feature: Client Bulk Upload
    Scenario: Client data uploaded successfully
        Given i have valid client data
        And my bulk upload page has loaded
        When i select my file and submit
        Then the response should be as i expect

    Scenario: Client invalid data not uploaded successfully
        Given i have invalid client data
        And my bulk upload page has loaded
        When i select my file and submit
        Then the response should be as i expect

      Scenario: To download the format of samplefile
        Given sample file stored in local storage
        And my bulk upload page has loaded
        When click the download format button
        Then file get downloaded
