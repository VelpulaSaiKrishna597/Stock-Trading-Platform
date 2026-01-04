package com.trading;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Stock class representing a single stock with price and trading data.
 */
public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;
    private String symbol;
    private String name;
    private double currentPrice;
    private List<PricePoint> priceHistory;
    private int volume;

    /**
     * Inner class to represent a price point in history.
     */
    public static class PricePoint implements Serializable {
        private static final long serialVersionUID = 1L;
        private LocalDateTime timestamp;
        private double price;

        public PricePoint(LocalDateTime timestamp, double price) {
            this.timestamp = timestamp;
            this.price = price;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public double getPrice() {
            return price;
        }
    }

    /**
     * Initialize a stock.
     *
     * @param symbol Stock ticker symbol (e.g., 'AAPL')
     * @param name Full company name
     * @param initialPrice Starting price per share
     */
    public Stock(String symbol, String name, double initialPrice) {
        this.symbol = symbol.toUpperCase();
        this.name = name;
        this.currentPrice = initialPrice;
        this.priceHistory = new ArrayList<>();
        this.priceHistory.add(new PricePoint(LocalDateTime.now(), initialPrice));
        this.volume = 0;
    }

    /**
     * Update stock price with random fluctuation or set price.
     *
     * @param newPrice Optional fixed price, if null then random fluctuation
     * @return Updated price
     */
    public double updatePrice(Double newPrice) {
        if (newPrice != null) {
            this.currentPrice = newPrice;
        } else {
            // Random price fluctuation between -5% and +5%
            double changePercent = (Math.random() * 0.10) - 0.05;
            this.currentPrice = Math.max(0.01, this.currentPrice * (1 + changePercent));
        }
        this.priceHistory.add(new PricePoint(LocalDateTime.now(), this.currentPrice));
        return this.currentPrice;
    }

    /**
     * Calculate price change percentage from initial price.
     */
    public double getPriceChange() {
        if (priceHistory.size() < 2) {
            return 0.0;
        }
        double initial = priceHistory.get(0).getPrice();
        return ((currentPrice - initial) / initial) * 100.0;
    }

    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public List<PricePoint> getPriceHistory() {
        return new ArrayList<>(priceHistory);
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): $%.2f", symbol, name, currentPrice);
    }
}

