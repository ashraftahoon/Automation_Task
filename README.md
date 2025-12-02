# Selenium Automation Framework

## Project Overview
This is a Selenium WebDriver automation framework using **Java**, **TestNG**, and **Maven**.  
It is designed to automate web application testing and generate **Allure reports**.

---

## Features
- Page Object Model (POM) design pattern
- Cross-browser testing (Chrome, Firefox, etc.)
- TestNG framework for structured test execution
- Allure reporting for test results
- Logging using Log4j2
- Supports data-driven tests

 ## Environment Setup

1. **Install Java JDK**
   - Java 21
   - Setup java 21 in your device env
   - verify installation from cmd "java -version" 

2. **Install Maven**
   - Maven 3.6+ is required.
   - verify installation from cmd "mvn -v" 
   
4. **Clone the Project**
   git clone https://github.com/ashraftahoon/Automation_Task.git
   cd Automation_Task
   
4. **import project in IntelliJ IDEA.**
   - Open IntelliJ IDEA.
   - Click Open or Import Project.
   - Select your project folder (Automation_Task).
   - Choose Open as Maven project if IntelliJ asks.
  
   ## Run Task (use this commends)
   - open terminal and run "mvn clean install"
   - then run "mvn test"
   - then run "mvn allure:serve" #to show allure report





