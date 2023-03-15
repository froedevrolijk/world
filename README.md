# Mastery Day Q3/4

#### How to start the project
Connect to docker container:  
`docker-compose -f docker/docker-compose.yml up -d`

### How to test

- unit tests: `sbt test`
- integration tests: `sbt it:test`
- all tests: `sbt test it:test`

### How to run

- Run the application: `sbt run`

#### Check the running container
Launch a bash terminal within the container:  
`docker exec -it world-db bash`

Connect to postgres database:
`psql -h localhost -U user world`

List databases:  
`\l`

Switch connection to database `world`:  
`\c worlddb`

List tables:  
`\dt`

### Send requests

Check if the application is up and running:
```
curl -k -v -X GET http://localhost:9000/keepalive
```

### Cities
Add city:
```
curl -k -v -X POST http://localhost:9000/add-city \
-H 'Content-Type: text/json' \
-d @- << EOF
{
  "id": "1",
  "name": "Nijmegen",
  "countryCode": "NLD",
  "district": "Gelderland",
  "population": "152463"
}
EOF
```

Delete city: // NOT USED
```
curl -k -v -X DELETE http://localhost:9000/delete-city/394f15cd-e99f-4deb-94b3-4837ce6b6c27
```

### Countries
Get all countries:
```
curl -k -v -X GET http://localhost:9000/get-all-countries
```

Update country:
```
curl -k -v -X PUT http://localhost:9000/update-country/d21beae6-b83b-4051-94f3-3d4c10d4e239 \                                                                                                       ✔
-H 'Content-Type: text/json' \
-d @- << EOF
{
  "name": "Australia",
  "continent": "Europe",
  "region": "Western Europe",
  "surfaceArea": "1000",
  "independenceYear": "2000",
  "population": "10213",
  "gnp": "12412.0",
  "governmentForm": "Republic",
  "headOfState": "Klaas"
}
EOF
```



Find countries:
```
curl -k -v -X POST http://localhost:9000/countries \
-H 'Content-Type: text/json' \
-d @- << EOF
{
  "country": "U%"
}
EOF
```

Delete country:
```
curl -k -v -X DELETE http://localhost:9000/delete-country/09fa682b-ad3c-421e-9484-59d7a05cd6a2
```





## Frontend
Build Docker image of our frontend application:
`docker build -t md-frontend-image .`

Run frontend in a Docker container:
`docker run -it -p 8080:80 --rm --name md-frontend md-frontend-image`

or
`docker-compose -f docker-compose.yml up -d`

## API
`docker run -it -p 9000:9000 --rm --name world-api world-api:0.1`

```json
{
    "cities": [
        {
            "id": "5",
            "name": "Nijmegen",
            "countryCode": "NLD",
            "district": "Gelderland",
            "population": "152463"
        },
        {
            "id": "5",
            "name": "Nijmegen",
            "countryCode": "NLD",
            "district": "Gelderland",
            "population": "152463"
        }
    ]
}
```
