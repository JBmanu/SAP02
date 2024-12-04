Feature: As a user, I want to sign up, so that I can use e-bike hire service

  Scenario: User sign up with same username of another user
    Given The user is not registered
    And another user is register with username "manuel" and password "1234"
    When The user sign up with username "manuel" and password "password"
    Then The system shows an error message, the username "manuel" is already registered

  Scenario: User sign up with correct username
    Given The user is not registered
    When The user sign up with correct username "manuel" and password "password"
    Then The system register user "manuel", and user access to the service

  Scenario: User sign up with empty username or password
    Given The user is not registered
    When The user sign up with empty username or password
    Then The system shows an error message, the username or password is empty
