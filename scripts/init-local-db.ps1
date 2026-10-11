<#
.SYNOPSIS
  Creates the per-service databases (user_db, water_db, supply_db, operations_db) in a PostgreSQL that is installed on this
  machine - use this INSTEAD of docker compose. Safe to run again: existing databases are skipped.

.EXAMPLE
  .\scripts\init-local-db.ps1                      # asks for the postgres password
  .\scripts\init-local-db.ps1 -Port 5433 -User me
#>
[CmdletBinding()]
param(
    [string]$DbHost = 'localhost',
    [int]$Port = 5432,
    [string]$User = 'postgres',
    [string]$PsqlPath
)

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
. "$PSScriptRoot\_common.ps1"
Import-DotEnv (Join-Path $root '.env')

# keep in sync with docker/postgres/init.sql
$databases = 'user_db', 'water_db', 'supply_db', 'operations_db'

# Values from .env win over the defaults (but not over parameters typed on the command line)
if (-not $PSBoundParameters.ContainsKey('DbHost') -and $env:DB_HOST) { $DbHost = $env:DB_HOST }
if (-not $PSBoundParameters.ContainsKey('Port') -and $env:DB_PORT) { $Port = [int]$env:DB_PORT }
if (-not $PSBoundParameters.ContainsKey('User') -and $env:DB_USERNAME) { $User = $env:DB_USERNAME }

# --- find psql ---
if (-not $PsqlPath) {
    $cmd = Get-Command psql -ErrorAction SilentlyContinue
    if ($cmd) { $PsqlPath = $cmd.Source }
}
if (-not $PsqlPath) {
    $candidates = @('D:\psql\bin\psql.exe') + @(Get-ChildItem 'C:\Program Files\PostgreSQL\*\bin\psql.exe' -ErrorAction SilentlyContinue | ForEach-Object { $_.FullName })
    $PsqlPath = $candidates | Where-Object { Test-Path $_ } | Select-Object -First 1
}
if (-not $PsqlPath) { throw 'psql.exe not found. Pass it explicitly: -PsqlPath "C:\path\to\psql.exe"' }
Write-Host "Using $PsqlPath"

# --- password: from .env, else ask ---
$password = $env:DB_PASSWORD
if (-not $password) {
    $secure = Read-Host "Password of PostgreSQL user '$User'" -AsSecureString
    $password = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($secure))
}
$env:PGPASSWORD = $password   # visible to the psql child processes of this script only

foreach ($db in $databases) {
    $exists = & $PsqlPath -h $DbHost -p $Port -U $User -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='$db'"
    if ($LASTEXITCODE -ne 0) { throw "Could not connect to PostgreSQL at ${DbHost}:${Port} as '$User'. Check host, port and password." }
    if ("$exists".Trim() -eq '1') {
        Write-Host "  $db already exists"
    }
    else {
        & $PsqlPath -h $DbHost -p $Port -U $User -d postgres -c "CREATE DATABASE $db"
        if ($LASTEXITCODE -ne 0) { throw "Creating $db failed." }
        Write-Host "  $db created" -ForegroundColor Green
    }
}

Write-Host ''
Write-Host "Done. Make sure your .env contains DB_PORT=$Port, DB_USERNAME=$User and DB_PASSWORD=<your password>," -ForegroundColor Cyan
Write-Host 'then start the services with .\scripts\start-all.ps1'
