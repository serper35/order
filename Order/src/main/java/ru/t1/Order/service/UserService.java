package ru.t1.Order.service;

import ru.t1.Order.dto.OrderDTO;
import ru.t1.Order.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO user);

    UserDTO updateUser(UserDTO user);

    UserDTO getUser(int id);

    void deleteUser(int id);

    List<OrderDTO> getOrders(int id);
}
