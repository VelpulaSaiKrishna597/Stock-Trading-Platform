package com.trading;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Portfolio class managing user's stock holdings and performance.
 */
public class Portfolio implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private Map<String, Integer> holdings; // symbol -> quantity
    private List<Transaction> transactions;
    private List<PerformancePoint> performanceHistory;

    /**
     * Inner class to represent a performance point in history.
     */
    public static class PerformancePoint implements Serializable {
        private static final long serialVersionUID = 1L;
        private LocalDateTime timestamp;
        private double value;

        public PerformancePoint(LocalDateTime timestamp, double value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public double getValue() {
            return value;
        }
    }

    /**
     * Initialize a portfolio for a user.
     *
     * @param userId ID of the user who owns this portfolio
     */
    public Portfolio(String userId) {
        this.userId = userId;
        this.holdings = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.performanceHistory = new ArrayList<>();
    }

    /**
     * Add a transaction and update holdings.
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        String symbol = transaction.getStockSymbol();

        if (transaction.getTransactionType() == TransactionType.BUY) {
            holdings.put(symbol, holdings.getOrDefault(symbol, 0) + transaction.getQuantity());
        } else if (transaction.getTransactionType() == TransactionType.SELL) {
            int currentQuantity = holdings.getOrDefault(symbol, 0);
            int newQuantity = currentQuantity - transaction.getQuantity();
            if (newQuantity <= 0) {
                holdings.remove(symbol);
            } else {
                holdings.put(symbol, newQuantity);
            }
        }
    }

    /**
     * Get current stock holdings.
     */
    public Map<String, Integer> getHoldings() {
        return new HashMap<>(holdings);
    }

    /**
     * Get quantity of a specific stock.
     */
    public int getQuantity(String symbol) {
        return holdings.getOrDefault(symbol.toUpperCase(), 0);
    }

    /**
     * Calculate total portfolio value based on current stock prices.
     *
     * @param stocks Map of Stock objects keyed by symbol
     * @return Total portfolio value
     */
    public double calculateTotalValue(Map<String, Stock> stocks) {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            if (stocks.containsKey(symbol)) {
                total += stocks.get(symbol).getCurrentPrice() * quantity;
            }
        }
        return total;
    }

    /**
     * Calculate profit/loss metrics.
     *
     * @param stocks Map of Stock objects keyed by symbol
     * @param initialBalance User's initial cash balance
     * @return Map with P/L metrics
     */
    public Map<String, Double> calculateProfitLoss(Map<String, Stock> stocks, double initialBalance) {
        double currentValue = calculateTotalValue(stocks);

        // Calculate total cost basis
        double totalCost = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType() == TransactionType.BUY) {
                totalCost += transaction.getTotalValue();
            } else if (transaction.getTransactionType() == TransactionType.SELL) {
                totalCost -= transaction.getTotalValue();
            }
        }

        // Current holdings value
        double holdingsValue = currentValue;

        // Total profit/loss
        double totalPnl = holdingsValue - totalCost;

        // Percentage return
        double pnlPercent = totalCost > 0 ? (totalPnl / totalCost) * 100.0 : 0.0;

        Map<String, Double> result = new HashMap<>();
        result.put("total_cost", totalCost);
        result.put("current_value", currentValue);
        result.put("profit_loss", totalPnl);
        result.put("profit_loss_percent", pnlPercent);
        result.put("holdings_value", holdingsValue);

        return result;
    }

    /**
     * Record current portfolio performance snapshot.
     */
    public void recordPerformance(Map<String, Stock> stocks) {
        double totalValue = calculateTotalValue(stocks);
        performanceHistory.add(new PerformancePoint(LocalDateTime.now(), totalValue));
    }

    /**
     * Get portfolio performance over time.
     */
    public List<PerformancePoint> getPerformanceHistory() {
        return new ArrayList<>(performanceHistory);
    }

    /**
     * Get all transactions in this portfolio.
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Portfolio(user_id=").append(userId).append(", holdings=");
        holdings.forEach((sym, qty) -> sb.append(sym).append(":").append(qty).append(", "));
        if (!holdings.isEmpty()) {
            sb.setLength(sb.length() - 2); // Remove trailing ", "
        }
        sb.append(")");
        return sb.toString();
    }
}

