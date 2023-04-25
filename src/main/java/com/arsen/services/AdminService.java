package com.arsen.services;

import com.arsen.enums.OrderStatus;
import com.arsen.models.Company;
import com.arsen.models.Order;
import com.arsen.models.Product;
import com.arsen.models.User;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final CsvDownload csvDownload;
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CompanyService companyService;

    @Autowired
    public AdminService(CsvDownload csvDownload, UserService userService, ProductService productService,
                        OrderService orderService, CompanyService companyService) {
        this.csvDownload = csvDownload;
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.companyService = companyService;
    }

    // начисления средств на баланс пользователя
    public Boolean replenishment(Long userId, BigDecimal amount) {
        User user = userService.getUserById(userId);
        user.setBalance(user.getBalance().add(amount));
        userService.updateUser(user.getId(), user);
        return true;
    }

    // Метод резервирования средств пользователя
    public Boolean reserving(Long userId, Long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        if (user.getBalance().compareTo(product.getPrice()) < 0)
            return false;

        user.setBalance(user.getBalance().subtract(product.getPrice()));
        user.setReserveBalance(user.getReserveBalance().add(product.getPrice()));
        userService.updateUser(user.getId(), user);
        orderService.createOrder(
                Order.builder()
                        .date(LocalDateTime.now())
                        .user(user)
                        .product(product)
                        .status(OrderStatus.PENDING)
                        .build());
        return true;
    }

    // Метод признания выручки
    public Boolean revenueRecognition(Long orderId) {
        Order order = orderService.getOrderById(orderId);
        Company company = order.getProduct().getCompany();
        Product product = order.getProduct();
        User user = order.getUser();

        if (user.getReserveBalance().compareTo(product.getPrice()) < 0 ||
                order.getStatus().equals(OrderStatus.ACCEPTED))
            return false;

        user.setReserveBalance(user.getReserveBalance().subtract(product.getPrice()));
        company.setBalance(company.getBalance().add(product.getPrice()));
        order.setStatus(OrderStatus.ACCEPTED);
        product.setUser(user);

        orderService.updateOrder(order.getId(), order);
        companyService.updateCompany(company.getId(), company);
        productService.updateProduct(product.getId(), product);
        userService.updateUser(user.getId(), user);

        return true;
    }

    //    сценарий разрезервирования денег, если услугу применить не удалось (отмена установки услуги)
    public Boolean unReserving(Long orderId) {
        Order order = orderService.getOrderById(orderId);
        User user = order.getUser();
        if (order.getStatus().equals(OrderStatus.ACCEPTED) ||
                user.getReserveBalance().compareTo(order.getProduct().getPrice()) < 0)
            return false;

        user.setReserveBalance(user.getReserveBalance().subtract(order.getProduct().getPrice()));
        user.setBalance(user.getBalance().add(order.getProduct().getPrice()));

        userService.updateUser(user.getId(), user);
        return true;
    }

    // метод для получения месячного отчета по каждой услуге компании
    public ResponseEntity<byte[]> downloadMonthlyProductReport(Long companyId) {
        Company company = companyService.getCompanyById(companyId);
        List<Product> products = company.getProducts();

        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            csvWriter.writeNext(new String[]{"ID", "Название", "Описание", "Сумма выручки за месяц"});
            for (Product product : products) {
                csvWriter.writeNext(new String[]{
                        product.getId().toString(),
                        product.getName(),
                        product.getDescription(),
                        orderService.orderSumByProductId(product.getId()).toString()
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return csvDownload.convertToByteArray(stringWriter);
    }

    // метод для получения месячного отчета по заказам
    public ResponseEntity<byte[]> downloadMonthlyReport(int year, int month) {
        // определяем начало и конец месяца
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

        // получаем список заказов за месяц
        return csvDownload.download(orderService.getOrdersByDateBetween(startOfMonth, endOfMonth));
    }

}
