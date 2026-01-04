package com.trading;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * File I/O module for persisting portfolio and user data.
 * Uses simple text-based serialization.
 */
public class DataPersistence {
    private String dataDir;
    private String usersFile;
    private String portfoliosFile;

    /**
     * Initialize persistence manager.
     */
    public DataPersistence(String dataDir) {
        this.dataDir = dataDir;
        this.usersFile = dataDir + File.separator + "users.dat";
        this.portfoliosFile = dataDir + File.separator + "portfolios.dat";
        ensureDataDir();
    }

    public DataPersistence() {
        this("data");
    }

    private void ensureDataDir() {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Save all users to file using Java serialization.
     */
    public boolean saveUsers(Map<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(usersFile))) {
            oos.writeObject(users);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving users: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load all users from file.
     */
    @SuppressWarnings("unchecked")
    public Map<String, User> loadUsers() {
        if (!Files.exists(Paths.get(usersFile))) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(usersFile))) {
            return (Map<String, User>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Save all portfolios to file.
     */
    public boolean savePortfolios(Map<String, Portfolio> portfolios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(portfoliosFile))) {
            oos.writeObject(portfolios);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving portfolios: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load all portfolios from file.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Portfolio> loadPortfolios() {
        if (!Files.exists(Paths.get(portfoliosFile))) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(portfoliosFile))) {
            return (Map<String, Portfolio>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error loading portfolios: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Save both users and portfolios.
     */
    public boolean saveAll(Map<String, User> users, Map<String, Portfolio> portfolios) {
        boolean usersOk = saveUsers(users);
        boolean portfoliosOk = savePortfolios(portfolios);
        return usersOk && portfoliosOk;
    }

    /**
     * Load both users and portfolios.
     */
    public DataLoadResult loadAll() {
        Map<String, User> users = loadUsers();
        Map<String, Portfolio> portfolios = loadPortfolios();
        return new DataLoadResult(users, portfolios);
    }

    /**
     * Helper class for load results.
     */
    public static class DataLoadResult {
        private Map<String, User> users;
        private Map<String, Portfolio> portfolios;

        public DataLoadResult(Map<String, User> users, Map<String, Portfolio> portfolios) {
            this.users = users;
            this.portfolios = portfolios;
        }

        public Map<String, User> getUsers() {
            return users;
        }

        public Map<String, Portfolio> getPortfolios() {
            return portfolios;
        }
    }
}

