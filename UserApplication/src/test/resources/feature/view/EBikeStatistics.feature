Feature: As a user, I want to see statistics of the e-bike, so that I can monitor the e-bike \\

  Scenario: User see the hire e-bike
    Given The user has sign in and hired a e-bike
    When The user hired a e-bike
    Then The system shows statistics of the e-bike

  Scenario: User see the e-bike in use
    Given The user has sign in and hired a e-bike
    When The user ride the e-bike
    Then The system update and show the statistics of the e-bike
