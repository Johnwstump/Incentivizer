@Challenges
Feature: We must be able to determine when a challenge has succeeded or failed

  Scenario Outline: the challenge met or exceeded its targets
    Given I have a challenge
    And the challenge has a reward triggered when there are <postComparator> <postTarget> posts and <pointComparator> <pointTarget> points
    When the challenge has <postQuantity> posts and <pointQuantity> points
    Then the challenge should show as succeeded

    Examples:
      | postComparator | postTarget | postQuantity | pointComparator | pointTarget | pointQuantity |
      | ">"            | 5          | 6            | "<"             | 4           | 3.0           |
      | ">="           | 5          | 5            | "<"             | 4           | 3.0           |
      | "<="           | 5          | 5            | "<"             | 4           | 3.0           |
      | "<"            | 5          | 4            | "<"             | 4           | 3.0           |
      | ">="           | 5          | 5            | "<"             | 4           | 3.0           |
      | ">="           | 5          | 5            | "<="            | 4           | 4.0           |
      | ">="           | 5          | 5            | ">="            | 4           | 4.0           |
      | ">="           | 5          | 5            | ">"             | 4           | 5.0           |
      | "="            | 5          | 5            | "="             | 4           | 4.0           |

  Scenario Outline: the challenge has been failed
    Given I have a challenge
    And the challenge has a reward triggered when there are <postComparator> <postTarget> posts and <pointComparator> <pointTarget> points
    When the challenge has <postQuantity> posts and <pointQuantity> points
    Then the challenge should show as failed

    Examples:
      | postComparator | postTarget | postQuantity | pointComparator | pointTarget | pointQuantity |
      | "<"            | 5          | 5            | ">"             | 4           | 3.0           |
      | "<="           | 5          | 6            | ">"             | 4           | 3.0           |
      | ">"            | 5          | 6            | "<"             | 4           | 4.0           |
      | ">"            | 5          | 6            | "<="            | 4           | 5.0           |
      | "<"            | 5          | 5            | "<"             | 4           | 4.0           |
      | "<="           | 5          | 6            | "<="            | 4           | 5.0           |
      | "="            | 5          | 6            | "<="            | 4           | 4.0           |
      | "="            | 5          | 5            | "="             | 4           | 4.5           |

  Scenario Outline: the challenge is ongoing
    Given I have a challenge
    And the challenge has a reward triggered when there are <postComparator> <postTarget> posts and <pointComparator> <pointTarget> points
    When the challenge has <postQuantity> posts and <pointQuantity> points
    Then the challenge should show as neither succeeded nor failed

    Examples:
      | postComparator | postTarget | postQuantity | pointComparator | pointTarget | pointQuantity |
      | "<"            | 5          | 4            | ">"             | 4           | 3.0           |
      | "<="           | 5          | 4            | ">="            | 4           | 3.0           |
      | ">"            | 5          | 4            | ">"             | 4           | 3.0           |
      | ">="           | 5          | 4            | ">="            | 4           | 3.0           |
      | "="            | 5          | 4            | "="             | 4           | 3.0           |
