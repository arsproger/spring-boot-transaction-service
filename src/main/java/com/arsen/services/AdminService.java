package com.arsen.services;

import com.arsen.enums.OrderStatus;
import com.arsen.models.Company;
import com.arsen.models.Order;
import com.arsen.models.Product;
import com.arsen.models.User;
import com.arsen.repositories.CompanyRepository;
import com.arsen.repositories.OrderRepository;
import com.arsen.repositories.ProductRepository;
import com.arsen.repositories.UserRepository;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final CompanyRepository companyRepository;

    @Autowired
    public AdminService(UserService userService, ProductService productService,
                        UserRepository userRepository, OrderRepository orderRepository,
                        OrderService orderService, CompanyRepository companyRepository,
                        ProductRepository productRepository) {
        this.userService = userService;
        this.productService = productService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
    }

    // начисления средств на баланс пользователя
    public Boolean replenishment(Long userId, BigDecimal amount) {
        User user = userService.getUserById(userId);
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
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
        userRepository.save(user);
        orderRepository.save(
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

        orderRepository.save(order);
        companyRepository.save(company);
        productRepository.save(product);
        userRepository.save(user);

        return true;
    }

    public String generateMonthlyReport(int year, int month) {
        // создаем форматтер для даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // определяем начало и конец месяца
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

        // получаем список заказов за месяц
        List<Order> orders = orderRepository.findByDateBetween(startOfMonth, endOfMonth);

        // создаем CSV файл
        String fileName = "monthly_report_" + year + "-" + month + ".csv";
        try (PrintWriter writer = new PrintWriter(fileName)) {
            // записываем заголовок файла
            writer.write("Order ID,Date,Status,User ID,Product ID\n");

            // записываем данные о заказах
            for (Order order : orders) {
                writer.write(order.getId() + ",");
                writer.write(order.getDate().format(formatter) + ",");
                writer.write(order.getStatus() + ",");
                writer.write(order.getUser().getId() + ",");
                writer.write(order.getProduct().getId() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // возвращаем ссылку на CSV файл
        return fileName;
    }

    public ResponseEntity<byte[]> download(int year, int month) {
        // создаем форматтер для даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // определяем начало и конец месяца
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

        // получаем список заказов за месяц
        List<Order> orders = orderRepository.findByDateBetween(startOfMonth, endOfMonth);

        // Создаем CSVWriter для записи данных в CSV-файл
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        // Записываем заголовок CSV-файла
        csvWriter.writeNext(new String[]{"ID", "Дата", "Статус", "Имя пользователя", "Наименование товара"});

        // Записываем данные заказов в CSV-файл
        for (Order order : orders) {
            csvWriter.writeNext(new String[]{
                    order.getId().toString(),
                    order.getDate().toString(),
                    order.getStatus().toString(),
                    order.getUser().getFullName(),
                    order.getProduct().getName()
            });
        }

        // Освобождаем ресурсы CSVWriter'а
        try {
            csvWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Конвертируем данные CSV-файла в массив байтов
        byte[] csvData = stringWriter.toString().getBytes();

        // Создаем HTTP-ответ с заголовками, указывающими на тип и имя файла
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("orders.csv").build());

        // Возвращаем HTTP-ответ с массивом байтов CSV-файла
        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

}
