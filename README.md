# Receipt Processor API

## Overview
The **Receipt Processor API** is a RESTful web service that processes receipts and calculates points based on predefined rules. The API is designed to handle receipt submissions, assign unique identifiers, and compute reward points for each receipt.

## Features
- Accepts receipt JSON payloads and generates a unique receipt ID.
- Computes points based on receipt data.
- Retrieves the total points awarded for a given receipt ID.
- Uses an in-memory data store.
- Dockerized for easy deployment and execution.

---

## Tech Stack
- **Java 17**
- **Spring Boot** (REST API development)
- **Maven** (Dependency Management)
- **Docker** (Containerization)

---

## Getting Started

### Prerequisites
Ensure you have the following installed:
- **Java 17+**
- **Maven**
- **Docker** (if running via container)

### Cloning the Repository
```sh
git clone https://github.com/mo-fsdev/receipt-processor.git
cd receipt-processor
```

### Running the Application Locally

#### Using Maven
1. Navigate to the project directory:
   ```sh
   cd receipt-processor
   ```
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```
4. The API should now be running at [http://localhost:8080](http://localhost:8080).

#### Running with Docker
1. **Build the Docker Image**:
   ```sh
   docker build -t receipt-processor .
   ```
2. **Run the Docker Container**:
   ```sh
   docker run -p 8080:8080 receipt-processor
   ```
3. The service will be available at [http://localhost:8080](http://localhost:8080).

---

## API Documentation

### 1. Process Receipt
**Endpoint:** `/receipts/process`  
**Method:** `POST`  
**Description:** Accepts a receipt JSON and returns a unique ID.

### 2. Get Points
**Endpoint:** `/receipts/{id}/points`  
**Method:** `GET`  
**Description:** Retrieves the points associated with a given receipt ID.

---

## Testing the API

### Using Postman
1. Open **Postman**.
2. Send a `POST` request to `http://localhost:8080/receipts/process` with the sample JSON body provided in the instructions.
3. Copy the returned `id` and use it in a `GET` request to `http://localhost:8080/receipts/{id}/points`.

---

## Docker Commands Reference
### Stop Running Container
```sh
docker stop <container_id>
```

### Remove Container
```sh
docker rm <container_id>
```

### List Running Containers
```sh
docker ps
```

---

## Usage Restrictions
While this project is public, it is strictly intended for authorized users only. Unauthorized access, reproduction, or distribution is prohibited. Please contact the repository owner for inquiries regarding permitted usage.
