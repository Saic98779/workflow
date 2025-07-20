# Test the /programs endpoint
$url = "http://localhost:8081/workflow/api/programs"
Write-Host "Testing URL: $url"

try {
    $response = Invoke-RestMethod -Uri $url -Method Get -ContentType "application/json" -ErrorAction Stop
    Write-Host "Success! Response:"
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error occurred:"
    Write-Host "StatusCode:" $_.Exception.Response.StatusCode.value__
    Write-Host "StatusDescription:" $_.Exception.Response.StatusDescription
    
    # Try to get the response body for more details
    try {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $reader.DiscardBufferedData()
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response body:"
        $responseBody
    } catch {
        Write-Host "Could not read response body: $_"
    }
}