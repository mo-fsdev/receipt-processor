package com.mo.receiptprocessor;

import com.mo.receiptprocessor.model.Receipt;
import com.mo.receiptprocessor.model.ReceiptItems;
import com.mo.receiptprocessor.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReceiptServiceTest {

    private ReceiptService receiptService;

    @BeforeEach
    public void setup() {
        // Initialize ReceiptService before each test
        receiptService = new ReceiptService();
    }

    @Test
    void testStoreReceipt_InvalidData() {
        // Test case for invalid receipt data (e.g., missing retailer)
        Receipt invalidReceipt = new Receipt();
        invalidReceipt.setRetailer(null);  // Invalid: retailer is missing
        invalidReceipt.setPurchaseDate("2022-01-01");
        invalidReceipt.setPurchaseTime("13:01");
        invalidReceipt.setTotal("35.35");
        invalidReceipt.setItems(Arrays.asList(
                new ReceiptItems("Mountain Dew 12PK", "6.49")
        ));

        // Test that the exception is thrown when trying to store the invalid receipt
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            receiptService.storeReceipt(invalidReceipt);
        });
    }

    @Test
    void testCalculatePoints_EmptyItems() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("");
        receipt.setPurchaseDate("2022-02-02");
        receipt.setPurchaseTime("10:00");
        receipt.setTotal("1.03");
        receipt.setItems(Collections.emptyList());

        String receiptId = receiptService.storeReceipt(receipt);
        int points = receiptService.calculatePoints(receiptId);

        System.out.println("Calculated points: " + points);
        assertEquals(0, points);
    }

    @Test
    void testCalculatePoints_InvalidReceiptId() {
        // Test case for invalid receipt ID
        String invalidReceiptId = "invalid-id";  // Non-existent receipt ID

        // Test that the exception is thrown when trying to calculate points for an invalid receipt ID
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            receiptService.calculatePoints(invalidReceiptId);
        });
    }

    @Test
    void testCalculatePoints_InvalidDateFormat() {
        // Test case for invalid date format
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("t-p-p");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("35.35");
        receipt.setItems(Arrays.asList(
                new ReceiptItems("Mountain Dew 12PK", "6.49")
        ));

        String receiptId = receiptService.storeReceipt(receipt);

        int points = receiptService.calculatePoints(receiptId);

        assertEquals(6, points);
    }

    @Test
    void testCalculatePoints_InvalidPriceFormat() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("");
        receipt.setPurchaseDate("2022-02-02");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("35.35");
        receipt.setItems(List.of(
                new ReceiptItems("Mountain Dew 12PK", "invalid-price")  // Invalid price format
        ));

        String receiptId = receiptService.storeReceipt(receipt);
        int points = receiptService.calculatePoints(receiptId);

        assertEquals(0, points);
    }

    @Test
    void testCalculatePoints_ValidScenario() {
        // Test case with valid receipt data
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("35.35");
        receipt.setItems(Arrays.asList(
                new ReceiptItems("Mountain Dew 12PK", "6.49"),
                new ReceiptItems("Emils Cheese Pizza", "12.25"),
                new ReceiptItems("Knorr Creamy Chicken", "1.26"),
                new ReceiptItems("Doritos Nacho Cheese", "3.35"),
                new ReceiptItems("Klarbrunn 12-PK 12 FL OZ", "12.00")
        ));

        String receiptId = receiptService.storeReceipt(receipt);
        int points = receiptService.calculatePoints(receiptId);

        assertEquals(28, points);
    }
}
