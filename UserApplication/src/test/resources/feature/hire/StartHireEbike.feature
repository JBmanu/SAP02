Feature: As a user, I want to start ride e-bike, so that I can use it

  Background: User is logged in
    Given The user "manuel" has sign in with password "password", can to start ride e-bike
    And There is an e-bike free with id "0"

    Scenario: User start ride e-bike "0" with no credit
      Given The user has zero credit
      When The user start ride e-bike "0"
      Then The system shows an error message, the user has no credit

    Scenario: User start ride e-bike "0" that is already in use
      Given The e-bike "0" is in use from other user "marco" password "password"
      When The user start ride e-bike "0"
      Then The system shows an error message, the e-bike is already in use by another user

    Scenario: User start ride e-bike "0" with low battery
      Given the e-bike "0" is with low battery
      When The user start ride e-bike "0"
      Then The system shows an error message, the e-bike has no battery

    Scenario: User start ride e-bike "0"
      Given The user has some credit 100 and the e-bike "0" is free and has battery
      When The user start ride e-bike "0"
      Then The system starts the ride, notify the user, subtract some credits, it's minor of 100
      And change the state of the e-bike "0"