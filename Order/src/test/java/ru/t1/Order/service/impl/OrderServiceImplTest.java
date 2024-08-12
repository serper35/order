package ru.t1.Order.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import ru.t1.Order.dto.OrderDTO;
import ru.t1.Order.exception.OrderNotFoundException;
import ru.t1.Order.mapper.Mapper;
import ru.t1.Order.model.Order;
import ru.t1.Order.model.User;
import ru.t1.Order.repository.OrderRepository;
import ru.t1.Order.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Mapper mapper;

    @Test
    void testCreateOrderLogging(CapturedOutput output) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1);

        User user = new User();
        user.setId(1);

        Order order = new Order();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(mapper.convertOrderToEntity(orderDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(mapper.convertOrderToDTO(order)).thenReturn(orderDTO);

        orderService.createOrder(orderDTO);

        // Проверка логов
        assertThat(output).contains("Calling method createOrder with args")
                .contains("Returning from method createOrder with result");
    }

    @Test
    void testCreateOrderUserNotFoundLogging(CapturedOutput output) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderDTO));

        // Проверка логов
        assertThat(output).contains("Calling method createOrder with args")
                .contains("Exception thrown in method createOrder");
    }

    @Test
    void testUpdateOrderLogging(CapturedOutput output) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1);

        Order existingOrder = new Order();
        existingOrder.setId(1);

        when(orderRepository.findById(1)).thenReturn(Optional.of(existingOrder));
        when(mapper.convertOrderToEntity(orderDTO)).thenReturn(existingOrder);
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
        when(mapper.convertOrderToDTO(existingOrder)).thenReturn(orderDTO);

        orderService.updateOrder(orderDTO);

        // Проверка логов
        assertThat(output).contains("Calling method updateOrder with args")
                .contains("Returning from method updateOrder with result");
    }

    @Test
    void testGetOrderLogging(CapturedOutput output) {
        Order order = new Order();
        order.setId(1);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(mapper.convertOrderToDTO(order)).thenReturn(orderDTO);

        orderService.getOrder(1);

        // Проверка логов
        assertThat(output).contains("Calling method getOrder with args")
                .contains("Returning from method getOrder with result");
    }

    @Test
    void testGetOrderNotFoundLogging(CapturedOutput output) {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(1));

        // Проверка логов
        assertThat(output).contains("Calling method getOrder with args")
                .contains("Exception thrown in method getOrder");
    }

    @Test
    void testDeleteOrderLogging(CapturedOutput output) {
        Order order = new Order();
        order.setId(1);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1);

        // Проверка логов
        assertThat(output).contains("Calling method deleteOrder with args")
                .contains("Returning from method deleteOrder with result");
    }

    @Test
    void testDeleteOrderNotFoundLogging(CapturedOutput output) {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1));

        // Проверка логов
        assertThat(output).contains("Calling method deleteOrder with args")
                .contains("Exception thrown in method deleteOrder");
    }
}