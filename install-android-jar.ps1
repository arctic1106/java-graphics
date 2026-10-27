$ErrorActionPreference = 'Stop'
$jar = "$env:ANDROID_HOME\platforms\android-37.2\android.jar"
mvn install:install-file "-Dfile=$jar" "-DgroupId=dev.arcticsoft.sdk" "-DartifactId=android" "-Dversion=37.2" "-Dpackaging=jar"
