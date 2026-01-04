package com.trading;

import java.util.Map;
import java.util.Scanner;

/**
 * Main application class for the stock trading platform.
 */
public class TradingPlatform {
    private Market market;
    private TradingSystem tradingSystem;
    private DataPersistence persistence;
    private String currentUserId;
    private Scanner scanner;

    /**
     * Initialize the trading platform.
     */
    public TradingPlatform() {
        this.market = new Market();
        this.tradingSystem = new TradingSystem(market);
        this.persistence = new DataPersistence();
        this.currentUserId = null;
        this.scanner = new Scanner(System.in);
        loadData();
    }

    /**
     * Load saved user and portfolio data.
     */
    private void loadData() {
        DataPersistence.DataLoadResult result = persistence.loadAll();
        if (!result.getUsers().isEmpty() || !result.getPortfolios().isEmpty()) {
            tradingSystem.setUsers(result.getUsers());
            tradingSystem.setPortfolios(result.getPortfolios());
            System.out.println("Loaded saved data from previous session.");
        }
    }

    /**
     * Save current user and portfolio data.
     */
    private void saveData() {
        if (persistence.saveAll(tradingSystem.getUsers(), tradingSystem.getPortfolios())) {
            System.out.println("Data saved successfully.");
        } else {
            System.out.println("Warning: Failed to save data.");
        }
    }

    /**
     * Update all stock prices.
     */
    private void updateMarketPrices() {
        market.updatePrices();
        // Record performance for current user if logged in
        if (currentUserId != null) {
            Portfolio portfolio = tradingSystem.getPortfolio(currentUserId);
            portfolio.recordPerformance(market.getAllStocks());
        }
    }

    /**
     * Login or register a new user.
     */
    public void loginOrRegister() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println(String.format("%80s", "STOCK TRADING PLATFORM"));
        System.out.println("=".repeat(80));
        System.out.println("\n1. Login");
        System.out.println("2. Register New User");
        System.out.println("3. Exit");

        System.out.print("\nSelect option (1-3): ");
        String choice = scanner.nextLine().trim();

