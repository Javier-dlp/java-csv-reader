# The challenge

This is a Java application, that counts users visited our web-page from different sources.  
Mostly filled with duplicates.

As input you have a csv file, which can go up to GBs, that contains:

```
email,phone,source
test@test.com,123,google.com
test@test.com,,google.com
test1@test.com,321,google.com
```

---

# The solution

This service is a spring boot service with spring data and hibernate.
It uses a Postgres DB which is provided as a docker image.
If it wasn't reading local files, the service is also ready to be deployed via docker.
The service implements flyway for the DB creation and migrations. 

## How to run it

1. Start the DB using docker compose
```
docker-compose up -d
```
2. Run the Spring Boot application
```
./gradlew bootRun
```
The app is now deployed.

## Usage
It has two endpoints available in [Swagger](http://localhost:8080/swagger-ui/index.html#/controller):

- /processFile POST
```
curl -H 'Content-Type: application/json'
 -X POST -d '{"absolutePath": "pathToProject/src/test/resources/user_visit_example.csv"}'
  http://localhost:8080/processFile
```
- /userCount GET
```
curl localhost:8080/userCount
```

## Thoughts

Since loading big files can take a lot of time, I chose to create two endpoints, one to upload files,
another to get the latest count of users from different sources


### Regarding the data structure

Reading a large file in java is simple, as we can go line by line to avoid blowing up the memory.
Given we need to be used large files and reuse the information in the future, it makes sense to have a DB.

With the idea of many duplicates, I decided to keep users with different sources.
Having more information, such as timestamp, the DB structure could have been one-to-many relationship with two tables,
users, and sources; but for the current problem it is faster to simply add the sources to the PK to prevent duplicates
at DB level and insert quickly.

Ideally the app will be deployed with docker as well (as it is prepared), but testing sake and until a better location
for the files, such as S3 buckets, is implemented, is set to run locally. 


### Other thoughts

The set of libraries and technologies was specifically implemented for this exercise with no previous skeleton.
This took most of the time, but I am happy with the result, I think it makes a pretty nice skeleton project for a
wide range of services.
