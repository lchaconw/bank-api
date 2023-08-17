# Bank Inc Card Management System

Bank Inc's Card Management System is a RESTful API designed to manage debit and credit cards, providing functionalities like generating card numbers, activating cards, blocking cards, checking balances, and processing transactions.

## Features

- **Card Generation**: Generate a unique card number based on a product ID.
- **Card Activation**: Activate a previously inactive card.
- **Card Blocking**: Block a card from being used for transactions.
- **Balance Management**: Load balance onto a card and retrieve the balance of a card.
- **Transaction Processing**: Process a purchase transaction and handle its possible annulation.

## Prerequisites

- Java 17
- MySQL
- Maven

## Setup and Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/lchaconw/bank-api
    cd bank-api
    ```

2. Update `application.properties` with your database credentials.

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your-database-name
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   ```

3. Build the project:
    ```bash
    mvn clean install
    ```

4. Run the application:
    ```bash
    mvn spring-boot:run
    ```

## API Endpoints

| Method | Endpoint                              | Description                         |
|--------|---------------------------------------|-------------------------------------|
| POST   | `/card/{productId}/number`            | Generate card number                |
| POST   | `/card/enroll`                        | Activate a card                     |
| DELETE | `/card/{cardId}`                      | Block a card                        |
| POST   | `/card/balance`                       | Load balance onto a card            |
| GET    | `/card/balance/{cardId}`              | Get the balance of a card           |
| POST   | `/transaction/purchase`               | Process a purchase transaction      |
| GET    | `/transaction/{transactionId}`        | Retrieve details of a transaction   |
| POST   | `/transaction/anulation`              | Anulate a transaction               |

## Testing

The project includes unit tests written using JUnit 5 and Mockito. To run the tests:

```bash
mvn test
```

## Postman Collection

A Postman collection is included in the project root directory. It contains examples of requests for each endpoint.

## Swagger Documentation

The project includes Swagger documentation. To access it, run the application and go to `http://localhost:8080/swagger-ui.html` in your browser.