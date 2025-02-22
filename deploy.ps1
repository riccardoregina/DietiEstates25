# Stop execution on any error
$ErrorActionPreference = "Stop"

Write-Host "Starting deployment process..." -ForegroundColor Green

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

    # Step 5: Start containers
    Write-Host "Starting containers..." -ForegroundColor Yellow
    docker-compose up
    if (-not $?) { throw "Failed to start containers" }

} catch {
    Write-Host "Error: $_" -ForegroundColor Red
    Write-Host "Deployment failed!" -ForegroundColor Red
    exit 1
}

Write-Host "Deployment completed successfully!" -ForegroundColor Green
