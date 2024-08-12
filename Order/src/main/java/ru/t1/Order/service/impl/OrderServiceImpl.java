package ru.t1.Order.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.t1.Order.dto.OrderDTO;
import ru.t1.Order.exception.OrderNotFoundException;
import ru.t1.Order.mapper.Mapper;
import ru.t1.Order.model.Order;
import ru.t1.Order.model.User;
import ru.t1.Order.repository.OrderRepository;
import ru.t1.Order.repository.UserRepository;
import ru.t1.Order.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, Mapper mapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }


    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = mapper.convertOrderToEntity(orderDTO);
        order.setUser(user);
        Order savedOrder = orderRepository.save(order);
        return mapper.convertOrderToDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        Order order = mapper.convertOrderToEntity(orderDTO);
        Order fromDb = orderRepository.findById(order.getId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found exception"));

        fromDb.setDescription(order.getDescription());
        fromDb.setStatus(order.getStatus());

        Order updatedOrder = orderRepository.save(fromDb);
        return mapper.convertOrderToDTO(updatedOrder);
    }

    @Override
    public OrderDTO getOrder(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found exception"));
        return mapper.convertOrderToDTO(order);
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found exception"));
        orderRepository.deleteById(id);
    }
}