        if ("1".equals(choice)) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine().trim();
            if (tradingSystem.getUsers().containsKey(userId)) {
                currentUserId = userId;
                User user = tradingSystem.getUser(userId);
                System.out.println("\nWelcome back, " + user.getName() + "!");
            } else {
                System.out.println("User " + userId + " not found. Please register first.");
                loginOrRegister();
            }
        } else if ("2".equals(choice)) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine().trim();
            if (tradingSystem.getUsers().containsKey(userId)) {
                System.out.println("User " + userId + " already exists. Please login instead.");
                loginOrRegister();
            } else {
                System.out.print("Enter your name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Enter initial balance (default 10000): ");
                String balanceInput = scanner.nextLine().trim();
                double balance = balanceInput.isEmpty() ? 10000.0 : Double.parseDouble(balanceInput);

                tradingSystem.registerUser(userId, name, balance);
                currentUserId = userId;
                System.out.printf("\nWelcome, %s! Your account has been created with $%.2f.%n", name, balance);
            }
        } else if ("3".equals(choice)) {
            saveData();
            System.out.println("\nThank you for using Stock Trading Platform. Goodbye!");
            System.exit(0);
        } else {
            System.out.println("Invalid choice. Please try again.");
            loginOrRegister();
        }
    }

    /**
     * Display main menu options.
     */
    public void displayMainMenu() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println(String.format("%80s", "MAIN MENU"));
        System.out.println("=".repeat(80));
        System.out.println("\n1. View Market Data");
        System.out.println("2. View Portfolio");
        System.out.println("3. Buy Stock");
        System.out.println("4. Sell Stock");
        System.out.println("5. View Transaction History");
        System.out.println("6. View Performance History");
        System.out.println("7. Update Market Prices");
        System.out.println("8. Switch User");
        System.out.println("9. Exit");
    }

    /**
     * Display portfolio performance over time.
     */
    public void viewPerformanceHistory() {
        if (currentUserId == null) {
            System.out.println("Please login first.");
            return;
        }

        Portfolio portfolio = tradingSystem.getPortfolio(currentUserId);
        var history = portfolio.getPerformanceHistory();

        if (history.isEmpty()) {
            System.out.println("\nNo performance history available yet. Make some trades and update prices!");
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println(String.format("%80s", "PERFORMANCE HISTORY"));
        System.out.println("=".repeat(80));
        System.out.printf("%-25s %-20s%n", "Timestamp", "Portfolio Value");
        System.out.println("-".repeat(45));

        for (Portfolio.PerformancePoint point : history) {
            String timestamp = point.getTimestamp().toString().replace("T", " ").substring(0, 19);
            System.out.printf("%-25s $%-19.2f%n", timestamp, point.getValue());
        }

        System.out.println("=".repeat(80) + "\n");
    }

    /**
     * Run the main application loop.
     */
    public void run() {
        // Initial login/registration
        loginOrRegister();

        while (true) {
            displayMainMenu();
            System.out.print("\nSelect option (1-9): ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        market.displayMarketData();
                        break;

                    case "2":
                        if (currentUserId != null) {
                            tradingSystem.displayPortfolio(currentUserId);
                        } else {
                            System.out.println("Please login first.");
                        }
                        break;

                    case "3":
                        if (currentUserId == null) {
                            System.out.println("Please login first.");
                            break;
                        }
                        market.displayMarketData();
                        System.out.print("\nEnter stock symbol to buy: ");
                        String buySymbol = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Enter quantity: ");
                        int buyQuantity = Integer.parseInt(scanner.nextLine().trim());

                        TradingSystem.TransactionResult buyResult = tradingSystem.buyStock(
                            currentUserId, buySymbol, buyQuantity);
                        System.out.println("\n" + buyResult.getMessage());
                        if (buyResult.isSuccess()) {
                            saveData();
                        }
                        break;

                    case "4":
                        if (currentUserId == null) {
                            System.out.println("Please login first.");
                            break;
                        }
                        Portfolio portfolio = tradingSystem.getPortfolio(currentUserId);
                        Map<String, Integer> holdings = portfolio.getHoldings();

                        if (holdings.isEmpty()) {
                            System.out.println("\nYou have no stocks to sell.");
                            break;
                        }

                        System.out.println("\nYour Holdings:");
                        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                            Stock stock = market.getStock(entry.getKey());
                            System.out.printf("  %s: %d shares @ $%.2f%n",
                                entry.getKey(), entry.getValue(), stock.getCurrentPrice());
                        }

                        System.out.print("\nEnter stock symbol to sell: ");
                        String sellSymbol = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Enter quantity: ");
                        int sellQuantity = Integer.parseInt(scanner.nextLine().trim());

                        TradingSystem.TransactionResult sellResult = tradingSystem.sellStock(
                            currentUserId, sellSymbol, sellQuantity);
                        System.out.println("\n" + sellResult.getMessage());
                        if (sellResult.isSuccess()) {
                            saveData();
                        }
                        break;

                    case "5":
                        if (currentUserId != null) {
                            tradingSystem.displayTransactionHistory(currentUserId);
                        } else {
                            System.out.println("Please login first.");
                        }
                        break;

                    case "6":
                        if (currentUserId != null) {
                            viewPerformanceHistory();
                        } else {
                            System.out.println("Please login first.");
                        }
                        break;

                    case "7":
                        System.out.println("\nUpdating market prices...");
                        updateMarketPrices();
                        System.out.println("Market prices updated!");
                        market.displayMarketData();
                        if (currentUserId != null) {
                            saveData();
                        }
                        break;

                    case "8":
                        saveData();
                        currentUserId = null;
                        loginOrRegister();
                        break;

                    case "9":
                        saveData();
                        System.out.println("\nThank you for using Stock Trading Platform. Goodbye!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please try again.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Entry point for the application.
     */
    public static void main(String[] args) {
        TradingPlatform platform = new TradingPlatform();
        platform.run();
    }
}

