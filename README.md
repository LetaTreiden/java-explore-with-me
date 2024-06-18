# Explore With Me - Java Project Documentation

## Introduction

Explore With Me is a Java-based application designed to facilitate the exploration of events among users. This documentation provides an overview of the project, installation instructions, usage guidelines, and details on the API endpoints.

## Table of Contents

1. [Introduction](#introduction)
2. [Installation](#installation)
3. [Usage](#usage)
4. [API Endpoints](#api-endpoints)
5. [Contributing](#contributing)
6. [License](#license)

## Installation

To set up the project locally, follow these steps:

1. **Clone the repository:**

   ```sh
   git clone https://github.com/LetaTreiden/java-explore-with-me.git
   cd java-shareit
   
2. **Build the project:**

   Ensure you have Maven installed. Run the following command to build the project:
   ```sh
   mvn clean install

3. **Run the application:**

   Use the following command to run the application:
   ```sh
   java -jar target/shareit-0.0.1-SNAPSHOT.jar
   ```

   ## Usage
To use the Explore With Me application, you need to interact with its API. Below are some examples of how to use the API.

Creating a new event:
```
POST /events
Content-Type: application/json

{
    "title": "Hiking Adventure",
    "description": "A fun hiking event",
    "date": "2024-06-15T09:00:00",
    "location": "Mountain Trail",
    "organizerId": 1
}
```
Listing all events:
```
GET /events
```
Adding a new user:
```
POST /users
Content-Type: application/json

{
    "name": "Alice Smith",
    "email": "alice.smith@example.com"
}
```

## API Endpoints
**Event Endpoints**

Create an Event
```
POST /events
Request Body: { "title": "string", "description": "string", "date": "string", "location": "string", "organizerId": number }
```
Get All Events
```
GET /events
```
Get Event by ID
```
GET /events/{id}
```
Update Event
```
PUT /events/{id}
Request Body: { "title": "string", "description": "string", "date": "string", "location": "string" }
```
Delete Event
```
DELETE /events/{id}
```

**User Endpolints**

Create a User
```
POST /users
Request Body: { "name": "string", "email": "string" }
```
Get All Users
```
GET /users
```
Get User by ID
```
GET /users/{id}
```
Update User
```
PUT /users/{id}
Request Body: { "name": "string", "email": "string" }
```
Delete User
```
DELETE /users/{id}
```

## Contributing
**Contributions are welcome!** Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch (git checkout -b feature-branch).
3. Commit your changes (git commit -am 'Add new feature').
4. Push to the branch (git push origin feature-branch).
5. Create a new Pull Request.


## License
This project is licensed under the MIT License
