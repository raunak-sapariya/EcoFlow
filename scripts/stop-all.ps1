<#
.SYNOPSIS
  Stops whatever is listening on the EcoFlow ports (8000 web-app, 8081-8084 microservices).
#>
$ports = 8000, 8081, 8082, 8083, 8084
foreach ($port in $ports) {
    $connections = Get-NetTCPConnection -State Listen -LocalPort $port -ErrorAction SilentlyContinue
    foreach ($c in $connections) {
        $p = Get-Process -Id $c.OwningProcess -ErrorAction SilentlyContinue
        if ($p) {
            Write-Host "Stopping $($p.ProcessName) (pid $($p.Id)) on port $port"
            Stop-Process -Id $p.Id -Force
        }
    }
}
Write-Host 'Done. (The terminal windows can be closed.)'
