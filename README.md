## Running with Docker

If you are on Windows run the script `deploy.ps1`
```powershell
.\deploy.ps1
```

Otherwise, you can build manually.

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
### Setup
If you are on Windows, run:
```powershell
.\test.ps1
```
Otherwise, you can run manually:
```
./mvnw clean package -DskipTests
```
```
docker build -t dietiestates25 .
```
```
docker-compose up postgis
```
### Run tests
```
docker-compose run --rm test
```