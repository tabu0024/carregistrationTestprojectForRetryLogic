@regression
Feature: vehicleRegistrationSearch


  Background: Open browser and the VehicleRegistration site
    Given I go to mysite

    @test1 @test123
    Scenario: Verify result with correct registration number
    Given I am on Drive Away insurance Page
    When I enter correct registration number
    And I click on find vehicle button
    Then I verify Cover Start date
    And I verify Cover end date


  @test2
  Scenario: Verify the date format
    Given I am on Drive Away insurance Page
    When I enter correct registration number
    And I click on find vehicle button
    Then I verify Cover Start date
    And I verify Cover end date
    And date format is correct

  @test3
  Scenario: Verify end date is less than start date
    Given I am on Drive Away insurance Page
    When I enter correct registration number
    And I click on find vehicle button
    And end date is less Then start date

  @test4
  Scenario: Verify start date is not future date
    Given I am on Drive Away insurance Page
    When I enter correct registration number
    And I click on find vehicle button
    And start date is not future date
#  This is destined to fail as the start date we have in sample registration provided is future date in result

  @test5
  Scenario: Verify registration has expired
    Given I am on Drive Away insurance Page
    When I enter correct registration number
    And I click on find vehicle button
    And end date is past date
#   This step is destined to fail as the end date we have in sample registration provided is future date

  @test6
  Scenario Outline: Verify result with incorrect registration number
    Given I am on Drive Away insurance Page
    When I enter incorrect "<registration number>"
    And I click on find vehicle button
    Then I see error message

    Examples:
      | registration number |
      | test123             |
      | testing123          |

  @test7
  Scenario: Verify result when registration number is empty
    Given I am on Drive Away insurance Page
    And I click on find vehicle button
    Then I see user message to enter registration number

