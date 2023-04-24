package com.arsen.controllers;

import com.arsen.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/replenishment")
    public Boolean replenishment(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        return adminService.replenishment(userId, amount);
    }

    @PostMapping("/reserving")
    public Boolean reserving(@RequestParam Long userId, @RequestParam Long productId) {
        return adminService.reserving(userId, productId);
    }

    @PostMapping("/un-reserving")
    public Boolean unReserving(@RequestParam Long orderId) {
        return adminService.unReserving(orderId);
    }

    @PostMapping("/revenue-recognition")
    public Boolean revenueRecognition(@RequestParam Long orderId) {
        return adminService.revenueRecognition(orderId);
    }

    @GetMapping("/monthly-order-report")
    public ResponseEntity<byte[]> downloadMonthReport(@RequestParam Integer year, @RequestParam Integer month) {
        return adminService.downloadMonthlyReport(year, month);
    }

    @GetMapping("/monthly-product-report")
    public ResponseEntity<byte[]> downloadMonthlyProductReport(@RequestParam Long companyId) {
        return adminService.downloadMonthlyProductReport(companyId);
    }

}
