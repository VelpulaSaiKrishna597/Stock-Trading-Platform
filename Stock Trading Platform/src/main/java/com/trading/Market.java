package com.trading;

import java.util.*;

/**
 * Market class managing stock market simulation and data.
 */
public class Market {
    private Map<String, Stock> stocks;

    /**
     * Initialize the market with default stocks.
     */
    public Market() {
        this.stocks = new HashMap<>();
        initializeDefaultStocks();
    }

    private void initializeDefaultStocks() {
        List<Object[]> defaultStocks = Arrays.asList(
            new Object[]{"AAPL", "Apple Inc.", 175.50},
            new Object[]{"GOOGL", "Alphabet Inc.", 142.30},
            new Object[]{"MSFT", "Microsoft Corporation", 378.85},
            new Object[]{"AMZN", "Amazon.com Inc.", 145.20},
            new Object[]{"TSLA", "Tesla Inc.", 248.50},
            new Object[]{"META", "Meta Platforms Inc.", 485.00},
            new Object[]{"NVDA", "NVIDIA Corporation", 875.00},
            new Object[]{"JPM", "JPMorgan Chase & Co.", 180.25},
            new Object[]{"V", "Visa Inc.", 280.75},
            new Object[]{"JNJ", "Johnson & Johnson", 165.40}
        );

        for (Object[] stockData : defaultStocks) {
            String symbol = (String) stockData[0];
            String name = (String) stockData[1];
            double price = (Double) stockData[2];
            stocks.put(symbol, new Stock(symbol, name, price));
        }
    }

    /**
     * Add a new stock to the market.
     */
    public Stock addStock(String symbol, String name, double initialPrice) {
        Stock stock = new Stock(symbol, name, initialPrice);
        stocks.put(symbol.toUpperCase(), stock);
        return stock;
    }

    /**
     * Get a stock by symbol.
     */
    public Stock getStock(String symbol) {
        Stock stock = stocks.get(symbol.toUpperCase());
        if (stock == null) {
            throw new IllegalArgumentException("Stock " + symbol + " not found in market");
        }
        return stock;
    }

    /**
     * Get all stocks in the market.
     */
    public Map<String, Stock> getAllStocks() {
        return new HashMap<>(stocks);
    }

    /**
     * Update all stock prices with random fluctuations.
     */
    public void updatePrices() {
        for (Stock stock : stocks.values()) {
            stock.updatePrice(null);
        }
    }

    /**
     * Get formatted market data for display.
     */
    public List<Map<String, Object>> getMarketData() {
        List<Map<String, Object>> marketData = new ArrayList<>();
        for (Stock stock : stocks.values()) {
            Map<String, Object> data = new HashMap<>();
            data.put("symbol", stock.getSymbol());
            data.put("name", stock.getName());
            data.put("price", stock.getCurrentPrice());
            double change = stock.getPriceChange();
            data.put("change", change);
            data.put("change_percent", change);
            marketData.add(data);
        }
        marketData.sort(Comparator.comparing(d -> (String) d.get("symbol")));
        return marketData;
    }

    /**
     * Display formatted market data table.
     */
    public void displayMarketData() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println(String.format("%80s", "MARKET DATA"));
        System.out.println("=".repeat(80));
        System.out.printf("%-10s %-30s %-15s %-15s%n", "Symbol", "Company Name", "Price", "Change %");
        System.out.println("-".repeat(80));

        for (Map<String, Object> data : getMarketData()) {
            String symbol = (String) data.get("symbol");
            String name = (String) data.get("name");
            double price = (Double) data.get("price");
            double changePercent = (Double) data.get("change_percent");
            String changeStr = String.format("%+.2f%%", changePercent);
            System.out.printf("%-10s %-30s $%-14.2f %-15s%n", symbol, name, price, changeStr);
        }
        System.out.println("=".repeat(80) + "\n");
    }
}

