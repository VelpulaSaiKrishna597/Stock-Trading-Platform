# Stock Trading Platform (Java)

A comprehensive stock trading simulation platform built with Java using Object-Oriented Programming (OOP) principles. This platform allows users to simulate stock trading, track portfolios, and monitor performance over time.

## Features

### Core Functionality
- **Market Data Display**: Real-time (simulated) stock prices with price change tracking
- **Buy/Sell Operations**: Execute trades with validation and balance checking
- **Portfolio Management**: Track holdings, calculate profit/loss, and monitor performance
- **Performance Tracking**: Historical performance data over time
- **Data Persistence**: Save and load user data and portfolios using Java serialization

### Object-Oriented Design
- **Stock**: Represents individual stocks with price history
- **User**: Manages user accounts with balance and transaction history
- **Transaction**: Records buy/sell operations with timestamps
- **Portfolio**: Tracks holdings and calculates performance metrics
- **Market**: Manages the stock market and price updates
- **TradingSystem**: Handles trading operations and user interactions
- **DataPersistence**: Manages file I/O for data persistence

## Installation

This project uses only Java standard library, so no additional dependencies are required.

### Requirements
- Java 11 or higher (for `String.repeat()` method)
- Java Development Kit (JDK)

## Compilation and Usage

### Compiling the Application

```bash
# Compile all Java files
javac -d build src/main/java/com/trading/*.java

# Or compile from the src directory
cd src/main/java
javac com/trading/*.java
```

### Running the Application

```bash
# From the project root
java -cp build com.trading.TradingPlatform

# Or if compiled in src/main/java
java -cp . com.trading.TradingPlatform
```

### Using an IDE

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)
2. Ensure Java 11+ is configured
3. Run the `TradingPlatform` class (main method)

## Getting Started

1. **Register/Login**: When you start the application, you can either:
   - Register a new user account
   - Login with an existing user ID

2. **View Market Data**: See all available stocks with current prices and changes

3. **Buy Stocks**: 
   - View market data
   - Enter stock symbol and quantity
   - System validates you have sufficient funds

4. **Sell Stocks**:
   - View your current holdings
   - Enter stock symbol and quantity to sell
   - System validates you have sufficient shares

5. **Track Performance**:
   - View your portfolio with current holdings and profit/loss
   - Check transaction history
   - Monitor performance over time

6. **Update Prices**: Simulate market price changes (random fluctuations)

## Project Structure

```
Stock Trading Platform/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── trading/
│                   ├── TradingPlatform.java    # Main application entry point
│                   ├── Stock.java              # Stock class definition
│                   ├── User.java               # User class definition
│                   ├── Transaction.java        # Transaction class definition
│                   ├── TransactionType.java    # Transaction type enum
│                   ├── Portfolio.java          # Portfolio class definition
│                   ├── Market.java             # Market simulation and data management
│                   ├── TradingSystem.java      # Trading operations and user management
│                   └── DataPersistence.java    # File I/O for data persistence
├── data/                                       # Data directory (created automatically)
│   ├── users.dat                               # Saved user data (serialized)
│   └── portfolios.dat                          # Saved portfolio data (serialized)
└── README.md                                   # This file
```

## Features in Detail

### Market Simulation
- Pre-loaded with 10 popular stocks (AAPL, GOOGL, MSFT, AMZN, TSLA, META, NVDA, JPM, V, JNJ)
- Random price fluctuations between -5% and +5% per update
- Price history tracking for each stock

### Trading Operations
- **Buy Orders**: Validates sufficient funds before execution
- **Sell Orders**: Validates sufficient shares before execution
- Transaction history with timestamps
- Automatic balance updates

### Portfolio Tracking
- Real-time portfolio value calculation
- Profit/Loss metrics:
  - Total cost basis
  - Current holdings value
  - Profit/Loss amount and percentage
  - Overall return on initial balance
- Performance history snapshots over time

### Data Persistence
- Automatic saving after transactions
- Java serialization-based storage in `data/` directory
- Loads previous session data on startup
- Separate files for users and portfolios

## Example Usage Flow

1. Start the application: `java -cp build com.trading.TradingPlatform`
2. Register a new user (e.g., ID: "user1", Name: "John Doe")
3. View market data to see available stocks
4. Buy 10 shares of AAPL
5. Update market prices to see price changes
6. View portfolio to see your holdings and P/L
7. Sell 5 shares of AAPL
8. View transaction history
9. Check performance history to see portfolio value over time

## Technical Details

### OOP Design Principles
- **Encapsulation**: Each class manages its own data and operations with private fields and public methods
- **Abstraction**: Complex operations hidden behind simple interfaces
- **Modularity**: Separate classes for different responsibilities
- **Reusability**: Classes can be easily extended or modified
- **Inheritance**: Proper use of interfaces (Serializable) for data persistence

### Data Storage
- Java serialization for object persistence
- Binary format (.dat files) for efficient storage
- Automatic directory creation
- Error handling for file operations
- Transaction-based persistence

### Java Features Used
- Object-Oriented Programming (classes, enums, inner classes)
- Collections Framework (HashMap, ArrayList, List, Map)
- File I/O (ObjectInputStream, ObjectOutputStream)
- Exception handling
- Java 11+ features (String.repeat())

## Building with Maven (Optional)

If you prefer Maven, you can create a `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.trading</groupId>
    <artifactId>stock-trading-platform</artifactId>
    <version>1.0.0</version>
    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
    </properties>
</project>
```

Then compile and run:
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.trading.TradingPlatform"
```

## Future Enhancements

Potential improvements:
- Real-time market data integration (APIs)
- Database support (JDBC with SQLite, PostgreSQL, or H2)
- Advanced charting and visualization
- Multiple portfolio support per user
- Order types (limit orders, stop-loss)
- Market analysis tools
- User authentication and security
- REST API for web interface
- JSON-based persistence (using Gson or Jackson)

## License

This project is provided as-is for educational and demonstration purposes.
