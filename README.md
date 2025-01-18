# Ewallet Project

## Overview
The Ewallet Project is a Java-based backend application implementing a microservices architecture. It allows users to manage wallets, perform transactions, and receive notifications. The project leverages Kafka for asynchronous communication and Spring Boot for API development.

---

## Features

### User Management
- Create, retrieve, and manage users.
- Unique identification of users using email and phone numbers.

### Wallet Management
- Create and manage user wallets.
- Validate wallet balance before transactions.

### Transaction Management
- Handle credit and debit transactions.
- Track transaction status in real-time.

### Notification Service
- Notify users via email for important events (e.g., wallet creation, successful transactions).

### Inter-Service Communication
- Synchronous communication using `RestTemplate`.
- Asynchronous communication using Kafka topics.

### Security
- Spring Security for API authentication and authorization.

---

## Technologies Used

### Backend
- **Java**: Core programming language.
- **Spring Boot**: Framework for building REST APIs.
- **Spring Security**: For securing the application.
- **Spring Data JPA**: For database interaction.

### Messaging
- **Apache Kafka**: For asynchronous communication.

### Database
- **MySQL**: Relational database for data persistence.

---

## Architecture

The project follows a microservices architecture with the following services:

### Services
1. **User-Service**
    - Manages user-related operations.
    - Publishes events to Kafka upon user creation.

2. **Wallet-Service**
    - Manages wallets linked to users.
    - Consumes user creation events to initialize wallets.

3. **Transaction-Service**
    - Handles transactions between wallets.

4. **Notification-Service**
    - Listens to Kafka events for user and transaction notifications.
    - Sends email notifications to users.

---

## Setup Instructions

### Prerequisites
1. Install **Java** (JDK 11 or higher).
2. Install **MySQL** and create a database named `ewallet`.
3. Install **Apache Kafka** and **Zookeeper**.

---

### Steps to Run the Application

1. Clone the repository:
   ```bash
   git clone <repository_url>
   cd ewallet
   ```
2. Build the project:
   ```
   mvn clean install
    ```
3. start all the 4 services:
   1. User-Service
   2. Wallet-Service
   3. Transaction-Service
   4. Notification-Service
