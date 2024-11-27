package com.firstspringtutorial.dreamshops.controller;

import com.firstspringtutorial.dreamshops.DTO.OrderDto;
import com.firstspringtutorial.dreamshops.exceptions.ResourceNotFoundException;
import com.firstspringtutorial.dreamshops.model.Order;
import com.firstspringtutorial.dreamshops.response.ApiResponse;
import com.firstspringtutorial.dreamshops.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("ITEM ORDERED SUCCESSFULLY" , orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error Occurred" , e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDto order =orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("ORDER FOUND SUCCESS!" , order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Oops , ORDER NOT FOUND" , e.getMessage()));
        }
    }

    @GetMapping("/{userId}/order")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try {
            List<OrderDto> order =orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("ORDER RETRIEVED SUCCESSFULLY!" , order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Oops , ORDER NOT FOUND" , e.getMessage()));
        }
    }


}
