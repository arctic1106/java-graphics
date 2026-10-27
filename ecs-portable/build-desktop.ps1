$ErrorActionPreference = 'Stop'
& "$PSScriptRoot\compile.ps1"

if ($args -contains '--run') { java -cp "$PSScriptRoot\build\classes" host.desktop.DesktopMain }
