# Mastery Day Q3/4

#### Start project
Connect to docker container:  
`docker-compose -f docker-compose.yml up -d`

Then:  
`sbt run`

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
curl -k -v -X GET http://localhost:8086/keepalive \
-H 'Content-Type: text/json'
```

Find countries:
```
curl -k -v -X POST http://localhost:8086/countries \
-H 'Content-Type: text/json' \
-d @- << EOF
{
  "country": "U%"
}
EOF
```

Add city:
```
curl -k -v -X POST http://localhost:8086/add-city-single \
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

Add multiple cities:
```
curl -k -v -X POST http://localhost:8086/add-city-many \
-H 'Content-Type: text/json' \
-d @- << EOF
{
    "cityList": [
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
EOF
```

## Frontend
Build Docker image of our frontend application:
`docker build -t md-frontend-image .`

Run frontend in a Docker container:
`docker run -it -p 8080:80 --rm --name md-frontend md-frontend-image`

or
`docker-compose -f docker-compose.yml up -d`

## API
`docker run -it -p 9000:9001 --rm --name world-api world-api:0.1`

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

### TODOS
- add logging
- add error handling
- add unit / integration tests
- add database migration

Furthermore: 
- add containers setup locally
- deploy containers to GCP
- Github Actions

