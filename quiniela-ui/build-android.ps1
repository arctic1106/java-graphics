$ErrorActionPreference = 'Stop'
$sdk = $env:ANDROID_HOME
$bt = "$sdk\build-tools\37.0.0"
$jar = "$sdk\platforms\android-37.2\android.jar"
$out = "$PSScriptRoot\build\android"
$ks = "$PSScriptRoot\debug.keystore"

& "$PSScriptRoot\compile.ps1"

Remove-Item -Recurse -Force $out -ErrorAction SilentlyContinue
New-Item -ItemType Directory $out | Out-Null

& "$bt\aapt2.exe" link --manifest "$PSScriptRoot\AndroidManifest.xml" -I $jar -o "$out\base.apk"
if ($LASTEXITCODE) { throw 'aapt2 failed' }

$classes = Get-ChildItem "$PSScriptRoot\build\classes" -Recurse -Filter *.class | Where-Object { $_.FullName -notlike '*\host\desktop\*' } | ForEach-Object FullName
& "$bt\d8.bat" --lib $jar --min-api 29 --output $out $classes
if ($LASTEXITCODE) { throw 'd8 failed' }

Push-Location $out
jar --update --no-manifest --file base.apk classes.dex
$jarExit = $LASTEXITCODE
Pop-Location
if ($jarExit) { throw 'jar failed' }

& "$bt\zipalign.exe" -f 4 "$out\base.apk" "$out\aligned.apk"
if ($LASTEXITCODE) { throw 'zipalign failed' }

if (-not (Test-Path $ks)) {
    keytool -genkeypair -keystore $ks -storepass android -keypass android -alias debug -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Debug"
}

& "$bt\apksigner.bat" sign --ks $ks --ks-pass pass:android --key-pass pass:android --out "$out\app.apk" "$out\aligned.apk"
if ($LASTEXITCODE) { throw 'apksigner failed' }

Write-Host "Built $out\app.apk"
if ($args -contains '--install') {
    adb install -r "$out\app.apk"
    adb shell am start -n quiniela.ui/host.android.MainActivity
}
