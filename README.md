# Periplus E-commerce QA Automation Test

## Description
This is a Java-based automation testing framework designed for testing e-commerce functionalities on [Periplus.com](https://www.periplus.com/), an online bookstore platform. The framework uses Selenium WebDriver to simulate real user interactions, including:

- User login using credentials from environment variables
- Product search by keyword
- Adding random products to the shopping cart
- Verifying cart item count updates after each addition

The main entry point is `PeriplusTest.java`, which demonstrates an end-to-end test flow. The project follows Maven standard directory structure and is configured for Java 11.

**Note**: The `App.java` is a Maven-generated stub and not used in automation flows.

## Tech Stack
- **Language**: Java 11
- **Build Tool**: Maven
- **Web Automation**: Selenium WebDriver 4.18.0 (ChromeDriver)
- **Testing Framework**: TestNG 7.9.0
- **Environment Management**: dotenv-java 3.0.0
- **Other**: JUnit 4.11 (for additional tests), WebDriverWait, Actions, JavascriptExecutor

## Prerequisites
- Java 11 JDK installed
- Maven 3.x installed
- Google Chrome browser installed
- Git (for cloning)

## Setup
1. **Clone the repository**:
   
```
   git clone https://github.com/steven-adrnn/periplus-test.git
   cd periplus-test
   
```

2. **Install dependencies**:
   
```
   mvn clean install
   
```

3. **Edit `.env.example` file** in the project root with your Periplus credentials:
   
```
   PERIPLUS_EMAIL=your_email@example.com
   PERIPLUS_PASSWORD=your_password
   SEARCH_KEYWORD=your_search_term
   
```

## Running the Tests
1. **Run all tests**:
   
```
   mvn clean test
   
```

2. **Run specific test class**:
   
```
   mvn test -Dtest=PeriplusTest
   
```

3. **Run with verbose output**:
   
```
   mvn test -Dsurefire.useFile=false
   
```

The test will:
- Launch Chrome browser
- Navigate to Periplus.com
- Login automatically
- Search for products
- Add 1 random product to cart (configurable via `itemsToAdd`)
- Verify cart updates
- Print progress to console
- Close browser

## Test Output Example
```
Total Cart Item: 0
Successfully added item-1. Total Cart Item: 1
Test completed!
```

## Project Structure
```
.
├── pom.xml                 # Maven configuration & dependencies
├── src/
│   ├── main/java/com/openway/App.java  # Maven stub
│   └── test/java/com/openway/
│       ├── AppTest.java    # Standard Maven test
│       └── PeriplusTest.java # Main automation test
├── .env                    # Environment variables (create manually)
├── .gitignore
└── target/                 # Compiled classes & reports
```