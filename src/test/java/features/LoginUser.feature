Feature: Verify that User is able to login successfully

  @login
  Scenario Outline: Perform login API operations

    Given Take base payload of "<payload>"
    And Update "login" with "email" as "<email>"
    And Update "login" with "password" as "<password>"
    When User sends "login" API request and get response code as <response_code>
    Then Verify "type" present as "<user_type>" in response
    Then Verify "firstname" present as "<firstname>" in response
    Then Verify "lastname" present as "<lastname>" in response
    Then Verify "status" present as "<status>" in response
    Then Verify "statusCode" present as "<statusCode>" in response
    Then Verify "message" present as "<message>" in response

    Examples:
      | payload | email           | password | user_type   | firstname   | lastname    | status      | response_code | statusCode  | message              |
      | login   | admin@gapmap.no | Xtm4923  | ADMIN       | Admin       | adminji     | true        | 200           | NOT PRESENT | NOT PRESENT          |
      | login   | admin@gapmap.no | Xtm49233 | NOT PRESENT | NOT PRESENT | NOT PRESENT | NOT PRESENT | 400           | 400         | Invalid credentials! |

  @logout
  Scenario Outline: Perform logout API operations

    Given Take base payload of "<payload>"
    And Update "login" with "email" as "<email>"
    And Update "login" with "password" as "<password>"
    When User sends "login" API request and get response code as <response_code>
    Then Verify "type" present as "<user_type>" in response
    Then Verify "firstname" present as "<firstname>" in response
    Then Verify "lastname" present as "<lastname>" in response
    Then Verify "status" present as "<status>" in response
    Then Verify "statusCode" present as "<statusCode>" in response
    Then Verify "message" present as "<message>" in response
    When User sends "logout" API request and get response code as <response_code>

    Examples:
      | payload | email           | password | user_type   | firstname   | lastname    | status      | response_code | statusCode  | message              |
      | login   | admin@gapmap.no | Xtm4923  | ADMIN       | Admin       | adminji     | true        | 200           | NOT PRESENT | NOT PRESENT          |
