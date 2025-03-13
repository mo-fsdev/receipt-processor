package com.mo.receiptprocessor.service;

import com.mo.receiptprocessor.model.Receipt;
import com.mo.receiptprocessor.model.ReceiptItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReceiptService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Map<String, Receipt> receiptStorage = new ConcurrentHashMap<>();

    public String storeReceipt(Receipt receipt) {
        if (receipt == null || receipt.getRetailer() == null || receipt.getTotal() == null ||
                receipt.getItems() == null || receipt.getPurchaseDate() == null || receipt.getPurchaseTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid receipt data.");
        }

        String id = UUID.randomUUID().toString();
        receiptStorage.put(id, receipt);
        logger.info("Receipt stored with ID: {}", id);
        return id;
    }

    public int calculatePoints(String id) {
        Receipt receipt = receiptStorage.get(id);
        if (receipt == null) {
            logger.warn("Receipt not found for ID: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receipt not found");
        }

        int retailerPoints = calculateRetailerPoints(receipt.getRetailer());
        int totalPoints = calculateTotalPoints(receipt.getTotal());
        int itemPoints = calculateItemPoints(receipt.getItems());
        int datePoints = calculateDatePoints(receipt.getPurchaseDate());
        int timePoints = calculateTimePoints(receipt.getPurchaseTime());


        logger.info("Retailer Points: {}", retailerPoints);
        logger.info("Total Points: {}", totalPoints);
        logger.info("Item Points: {}", itemPoints);
        logger.info("Date Points: {}", datePoints);
        logger.info("Time Points: {}", timePoints);

        int total = retailerPoints + totalPoints + itemPoints + datePoints + timePoints;
        logger.info("Total points for receipt {}: {}", id, total);

        return total;
    }

    private int calculateRetailerPoints(String retailer) {
        int points = retailer.replaceAll("[^a-zA-Z0-9]", "").length();
        logger.debug("Retailer '{}' earned {} points", retailer, points);
        return points;
    }

    private int calculateTotalPoints(String totalStr) {
        try {
            BigDecimal total = new BigDecimal(totalStr);
            int points = 0;

            if (total.stripTrailingZeros().scale() <= 0) { // Proper check for round dollar
                points += 50;
                logger.debug("Total {} is a round dollar amount: +50 points", total);
            }
            if (total.remainder(new BigDecimal("0.25")).compareTo(BigDecimal.ZERO) == 0) {
                points += 25;
                logger.debug("Total {} is a multiple of 0.25: +25 points", total);
            }
            return points;
        } catch (NumberFormatException e) {
            logger.warn("Invalid total format '{}'. Assigning 0 points.", totalStr);
            return 0;
        }
    }

    private int calculateItemPoints(List<ReceiptItems> items) {
        if (items == null || items.isEmpty()) {
            logger.debug("No items found on receipt. Item points: 0");
            return 0;
        }

        int points = (items.size() / 2) * 5; // 5 points for every 2 items
        logger.debug("Item pair points: {}", points);

        for (ReceiptItems item : items) {
            String description = item.getShortDescription().trim();
            BigDecimal price;

            // Skip invalid price items
            try {
                price = new BigDecimal(item.getPrice());
            } catch (NumberFormatException e) {
                logger.warn("Invalid price format for item '{}'. Skipping.", item.getShortDescription());
                continue;
            }

            if (description.length() % 3 == 0) {
                int itemPoints = price.multiply(new BigDecimal("0.2"))
                        .setScale(0, RoundingMode.CEILING)
                        .intValue();
                points += itemPoints;
                logger.debug("Item '{}' earned {} points", item.getShortDescription(), itemPoints);
            }
        }
        return points;
    }

    private int calculateDatePoints(String purchaseDate) {
        try {
            LocalDate date = LocalDate.parse(purchaseDate, DATE_FORMATTER);
            if (date.getDayOfMonth() % 2 != 0) {
                logger.debug("Purchase date {} is an odd day: +6 points", purchaseDate);
                return 6;
            }
        } catch (Exception e) {
            logger.warn("Invalid purchase date format '{}'. Assigning 0 points.", purchaseDate);
        }
        return 0;
    }

    private int calculateTimePoints(String purchaseTime) {
        try {
            LocalTime time = LocalTime.parse(purchaseTime, TIME_FORMATTER);
            if (time.isAfter(LocalTime.of(14, 0)) && time.isBefore(LocalTime.of(16, 0))) {
                logger.debug("Purchase time {} is between 2-4 PM: +10 points", purchaseTime);
                return 10;
            }
        } catch (Exception e) {
            logger.warn("Invalid purchase time format '{}'. Assigning 0 points.", purchaseTime);
        }
        return 0;
    }
}
