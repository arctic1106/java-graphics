# JavaFX Native
Minimal JavaFX demo, run on the JVM or compiled to a Windows native image with GluonFX.

## Run
```sh
mvn javafx:run
```

## Native image (Windows)
```sh
set GRAALVM_HOME=C:\Users\david\code\graalvm
mvn gluonfx:build gluonfx:nativerun
```
