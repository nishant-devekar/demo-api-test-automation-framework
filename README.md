# 🧪 demo-api-test-automation-framework

A fast, clean API test automation framework built for real teams.  
Use it to test any RESTful API with simple, human-readable scenarios.

> ✊ No QA is less than a Dev — this framework helps prove it.

---

## ✅ Use Cases

- Validate API responses before frontend is ready
- Automate post-deployment sanity checks
- Run regression tests on any environment
- Integrate with CI/CD pipelines effortlessly

---

## 🚀 How to Use

1. **Write a test**  
   Describe what the API should do in plain English using `.feature` files.

2. **Run it**  
   Execute with one Maven command — no coding required if your steps exist.

3. **Read the report**  
   Get instant feedback on what passed or failed.

---

## 🧩 Example

```gherkin
Feature: User API

  Scenario: Get user by ID
    Given the user service is up
    When I fetch the user with ID 5
    Then the response code should be 200
    And the user's name should be "Sarah"
````

---

## 🛠 Requirements

* Java 11+
* Maven

Run tests with:

```bash
mvn test
```

---

## 📊 Output

* Test results in terminal
* HTML reports in `/target` folder

---

## 👥 For Teams

* Easy to read, write, and share tests
* Keeps Devs and QAs aligned
* Works well in fast-moving, agile sprints

---

## 💡 Reminder

> Great testers don't just "verify" — they challenge.
> This repo is your toolkit to do that, confidently.

---

## 📫 Want to contribute?

Fork it, improve it, PR it. Or just use it and do great work.

```
Let me know if you want this adapted for CI/CD pipelines or Docker-based execution too.
```
