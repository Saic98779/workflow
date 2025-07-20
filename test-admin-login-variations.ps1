# Test script for login endpoint with admin credentials and various configurations
Write-Host "Testing login endpoint with different configurations"

# Define the login request payload with the provided credentials
$body = @{
    email = "admin@gmail.com"
    password = "Password@123"
} | ConvertTo-Json

# Test 1: Default local origin
Write-Host "`n[Test 1] Using local origin (http://localhost:8081)"
$headers1 = @{
    "Content-Type" = "application/json"
    "Origin" = "http://localhost:8081"
}

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8081/workflow/api/auth/login" -Method Post -Body $body -Headers $headers1 -ContentType "application/json" -ErrorAction Stop
    Write-Host "Login successful!"
    Write-Host "Response: $($response | ConvertTo-Json -Depth 4)"
} catch {
    Write-Host "Login failed with status code: $($_.Exception.Response.StatusCode.value__)"
    Write-Host "Error message: $($_.Exception.Message)"
}

# Test 2: Different origin (simulating cross-origin request)
Write-Host "`n[Test 2] Using different origin (http://example.com)"
$headers2 = @{
    "Content-Type" = "application/json"
    "Origin" = "http://example.com"
}

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8081/workflow/api/auth/login" -Method Post -Body $body -Headers $headers2 -ContentType "application/json" -ErrorAction Stop
    Write-Host "Login successful!"
    Write-Host "Response: $($response | ConvertTo-Json -Depth 4)"
} catch {
    Write-Host "Login failed with status code: $($_.Exception.Response.StatusCode.value__)"
    Write-Host "Error message: $($_.Exception.Message)"
}

# Test 3: No Origin header
Write-Host "`n[Test 3] No Origin header"
$headers3 = @{
    "Content-Type" = "application/json"
}

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8081/workflow/api/auth/login" -Method Post -Body $body -Headers $headers3 -ContentType "application/json" -ErrorAction Stop
    Write-Host "Login successful!"
    Write-Host "Response: $($response | ConvertTo-Json -Depth 4)"
} catch {
    Write-Host "Login failed with status code: $($_.Exception.Response.StatusCode.value__)"
    Write-Host "Error message: $($_.Exception.Message)"
}

# Test 4: Using incorrect context path
Write-Host "`n[Test 4] Using incorrect context path (/auth/login instead of /workflow/api/auth/login)"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8081/auth/login" -Method Post -Body $body -Headers $headers1 -ContentType "application/json" -ErrorAction Stop
    Write-Host "Login successful!"
    Write-Host "Response: $($response | ConvertTo-Json -Depth 4)"
} catch {
    Write-Host "Login failed with status code: $($_.Exception.Response.StatusCode.value__)"
    Write-Host "Error message: $($_.Exception.Message)"
}

# Test 5: Using incorrect HTTP method (GET instead of POST)
Write-Host "`n[Test 5] Using incorrect HTTP method (GET instead of POST)"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8081/workflow/api/auth/login" -Method Get -Headers $headers1 -ErrorAction Stop
    Write-Host "Request successful!"
    Write-Host "Response: $($response | ConvertTo-Json -Depth 4)"
} catch {
    Write-Host "Request failed with status code: $($_.Exception.Response.StatusCode.value__)"
    Write-Host "Error message: $($_.Exception.Message)"
}