Feature: Sample feature to test API methods on JSON request

  @example
  Scenario Outline: Perform following API operations

    Given "GET" list of all objects

    When "GET" list of objects by ID's "<multipleIDs>"
    Then Verify "<multipleIDs>" present in response

    When "GET" single object by "<ID>"
    Then Verify "<ID>" present in response

    Given Take base payload of "<payload>"
    And Update "<payload>" with "id" as "<new_id>"
    And Update "<payload>" with "name" as "<name>"
    And "POST" add an object
    Then Verify "id" present as "<new_id>" in response
    Then Verify "name" present as "<name>" in response

    Given Take base payload of "<payload>"
    And Update "<payload>" with "id" as "<new_id>"
    And Update "<payload>" with "name" as "<modified_name>"
    And "PUT" update an object of "<new_id>"
    And "DELETE" object of "<delete_id>"

    Examples:
      | multipleIDs | ID | payload | new_id | name                  | modified_name     |delete_id|
      | 1,2,3       | 1  | object  | 14     | Apple Mac Mini M4 Pro | Apple Mac mini M2 |7        |


  @example
  Scenario Outline: Perform following API operations

    Given "GET" list of all objects

    When "GET" list of objects by ID's "<multipleIDs>"
    Then Verify "<multipleIDs>" present in response

    When "GET" single object by "<ID>"
    Then Verify "<ID>" present in response

    Given Take base payload of "<payload>"
    And Update "<payload>" with "id" as "<new_id>"
    And Update "<payload>" with "name" as "<name>"
    And "POST" add an object
#    Then Verify "id" present as "<new_id>" in response
    Then Verify "name" present as "<name>" in response

    Given Take base payload of "<payload>"
    And Update "<payload>" with "id" as "<generated_id>"
    And Update "<payload>" with "name" as "<modified_name>"
    And "PUT" update an object of "<generated_id>"
    Then Verify "name" present as "<modified_name>" in response

    And "DELETE" object of "<generated_id>"
    Then Verify "message" present as "Object with id = <generated_id> has been deleted." in response

    Examples:
      | multipleIDs | ID | payload | new_id | name                  | generated_id | modified_name     |delete_id|
      | 1,2,3       | 1  | object  | 14     | Apple Mac Mini M4 Pro | GENERATED_ID | Apple Mac mini M2 |7        |