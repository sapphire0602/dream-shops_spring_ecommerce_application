package com.firstspringtutorial.dreamshops.service.order;

import com.firstspringtutorial.dreamshops.DTO.OrderDto;
import com.firstspringtutorial.dreamshops.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder (Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
