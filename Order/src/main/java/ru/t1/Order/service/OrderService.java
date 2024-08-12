package ru.t1.Order.service;

import ru.t1.Order.dto.OrderDTO;
public interface OrderService {
    OrderDTO createOrder(OrderDTO order);

    OrderDTO updateOrder(OrderDTO order);

    OrderDTO getOrder(int id);

    void deleteOrder(int id);
}
