package com.mo.receiptprocessor.controller;

import com.mo.receiptprocessor.model.Receipt;
import com.mo.receiptprocessor.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processReceipt(@RequestBody Receipt receipt) {
        String receiptId = receiptService.storeReceipt(receipt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", receiptId));
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<Map<String, Integer>> getPoints(@PathVariable String id) {
        id = id.replaceAll("[{}]", "");

        int points = receiptService.calculatePoints(id);
        return ResponseEntity.ok(Map.of("points", points));
    }
}
