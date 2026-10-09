@echo off
cd /d "%~dp0"
if exist out rmdir /s /q out
mkdir out
javac --release 17 -encoding UTF-8 -d out src\dunnahar\*.java || exit /b 1
echo Main-Class: dunnahar.Main> manifest.txt
jar cfm dunnahar.jar manifest.txt -C out .
del manifest.txt
echo dunnahar.jar gerado.
pause
