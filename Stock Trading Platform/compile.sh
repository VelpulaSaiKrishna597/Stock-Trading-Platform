#!/bin/bash
# Compilation script for Stock Trading Platform

echo "Compiling Stock Trading Platform..."

# Create build directory if it doesn't exist
mkdir -p build

# Compile all Java files
javac -d build -sourcepath src/main/java src/main/java/com/trading/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Run with: java -cp build com.trading.TradingPlatform"
else
    echo "Compilation failed!"
    exit 1
fi

