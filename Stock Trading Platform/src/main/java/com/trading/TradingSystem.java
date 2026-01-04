package com.trading;

import java.util.*;

/**
 * Trading system managing buy/sell operations and user interactions.
 */
public class TradingSystem {
    private Market market;
    private Map<String, User> users;
    private Map<String, Portfolio> portfolios;

    /**
     * Initialize trading system.
     */
    public TradingSystem(Market market) {
        this.market = market;
        this.users = new HashMap<>();
        this.portfolios = new HashMap<>();
    }

    /**
     * Register a new user.
     */
    public User registerUser(String userId, String name, double initialBalance) {
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException("User " + userId + " already exists");
        }
        User user = new User(userId, name, initialBalance);
        users.put(userId, user);
        portfolios.put(userId, new Portfolio(userId));
        return user;
    }

    /**
     * Get user by ID.
     */
    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User " + userId + " not found");
        }
        return user;
    }

    /**
     * Get portfolio for a user.
     */
    public Portfolio getPortfolio(String userId) {
        Portfolio portfolio = portfolios.get(userId);
        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio for user " + userId + " not found");
        }
        return portfolio;
    }

    /**
     * Execute a buy order.
     *
     * @return TransactionResult with success status, message, and transaction
     */
    public TransactionResult buyStock(String userId, String symbol, int quantity) {
        try {
            User user = getUser(userId);
            Portfolio portfolio = getPortfolio(userId);

            if (!market.getAllStocks().containsKey(symbol.toUpperCase())) {
                return new TransactionResult(false, "Stock " + symbol + " not found in market", null);
            }

            Stock stock = market.getStock(symbol);
            double totalCost = stock.getCurrentPrice() * quantity;

            if (user.getBalance() < totalCost) {
                return new TransactionResult(false,
                    String.format("Insufficient funds. Need $%.2f, have $%.2f", totalCost, user.getBalance()),
                    null);
            }

            if (quantity <= 0) {
                return new TransactionResult(false, "Quantity must be positive", null);
            }

            // Create transaction
            Transaction transaction = new Transaction(
                TransactionType.BUY, symbol, quantity,
                stock.getCurrentPrice(), userId
            );

            // Execute transaction
            user.setBalance(user.getBalance() - totalCost);
            user.addTransaction(transaction);
            portfolio.addTransaction(transaction);

            return new TransactionResult(true,
                String.format("Successfully bought %d shares of %s @ $%.2f", quantity, symbol, stock.getCurrentPrice()),
                transaction);

        } catch (Exception e) {
            return new TransactionResult(false, "Error: " + e.getMessage(), null);
        }
    }

    /**
     * Execute a sell order.
     */
    public TransactionResult sellStock(String userId, String symbol, int quantity) {
        try {
            User user = getUser(userId);
            Portfolio portfolio = getPortfolio(userId);

            if (!market.getAllStocks().containsKey(symbol.toUpperCase())) {
                return new TransactionResult(false, "Stock " + symbol + " not found in market", null);
            }

            Stock stock = market.getStock(symbol);
            int currentHoldings = portfolio.getQuantity(symbol);

            if (currentHoldings < quantity) {
                return new TransactionResult(false,
                    String.format("Insufficient shares. Have %d, trying to sell %d", currentHoldings, quantity),
                    null);
            }

            if (quantity <= 0) {
                return new TransactionResult(false, "Quantity must be positive", null);
            }

            // Create transaction
            Transaction transaction = new Transaction(
                TransactionType.SELL, symbol, quantity,
                stock.getCurrentPrice(), userId
            );

            // Execute transaction
            double totalValue = stock.getCurrentPrice() * quantity;
            user.setBalance(user.getBalance() + totalValue);
            user.addTransaction(transaction);
            portfolio.addTransaction(transaction);

            return new TransactionResult(true,
                String.format("Successfully sold %d shares of %s @ $%.2f", quantity, symbol, stock.getCurrentPrice()),
                transaction);

        } catch (Exception e) {
            return new TransactionResult(false, "Error: " + e.getMessage(), null);
        }
    }

    /**
     * Display user's portfolio and performance.
     */
    public void displayPortfolio(String userId) {
        User user = getUser(userId);
        Portfolio portfolio = getPortfolio(userId);

        System.out.println("\n" + "=".repeat(80));
        System.out.println(String.format("%80s", "PORTFOLIO - " + user.getName() + " (" + userId + ")"));
        System.out.println("=".repeat(80));

        // Account summary
        System.out.printf("%nAccount Balance: $%.2f%n", user.getBalance());
        System.out.printf("Initial Balance: $%.2f%n", user.getInitialBalance());

        // Holdings
        Map<String, Integer> holdings = portfolio.getHoldings();
        if (!holdings.isEmpty()) {
            System.out.println("\nCurrent Holdings:");
            System.out.printf("%-10s %-15s %-20s %-15s%n", "Symbol", "Quantity", "Current Price", "Total Value");
            System.out.println("-".repeat(60));

            double totalHoldingsValue = 0.0;
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                String symbol = entry.getKey();
                int quantity = entry.getValue();
                Stock stock = market.getStock(symbol);
                double value = stock.getCurrentPrice() * quantity;
                totalHoldingsValue += value;
                System.out.printf("%-10s %-15d $%-19.2f $%-14.2f%n",
                    symbol, quantity, stock.getCurrentPrice(), value);
            }

            System.out.println("-".repeat(60));
            System.out.printf("%-45s $%.2f%n", "Total Holdings Value", totalHoldingsValue);

            // Performance metrics
            Map<String, Double> pnl = portfolio.calculateProfitLoss(market.getAllStocks(), user.getInitialBalance());
            System.out.println("\nPerformance Metrics:");
            System.out.printf("  Total Cost Basis: $%.2f%n", pnl.get("total_cost"));
            System.out.printf("  Current Holdings Value: $%.2f%n", pnl.get("current_value"));
            System.out.printf("  Profit/Loss: $%.2f (%.2f%%)%n", pnl.get("profit_loss"), pnl.get("profit_loss_percent"));
            System.out.printf("  Total Portfolio Value: $%.2f%n", user.getBalance() + totalHoldingsValue);

            // Overall return
            double totalValue = user.getBalance() + totalHoldingsValue;
            double overallReturn = totalValue - user.getInitialBalance();
            double overallReturnPct = (overallReturn / user.getInitialBalance()) * 100.0;
            System.out.printf("  Overall Return: $%.2f (%.2f%%)%n", overallReturn, overallReturnPct);
        } else {
            System.out.println("\nNo current holdings.");
        }

        System.out.println("=".repeat(80) + "\n");
    }

    /**
     * Display user's transaction history.
     */
    public void displayTransactionHistory(String userId) {
        User user = getUser(userId);
        List<Transaction> transactions = user.getTransactionHistory();

        System.out.println("\n" + "=".repeat(80));
        System.out.println(String.format("%80s", "TRANSACTION HISTORY - " + user.getName()));
        System.out.println("=".repeat(80));

        if (transactions.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            System.out.printf("%-8s %-10s %-12s %-15s %-15s %-20s%n",
                "Type", "Symbol", "Quantity", "Price", "Total", "Time");
            System.out.println("-".repeat(80));
            for (Transaction transaction : transactions) {
                System.out.printf("%-8s %-10s %-12d $%-14.2f $%-14.2f %-20s%n",
                    transaction.getTransactionType(),
                    transaction.getStockSymbol(),
                    transaction.getQuantity(),
                    transaction.getPricePerShare(),
                    transaction.getTotalValue(),
                    transaction.getTimestamp().toString().replace("T", " ").substring(0, 19));
            }
        }

        System.out.println("=".repeat(80) + "\n");
    }

    /**
     * Helper class for transaction results.
     */
    public static class TransactionResult {
        private boolean success;
        private String message;
        private Transaction transaction;

        public TransactionResult(boolean success, String message, Transaction transaction) {
            this.success = success;
            this.message = message;
            this.transaction = transaction;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Transaction getTransaction() {
            return transaction;
        }
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public void setPortfolios(Map<String, Portfolio> portfolios) {
        this.portfolios = portfolios;
    }
}

