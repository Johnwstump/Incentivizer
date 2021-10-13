@Challenges
Feature: Points must be correctly calculated for any given challenge goal/entry

  Scenario Outline: the challenge uses a threshold point strategy
    Given I have a challenge
    And the challenge has a goal where <quantity> points are earned whenever an entry is <comparator> <threshold>
    When I create an entry with value <val>
    Then I should receive <points> points

    Examples:
      | quantity | comparator | threshold | val | points |
      | 1        | "<"        | 5         | 4   | 1.0    |
      | 1        | "<"        | 5         | 6   | 0.0    |
      | 1        | ">"        | 1         | 2   | 1.0    |
      | 1        | ">"        | 1         | 1   | 0.0    |
      | 2        | ">="       | 2         | 3   | 2.0    |
      | 2        | ">="       | 2         | 1   | 0.0    |
      | 1        | "<="       | 4         | 4   | 1.0    |
      | 1        | "<="       | 4         | 5   | 0.0    |

  Scenario Outline: the challenge uses an incremental point strategy
    Given I have a challenge
    And the challenge has a goal where <quantity> points are earned for every <increment> <comparator> <threshold>
    When I create an entry with value <val>
    Then I should receive <points> points

    Examples:
      | quantity | increment | comparator | threshold | val | points |
      | 1        | 1         | "<"        | 5         | 4   | 1.0    |
      | 1        | 2         | "<"        | 5         | 3   | 1.0    |
      | 1        | 2         | "<"        | 5         | 4   | 0.5    |
      | 1        | 2         | "<"        | 5         | 0   | 2.5    |
      | 1        | 1         | "<"        | 5         | 6   | 0.0    |
      | 1        | 1         | "<"        | 5         | 0   | 5.0    |

  Scenario Outline: the operators must be used correctly in determining the point total
    Given I have a challenge
    And the challenge has a goal where 1 points are earned whenever an entry is <comparator> 10
    When I create an entry with value <value>
    Then I should receive <points> points

    Examples:
      | comparator | value | points |
      | "<"        | 1     | 1.0    |
      | "<"        | 10    | 0.0    |
      | "<"        | 15    | 0.0    |
      | "<="       | 1     | 1.0    |
      | "<="       | 10    | 1.0    |
      | "<="       | 15    | 0.0    |
      | "="        | 1     | 0.0    |
      | "="        | 10    | 1.0    |
      | "="        | 15    | 0.0    |
      | ">"        | 1     | 0.0    |
      | ">"        | 10    | 0.0    |
      | ">"        | 15    | 1.0    |
      | ">="       | 1     | 0.0    |
      | ">="       | 10    | 1.0    |
      | ">="       | 15    | 1.0    |

  Scenario: A challenge with multiple threshold entries must correctly calculate the point total
    Given I have a challenge
    And the challenge has a goal where 1 points are earned for every 2 '<' 15
    When I create an entry with value 15
    And I create an entry with value 10
    And I create an entry with value 5
    Then I should receive 7.5 points

  Scenario: A challenge with multiple incremental entries must correctly calculate the point total
    Given I have a challenge
    And the challenge has a goal where 5 points are earned whenever an entry is '<' 15
    When I create an entry with value 10
    And I create an entry with value 15
    And I create an entry with value 2
    Then I should receive 10.0 points