Feature: As a user, I want to sign in, so that I can use e-bike hire service \\

  Scenario: User "manuel" sign in with wrong username
    Given The user "manuel" is not registered
    When The user "manuel" sign in with wrong username and "password" as password
    Then The system shows an error message, the user "manuel" is not registered

  Scenario: User sign "manuel" in with wrong password
    Given The user "manuel" is registered with "password"
    When The user "manuel" sign in with wrong password "123"
    Then The system shows an error message, the password is wrong

  Scenario: User sign in with empty username or password
    Given The user "manuel" is registered with "password"
    When The user sign in with empty username or password
    Then The system give an error message, the username or password is empty

  Scenario: User "manuel" sign in with correct username and password
    Given The user "manuel" is registered with "password"
    When The user sign in with correct username and password, "manuel" and "password"
    Then User access to the service
