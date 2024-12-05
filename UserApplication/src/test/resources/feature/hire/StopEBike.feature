Feature: As a user, I want to stop ride e-bike, so that I can end the ride

  Scenario: User stop ride e-bike
  Given The user has sign in with "manuel" and "password"
  When The user ride e-bike "0"
  Then The system stops the ride, notify the user and free the e-bike "0"
