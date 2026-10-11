<#
.SYNOPSIS
  Builds EcoFlow and starts the five apps (4 microservices + web-app), each in its own terminal window.

.EXAMPLE
  .\scripts\start-all.ps1 -Docker    # starts PostgreSQL with docker compose first
  .\scripts\start-all.ps1            # you already have PostgreSQL running (see .env.example + init-local-db.ps1)
  .\scripts\start-all.ps1 -SkipBuild # nothing changed since the last build

  Stop everything with .\scripts\stop-all.ps1 (or close the windows).
#>
[CmdletBinding()]
param(
    [switch]$Docker,     # run `docker compose up -d --wait` first
    [switch]$SkipBuild   # skip the initial build (use when nothing changed)
)

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
. "$PSScriptRoot\_common.ps1"
Set-Location $root

Initialize-JavaHome
Import-DotEnv (Join-Path $root '.env')

# Start order matters a little: water-service first (supply- and operations-service call it), web-app last.
$apps = [ordered]@{
    'user-service'       = 8081
    'water-service'      = 8082
    'supply-service'     = 8083
    'operations-service' = 8084
    'web-app'            = 8000
}
foreach ($name in $apps.Keys) {
    $busy = Get-NetTCPConnection -State Listen -LocalPort $apps[$name] -ErrorAction SilentlyContinue
    if ($busy) { throw "Port $($apps[$name]) ($name) is already in use. Run .\scripts\stop-all.ps1 or free the port." }
}

if ($Docker) {
    Write-Host '> Starting PostgreSQL (docker compose) ...' -ForegroundColor Cyan
    docker compose up -d --wait
    if ($LASTEXITCODE -ne 0) { throw 'docker compose failed. Is Docker Desktop running?' }
}

if (-not $SkipBuild) {
    Write-Host '> Building (first run downloads dependencies, this can take a few minutes) ...' -ForegroundColor Cyan
    & (Join-Path $root 'mvnw.cmd') -B -q -DskipTests package
    if ($LASTEXITCODE -ne 0) { throw 'Build failed - fix the errors above.' }
}

# New windows inherit JAVA_HOME and the .env values from this process.
foreach ($name in $apps.Keys) {
    Write-Host "> Starting $name on port $($apps[$name]) ..." -ForegroundColor Cyan
    $command = "`$Host.UI.RawUI.WindowTitle = '$name'; Set-Location '$root'; .\mvnw.cmd -pl $name spring-boot:run"
    Start-Process powershell.exe -ArgumentList '-NoExit', '-Command', $command
    Start-Sleep -Seconds 2
}

Write-Host '> Waiting for the apps to become healthy (first start also loads the Tamil Nadu data) ...' -ForegroundColor Cyan
$deadline = (Get-Date).AddSeconds(240)
$pending = New-Object System.Collections.Generic.List[string]
$pending.AddRange([string[]]@($apps.Keys))
while ($pending.Count -gt 0 -and (Get-Date) -lt $deadline) {
    foreach ($name in @($pending)) {
        try {
            $r = Invoke-WebRequest "http://localhost:$($apps[$name])/actuator/health" -UseBasicParsing -TimeoutSec 2
            if ($r.StatusCode -eq 200) { Write-Host "  $name is UP" -ForegroundColor Green; [void]$pending.Remove($name) }
        }
        catch { }
    }
    if ($pending.Count -gt 0) { Start-Sleep -Seconds 3 }
}

if ($pending.Count -gt 0) {
    Write-Warning ("Not healthy yet: " + ($pending -join ', ') + ". Look at their windows for the error.")
}
else {
    Write-Host ''
    Write-Host 'Ready!  Staff:   http://localhost:8000   (admin / admin123  or  staff / staff123)' -ForegroundColor Green
    Write-Host '        Public:  http://localhost:8000/report   and   http://localhost:8000/track'
    Write-Host '        APIs:    http://localhost:8081..8084/swagger-ui/index.html'
}
