Feature: As a user, I want to add credit, so that I can hire e-bike

  Background: User is logged in
    Given The user "manuel" has sign in with password "password"

    Scenario: User add negative credit
    When The user add negative credit -6.0
    Then The system shows an error message, the credit is negative

    Scenario: User add some credit
    When The user add some credit
    Then The system notify add credit

    Scenario: User add empty credit
    When The user add empty credit
    Then The system shows an error message, the credit is empty
