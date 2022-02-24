@echo off
for /r %%i in (*.class) do del "%%i"
echo Cleaned.
