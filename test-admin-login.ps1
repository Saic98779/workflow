# Test script for login endpoint with admin credentials
Write-Host "Testing login endpoint at http://localhost:8081/workflow/api/auth/login with admin credentials"

# Define the login request payload with the provided credentials
$body = @{
    email = "admin@gmail.com"
    password = "Password@123"
} | ConvertTo-Json

# Set headers
$headers = @{
    "Content-Type" = "application/json"
    "Origin" = "http://localhost:8081"
}

# Send the request
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8081/workflow/api/auth/login" -Method Post -Body $body -Headers $headers -ContentType "application/json" -ErrorAction Stop
    Write-Host "Login successful!"
    Write-Host "Response: $($response | ConvertTo-Json -Depth 4)"
} catch {
    Write-Host "Login failed with status code: $($_.Exception.Response.StatusCode.value__)"
    Write-Host "Error message: $($_.Exception.Message)"
    
    # Try to get more details from the response
    try {
        $errorDetails = $_.ErrorDetails.Message | ConvertFrom-Json
        Write-Host "Error details: $($errorDetails | ConvertTo-Json -Depth 4)"
    } catch {
        Write-Host "No additional error details available"
    }
}