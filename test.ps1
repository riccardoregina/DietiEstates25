# Stop execution on any error
$ErrorActionPreference = "Stop"

Write-Host "Starting test process..." -ForegroundColor Green

try {
    # Step 0: Clear terminal
    Clear-Host

    # Step 1: Bring down existing containers
    Write-Host "Stopping existing containers..." -ForegroundColor Yellow
    docker-compose down
    if (-not $?) { throw "Failed to stop containers" }

    # Step 2: Remove existing images
    Write-Host "Removing existing images..." -ForegroundColor Yellow
    $images = docker images --filter "reference=dietiestates25*" -q
    if ($images) {
        docker rmi $images
        if (-not $?) { throw "Failed to remove images" }
    }

    # Step 3: Build with Maven
    Write-Host "Building application with Maven..." -ForegroundColor Yellow
    ./mvnw clean package -DskipTests
    if (-not $?) { throw "Maven build failed" }

    # Step 4: Build Docker image
    Write-Host "Building Docker image..." -ForegroundColor Yellow
    docker build -t dietiestates25 .
    if (-not $?) { throw "Docker build failed" }

    # Step 5: Start db container
    Write-Host "Starting db container..." -ForegroundColor Yellow
    docker-compose up postgis
    if (-not $?) { throw "Failed to start db container" }

} catch {
    Write-Host "Error: $_" -ForegroundColor Red
    Write-Host "Tests run failed!" -ForegroundColor Red
    exit 1
}

Write-Host "Tests completed successfully!" -ForegroundColor Green
