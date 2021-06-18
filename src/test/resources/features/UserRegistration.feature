@Registration @User
Feature: a user can be registered

    Scenario: the email is already registered
      Given I have the following users registered
        | name   | email             | password |
        | DaveM  | Davey@test.com    | 12345    |
        | SallyT | Sally@testing.net | Test123  |
      When I register a user with email 'Davey@test.com'
      Then I should receive an error indicating the user already exists
      And there should still be only 1 registered user with email 'Davey@test.com'

    Scenario: Previously Unregistered User
      Given I have the following users registered
        | name   | email             | password |
        | JerryK | Jerry@test.com    | 12345    |
        | MariaL | Maria@testing.net | Test123  |
      When I register a user with email 'Davey1995@test.com'
      Then there should be a registered user with email 'Davey1995@test.com'

    Scenario: The email address is invalid
      Given I register a user with email 'BillKlark@test,com'
      Then I should receive an error indicating the email is invalid

    Scenario: The email address is too long
      Given I register a user with email
    """
    CallMeIshmaelSomeTimeAgoNeverMindHowLongPreciselyHaving
    LittleOrNoMoneyInMyPurseAndNothingInTheParticularTo
    InterestMeOnShoreIThoughtIWouldSailAboutALittleAndSeeTheWatery
    PartsOfTheWorld@test.com
    """
      Then I should receive an error indicating the email is invalid