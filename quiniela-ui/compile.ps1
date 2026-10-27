$ErrorActionPreference = 'Stop'
$jar = "$env:ANDROID_HOME\platforms\android-37.2\android.jar"
$out = "$PSScriptRoot\build\classes"
Remove-Item -Recurse -Force $out -ErrorAction SilentlyContinue
New-Item -ItemType Directory $out | Out-Null

$src = Get-ChildItem "$PSScriptRoot\src" -Recurse -Filter *.java | ForEach-Object FullName
javac --release 17 -encoding UTF-8 -cp $jar -d $out $src
if ($LASTEXITCODE) { throw 'javac failed' }
