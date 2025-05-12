# Parcel Tracking API

This project aims to create a RESTful API using Spring Boot that allows users to track parcels by their tracking number and manage their tracked parcels.

## API Endpoints

### Track a Parcel
**GET** `/api/track/{userId}/{trackingNumber}`
Retrieve the status of a parcel for a specific user using its tracking number.
### Send a Parcel
**POST** `/api/packages/send`  
Send a new package to a specified locker.

**Request body**:  
`{ "userId": "String", 
"trackingNumber": "String", 
"lockerId": "String", 
"packageSize": "String" }`

## Sample Data

Data structure to simulate the tracking information for a few predefined tracking numbers associated with user IDs.

## Technologies Used

- Java 21
- Spring Boot
- Maven

## Setup and Installation

1. Clone the repository:  
   `git clone https://github.com/czarniecka/parcel-tracking-api.git`  
   `cd parcel-tracking-api`

2. Build the project:  
   `mvn clean install`

3. Run the application:  
   `mvn spring-boot:run`  
   By default, the app will be available at: `http://localhost:8080`



## Running Tests

To run all unit tests:  
`mvn test`

## Author

Aleksandra Czarniecka