package com.firstspringtutorial.dreamshops.controller;

import com.firstspringtutorial.dreamshops.exceptions.ResourceNotFoundException;
import com.firstspringtutorial.dreamshops.model.Cart;
import com.firstspringtutorial.dreamshops.model.User;
import com.firstspringtutorial.dreamshops.response.ApiResponse;
import com.firstspringtutorial.dreamshops.service.cart.ICartItemService;
import com.firstspringtutorial.dreamshops.service.cart.ICartService;
import com.firstspringtutorial.dreamshops.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart( @RequestParam Long productId, @RequestParam Integer quantity){
        try {
            User user =   userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(user);

            cartItemService.addItemToCart(cart.getId() , productId , quantity);
            return ResponseEntity.ok(new ApiResponse("ITEM ADDED SUCCESSFULLY!" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }catch(JwtException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage() , null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId , @PathVariable Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId , itemId);
            return ResponseEntity.ok(new ApiResponse("ITEM REMOVED SUCCESSFULLY" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId , @PathVariable Long itemId , @RequestParam Integer quantity){
        try {
            cartItemService.updateItemQuantity(cartId , itemId ,quantity);
            return ResponseEntity.ok(new ApiResponse("ITEM UPDATED SUCCESSFULLY" , null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage() , null));
        }
    }

}
