Feature: As a user, I want to see my credit, so that I can monitor financial situation \\

  Scenario: User see credit
    Given The user has sign in
    When The user open the application
    Then The system shows the credit

  Scenario: User see payments
    Given The user has sign in
    When The user pay the unlock hire service
    And pay the ride service
    Then The system shows the residual credit
