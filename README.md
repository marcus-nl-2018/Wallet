# Wallet Service

A microservice that allows users to manage their digital wallets: create wallets, deposit and withdraw funds, transfer between wallets, and query current or historical balances.

## Features

* Create user wallets
* Deposit & withdraw funds
* Transfer money between wallets
* Retrieve current and historical balances
* Full auditability of transactions
* Designed for extensibility and production-readiness

## Tech Stack

* Java 17
* Spring Boot 3
* PostgreSQL
* Spring Data JPA
* JUnit 5 & Mockito
* Maven

## Project Structure
```py
src/main/java/com/recargapay/wallet/
├── controller/      # REST endpoints
├── dto/             # Request and response models
├── entity/          # JPA entities
├── enums/           # Enum types
├── exception/       # Custom exceptions & global handler
├── repository/      # Spring Data repositories
├── service/         # Business logic
```

## Requirements

* Java 17+
* Maven 3.8+
* PostgreSQL running locally (default: walletdb, user: postgres, password: password)

## Setup
```py
# Clone the repository
git clone https://github.com/your-username/wallet-service.git
cd wallet-service

# Start PostgreSQL and create DB (if not exists)
createdb walletdb

# Run the app
./mvnw spring-boot:run
```
## API Endpoints

### Create Wallet 
```py
http
POST /api/wallets
{
"userId": 123
}
```
### Deposit
```py
http
POST /api/wallets/{walletId}/deposit
{
"amount": 100.00
}
```

### Withdraw
```py
http
POST /api/wallets/{walletId}/withdraw
{
"amount": 50.00
}
```

### Transfer
```py
http
POST /api/wallets/{walletId}/transfer
{
"toWalletId": 2,
"amount": 25.00
}
```

### Get Current Balance
```py
http
GET /api/wallets/{walletId}/balance
```

### Get Historical Balance
```py
http
GET /api/wallets/{walletId}/balance?at=2024-01-01T12:00:00
```

## Tests
```py
# Run unit and integration tests
./mvnw test
```

## Build & Run Instructions
```py
# Build jar
./mvnw clean package

# Build and run containers
docker-compose up --build
```

## Access Swagger UI
After running the app:
```py
http://localhost:8080/swagger-ui.html
```
Or:
```py
http://localhost:8080/swagger-ui/index.html
```

## Database Setup (PostgreSQL)
This service uses a PostgreSQL database to store wallet and transaction data.

### Option 1: Local Installation
Make sure PostgreSQL is installed and running on port 5432. Then:
```py
CREATE DATABASE walletdb;
CREATE USER postgres WITH ENCRYPTED PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE walletdb TO postgres;

-- Users Table (for reference, not used directly by Wallet Service)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Wallets Table
CREATE TABLE IF NOT EXISTS wallets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance NUMERIC(18, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);

-- Transactions Table (Audit Trail)
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    wallet_id BIGINT NOT NULL,
    related_wallet_id BIGINT, -- for transfers (can be NULL)
    type VARCHAR(20) NOT NULL CHECK (type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER')),
    amount NUMERIC(18, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_wallet FOREIGN KEY (wallet_id)
        REFERENCES wallets (id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_wallet_user ON wallets (user_id);
CREATE INDEX idx_transaction_wallet_created ON transactions (wallet_id, created_at DESC);
```

### Option 2: Docker (Recommended)
Use the provided docker-compose.yml to spin up PostgreSQL:
```py
docker-compose up -d db

```
This will start a container with:
* DB Name: walletdb
* Username: postgres
* Password: password
* Port: 5432

## 📓 Design Considerations

* Transactional Safety: All operations are wrapped in @Transactional blocks.
* Auditability: Every deposit, withdrawal, and transfer creates a Transaction record.
* Historical Balance: Reconstructs past balances by summing transactions before a given timestamp.
* Scalability: Stateless service, database can be horizontally partitioned.

## ❗ Assumptions Made
* A wallet is uniquely tied to a userId.
* No support for multi-currency wallets (can be added later).
* Timestamps are stored in UTC.
* Wallet creation initializes with a zero balance.
