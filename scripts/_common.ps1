# Shared helpers, dot-sourced by the other scripts. Works on Windows PowerShell 5.1 and PowerShell 7.

# Loads KEY=VALUE lines from a .env file into this process's environment (the services inherit them).
function Import-DotEnv([string]$Path) {
    if (-not (Test-Path $Path)) { return }
    foreach ($line in Get-Content $Path) {
        $l = $line.Trim()
        if ($l -eq '' -or $l.StartsWith('#')) { continue }
        $i = $l.IndexOf('=')
        if ($i -lt 1) { continue }
        $key = $l.Substring(0, $i).Trim()
        $value = $l.Substring($i + 1).Trim().Trim('"').Trim("'")
        [Environment]::SetEnvironmentVariable($key, $value, 'Process')
    }
}

# The Maven wrapper (mvnw) fails if JAVA_HOME points to a folder that no longer exists.
# If that is the case, fall back to the java that is on PATH - for this script only.
function Initialize-JavaHome {
    $okHome = $env:JAVA_HOME -and (Test-Path (Join-Path $env:JAVA_HOME 'bin\java.exe'))
    if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
        throw 'Java was not found. Install a JDK (21 or newer) and reopen the terminal.'
    }
    $previous = $ErrorActionPreference
    $ErrorActionPreference = 'Continue'   # java prints its settings on stderr
    $props = & java -XshowSettings:properties -version 2>&1 | Out-String
    $ErrorActionPreference = $previous

    $spec = [regex]::Match($props, 'java\.specification\.version = (\d+)').Groups[1].Value
    if ($spec -and [int]$spec -lt 21) { throw "JDK 21 or newer is required, but 'java' on PATH is version $spec." }

    if (-not $okHome) {
        $jh = [regex]::Match($props, 'java\.home = (.+)').Groups[1].Value.Trim()
        if ($env:JAVA_HOME) { Write-Warning "JAVA_HOME ('$($env:JAVA_HOME)') does not exist. Using '$jh' for this run. Please fix JAVA_HOME permanently (see README)." }
        $env:JAVA_HOME = $jh
    }
}
