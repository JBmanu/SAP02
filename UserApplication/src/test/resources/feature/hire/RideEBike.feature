Feature: As a user, I want to ride e-bike, so that I can move

  Background: : user is logged in and ride e-bike
    Given The user "manuel" has sign in with "password"
    And The user hire e-bike "0"

  Scenario: User has some credit
    Given The user has some credit
    When The user ride e-bike
    Then The system subtract the credit, notify the user

  Scenario: User finished credit
    Given The user has no credit
    When The user ride e-bike
    Then The system stops the ride, notify the user and free the e-bike with id "0"

  Scenario: EBike finished battery
    Given The e-bike "0" is with low battery
    When The user ride e-bike
    Then The system stops the ride, notify the user and low battery the e-bike "0"

