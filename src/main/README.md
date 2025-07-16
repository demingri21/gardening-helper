**Garden Helper** is a lightweight Java-based web app that helps users track their plants and watering schedules. It uses a modern AWS backend and serves as a practical demonstration of full-stack cloud-integrated development.

**To Run**
[Requires java to run]
Download GardenHelper-1.0-SNAPSHOT.jar
You should see, Server running on http://localhost:8080/
Go to http://localhost:8080/index.html in your webbrowser

**Features**

* Add plants with custom watering intervals and days since last watered.
* View all plants associated with your username.
* Stores data in AWS DynamoD* via AWS Lambda APIs.
* Web interface built using HTML, JavaScript, and SparkJava.

**Technologies Used**

* Java 17
* Apache Maven (for dependency and build management)
* SparkJava (for REST API routing)
* AWS Lambda (for backend business logic)
* Amazon DynamoDB (as the persistent NoSQL database)
* Gson (for JSON serialization/deserialization)
* HTML/CSS/JavaScript (for frontend)
