package com.arsen.controllers;

import com.arsen.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/operation")
public class OperationController {
    private final OperationService operationService;

    @Autowired
    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping("/replenishment")
    public Boolean replenishment(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        return operationService.replenishment(userId, amount);
    }

    @PostMapping("/reserving")
    public Boolean reserving(@RequestParam Long userId, @RequestParam Long productId) {
        return operationService.reserving(userId, productId);
    }

    @PostMapping("/un-reserving")
    public Boolean unReserving(@RequestParam Long orderId) {
        return operationService.unReserving(orderId);
    }

    @PostMapping("/revenue-recognition")
    public Boolean revenueRecognition(@RequestParam Long orderId) {
        return operationService.revenueRecognition(orderId);
    }

    @GetMapping("/monthly-order-report")
    public ResponseEntity<byte[]> downloadMonthReport(@RequestParam Integer year, @RequestParam Integer month) {
        return operationService.downloadMonthlyReport(year, month);
    }

    @GetMapping("/monthly-product-report")
    public ResponseEntity<byte[]> downloadMonthlyProductReport(@RequestParam Long companyId) {
        return operationService.downloadMonthlyProductReport(companyId);
    }

}
