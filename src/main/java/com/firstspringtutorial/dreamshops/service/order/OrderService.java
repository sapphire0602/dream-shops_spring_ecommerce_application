package com.firstspringtutorial.dreamshops.service.order;

import com.firstspringtutorial.dreamshops.DTO.OrderDto;
import com.firstspringtutorial.dreamshops.enums.OrderStatus;
import com.firstspringtutorial.dreamshops.exceptions.ResourceNotFoundException;
import com.firstspringtutorial.dreamshops.model.Cart;
import com.firstspringtutorial.dreamshops.model.Order;
import com.firstspringtutorial.dreamshops.model.OrderItem;
import com.firstspringtutorial.dreamshops.model.Product;
import com.firstspringtutorial.dreamshops.repository.OrderRepository;
import com.firstspringtutorial.dreamshops.repository.ProductRepository;
import com.firstspringtutorial.dreamshops.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    //private final IOrderService orderService;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final ProductRepository productrepository;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);

        List<OrderItem> orderItemList = createOrderItems(order , cart);
        order.setOrderItems(new HashSet<>(orderItemList));

        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order , Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productrepository.save(product);
            return new OrderItem(
                    order,product, cartItem.getQuantity(), cartItem.getUnitPrice()
            );
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity()))).reduce(BigDecimal.ZERO , BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this :: convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }


    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order , OrderDto.class);
    }
}