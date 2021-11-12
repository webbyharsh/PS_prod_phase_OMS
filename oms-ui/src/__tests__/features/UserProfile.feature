Feature: User Profile
  User profile view edit
 
  Scenario Outline: Show user profile
    Given broker is <id>
    When on component mount
    Then user profile <response>
  
    Examples:
      | id | response    |                                                                                                                                                                     
      | 1  | {"userId": 1,"name": "Bruce Wayne","emailId": "p@gmail.com","enabled": true,"verificationCode": "asa","createdAt": "2021-08-12T10:20:27.120132","lastActiveAt": "2021-08-12T10:20:27.120132","contact": "121212121", "address": "Publicis Sapient, Bangalore, India","contact": "9999999999","emailId": "email@publicisgroupe.com","age": 23,"lastActiveAt": "29-07-2021","createdAt": "12-12-2020","active": false,"roleId": 2}|


      