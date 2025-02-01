## Running with Docker
First, build the jar (skip the tests not to have problems with dependencies)
```bash
./mvnw clean package -DskipTests
```

build the docker image by running
```bash
docker build -t dietiestates25 .
```
and then run the environment
```bash
docker-compose up
```

## Running tests
You can run the tests by running the maven test target
```bash
./mvnw test
```