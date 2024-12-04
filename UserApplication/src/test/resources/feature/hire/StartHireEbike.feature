Feature: As a user, I want to start ride e-bike, so that I can use it

  Background: User is logged in
    Given The user "manuel" has sign in with password "password"

    Scenario: User start ride e-bike with no credit
    Given The user has 0 credit
    When The user start ride e-bike
    Then The system shows an error message, the user has no credit

    Scenario: User start ride e-bike that is already in use
    Given The e-bike is in use
    When The user start ride e-bike
    Then The system shows an error message, the e-bike is already in use by another user

    Scenario: User start ride e-bike with empty battery
    Given the e-bike is with empty battery
    When The user start ride e-bike
    Then The system shows an error message, the e-bike has no battery

    Scenario: User start ride e-bike
    Given The user has some credit and the e-bike is free and has battery
    When The user start ride e-bike
    Then The system starts the ride, notify the user, subtract the credit
    And change the state of the e-bike
