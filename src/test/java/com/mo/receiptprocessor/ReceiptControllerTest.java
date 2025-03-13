package com.mo.receiptprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mo.receiptprocessor.controller.ReceiptController;
import com.mo.receiptprocessor.model.Receipt;
import com.mo.receiptprocessor.service.ReceiptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReceiptController.class)
public class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReceiptService receiptService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testProcessReceipt() throws Exception {
        Receipt receipt = new Receipt();

        when(receiptService.storeReceipt(any(Receipt.class))).thenReturn("12345");

        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receipt)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("12345"));
    }


    @Test
    public void testGetPoints() throws Exception {
        String receiptId = "12345";
        int expectedPoints = 10;

        when(receiptService.calculatePoints(receiptId)).thenReturn(expectedPoints);

        mockMvc.perform(get("/receipts/{id}/points", receiptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(expectedPoints));
    }

}
