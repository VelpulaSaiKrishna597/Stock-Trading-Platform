@echo off
REM Compilation script for Stock Trading Platform (Windows)

echo Compiling Stock Trading Platform...

REM Create build directory if it doesn't exist
if not exist build mkdir build

REM Compile all Java files
javac -d build -sourcepath src\main\java src\main\java\com\trading\*.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo Run with: java -cp build com.trading.TradingPlatform
) else (
    echo Compilation failed!
    exit /b 1
)

