Feature: Filters the current area/route query.
  The app needs to filter data when directed by the user directly or the system
  i.e choosing vehicle == bike,

  Scenario: Filter for Pedestrians
    Given I have done an area or route search
    And I have selected the Pedestrian filter
    When I click filter
    Then only crashes with a pedestrian involved are loaded into the controllers

  Scenario: Filter by year
    Given I have done an area or route search
    And I have selected the start year as 2016
    And I have selected the end year as 2022
    When I click filter
    Then only crashes between 2016 and 2022 are loaded into the controllers