package com.arsen.services;

import com.arsen.enums.OrderStatus;
import com.arsen.models.Company;
import com.arsen.models.Order;
import com.arsen.models.Product;
import com.arsen.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private CompanyService companyService;
    @Mock
    private ProductService productService;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OperationService operationService;

    @Test
    public void testReplenishment() {
        User user = User.builder().balance(new BigDecimal("5000.00")).build();

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(userService.updateUser(user.getId(), user)).thenReturn(user.getId());

        assertThat(operationService.replenishment(user.getId(), new BigDecimal("500.00"))).isTrue();
        assertThat(user.getBalance()).isEqualTo(new BigDecimal("5500.00"));
    }

    @Test
    public void testReserving() {
        User user = User.builder().balance(new BigDecimal("5000.00")).reserveBalance(BigDecimal.ZERO).build();
        Product product = Product.builder().price(new BigDecimal("1200.00")).build();

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(productService.getProductById(product.getId())).thenReturn(product);
        when(userService.updateUser(user.getId(), user)).thenReturn(user.getId());
        when(orderService.createOrder(any())).thenReturn(null);

        assertThat(operationService.reserving(user.getId(), product.getId())).isTrue();
        assertThat(user.getBalance()).isEqualTo(new BigDecimal("3800.00"));
        assertThat(user.getReserveBalance()).isEqualTo(product.getPrice());

        user.setBalance(BigDecimal.ZERO);
        assertThat(operationService.reserving(user.getId(), product.getId())).isFalse();
    }

    @Test
    public void testRevenueRecognition() {
        User user = User.builder().balance(new BigDecimal("5000.00"))
                .reserveBalance(new BigDecimal("2000.00")).build();
        Company company = Company.builder().balance(new BigDecimal("10000.00")).build();
        Product product = Product.builder().price(new BigDecimal("2000.00")).company(company)
                .users(new ArrayList<>()).build();
        Order order = Order.builder().status(OrderStatus.PENDING).product(product).user(user).build();

        when(orderService.getOrderById(order.getId())).thenReturn(order);
        when(orderService.updateOrder(any(), any())).thenReturn(null);
        when(companyService.updateCompany(any(), any())).thenReturn(null);
        when(productService.updateProduct(any(), any())).thenReturn(null);
        when(userService.updateUser(any(), any())).thenReturn(null);

        assertThat(operationService.revenueRecognition(order.getId())).isTrue();
        assertThat(user.getReserveBalance()).isEqualTo(new BigDecimal("0.00"));
        assertThat(company.getBalance()).isEqualTo(new BigDecimal("12000.00"));
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ACCEPTED);

        assertThat(operationService.revenueRecognition(order.getId())).isFalse();
    }

    @Test
    public void testUnReserving() {
        User user = User.builder().balance(new BigDecimal("5000.00"))
                .reserveBalance(new BigDecimal("3000.00")).build();
        Product product = Product.builder().price(new BigDecimal("1500.00")).build();
        Order order = Order.builder().status(OrderStatus.PENDING).user(user).product(product).build();

        when(orderService.getOrderById(order.getId())).thenReturn(order);
        when(userService.updateUser(any(), any())).thenReturn(null);

        assertThat(operationService.unReserving(order.getId())).isTrue();
        assertThat(user.getReserveBalance()).isEqualTo(new BigDecimal("1500.00"));
        assertThat(user.getBalance()).isEqualTo(new BigDecimal("6500.00"));
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);

        assertThat(operationService.unReserving(order.getId())).isFalse();
    }



}
