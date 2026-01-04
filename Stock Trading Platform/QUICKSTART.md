# Quick Start Guide

## Prerequisites
- Java 11 or higher installed
- `javac` and `java` commands available in your PATH

## Quick Compilation and Run

### Windows
```cmd
compile.bat
java -cp build com.trading.TradingPlatform
```

### Linux/Mac
```bash
chmod +x compile.sh
./compile.sh
java -cp build com.trading.TradingPlatform
```

### Manual Compilation
```bash
# Create build directory
mkdir -p build

# Compile
javac -d build -sourcepath src/main/java src/main/java/com/trading/*.java

# Run
java -cp build com.trading.TradingPlatform
```

## First Run

1. When prompted, choose option **2** to register a new user
2. Enter a User ID (e.g., `user1`)
3. Enter your name
4. Enter initial balance (or press Enter for default $10,000)
5. Start trading!

## Sample Session

```
1. View Market Data          # See available stocks
3. Buy Stock                 # Purchase shares
7. Update Market Prices     # Simulate price changes
2. View Portfolio           # Check your holdings and P/L
4. Sell Stock               # Sell shares
5. View Transaction History # See all your trades
6. View Performance History # Track portfolio over time
```

## Troubleshooting

### "String.repeat()" error
- Ensure you're using Java 11 or higher
- Check version: `java -version`

### Compilation errors
- Make sure all files are in `src/main/java/com/trading/`
- Check that you're compiling from the project root directory

### Data not persisting
- Check that the `data/` directory is created
- Verify write permissions in the project directory

