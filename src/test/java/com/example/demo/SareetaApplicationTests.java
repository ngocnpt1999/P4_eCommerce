package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class SareetaApplicationTests {

    @Autowired
    private CartController cartController;

    @Autowired
    private ItemController itemController;

    @Autowired
    private OrderController orderController;

    @Autowired
    private UserController userController;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCreateUser() throws Exception {
        String username = generateUsername();
        String password = "test1234";
        CreateUserRequest userRequest = createUserRequest(username, password);

        ResponseEntity<User> response = userController.createUser(userRequest);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody().getId() > 0);
        Assert.assertEquals(username, response.getBody().getUsername());
    }

    @Test
    public void testGetUserById() throws Exception {
        String username = generateUsername();
        String password = "test1234";
        CreateUserRequest userRequest = createUserRequest(username, password);
        ResponseEntity<User> createUserResponse = userController.createUser(userRequest);
        Assert.assertNotNull(createUserResponse.getBody());

        ResponseEntity<User> response = userController.findById(createUserResponse.getBody().getId());
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(createUserResponse.getBody().getId(), response.getBody().getId());
        Assert.assertEquals(createUserResponse.getBody().getUsername(), response.getBody().getUsername());
        Assert.assertEquals(createUserResponse.getBody().getPassword(), response.getBody().getPassword());
    }

    @Test
    public void testGetUserByUserName() throws Exception {
        String username = generateUsername();
        String password = "test1234";
        CreateUserRequest userRequest = createUserRequest(username, password);
        ResponseEntity<User> createUserResponse = userController.createUser(userRequest);
        Assert.assertNotNull(createUserResponse.getBody());

        ResponseEntity<User> response = userController.findByUserName(username);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(createUserResponse.getBody().getId(), response.getBody().getId());
        Assert.assertEquals(createUserResponse.getBody().getUsername(), response.getBody().getUsername());
        Assert.assertEquals(createUserResponse.getBody().getPassword(), response.getBody().getPassword());
    }

    @Test
    public void testAddToCart() throws Exception {
        String username = generateUsername();
        String password = "test1234";
        CreateUserRequest userRequest = createUserRequest(username, password);
        ResponseEntity<User> createUserResponse = userController.createUser(userRequest);
        Assert.assertNotNull(createUserResponse.getBody());

        ModifyCartRequest cartRequest = createCartRequest(username);
        ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testRemoveFromCart() throws Exception {
        String username = generateUsername();
        String password = "test1234";
        CreateUserRequest userRequest = createUserRequest(username, password);
        ResponseEntity<User> createUserResponse = userController.createUser(userRequest);
        Assert.assertNotNull(createUserResponse.getBody());

        ModifyCartRequest cartRequest = createCartRequest(username);
        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testGetItems() throws Exception {
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testGetItemById() throws Exception {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testGetItemsByName() throws Exception {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testSubmitOrder() throws Exception {
        String username = generateUsername();
        String password = "test1234";
        CreateUserRequest userRequest = createUserRequest(username, password);
        ResponseEntity<User> createUserResponse = userController.createUser(userRequest);
        Assert.assertNotNull(createUserResponse.getBody());

        ModifyCartRequest cartRequest = createCartRequest(username);
        ResponseEntity<Cart> cartResponse = cartController.addTocart(cartRequest);
        Assert.assertNotNull(cartResponse.getBody());

        ResponseEntity<UserOrder> response = orderController.submit(username);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testGetOrdersForUser() throws Exception {
        String username = generateUsername();
        String password = "test1234";
        CreateUserRequest userRequest = createUserRequest(username, password);
        ResponseEntity<User> createUserResponse = userController.createUser(userRequest);
        Assert.assertNotNull(createUserResponse.getBody());

        ModifyCartRequest cartRequest = createCartRequest(username);
        ResponseEntity<Cart> cartResponse = cartController.addTocart(cartRequest);
        Assert.assertNotNull(cartResponse.getBody());

        ResponseEntity<UserOrder> submittedOrderResponse = orderController.submit(username);
        Assert.assertNotNull(submittedOrderResponse.getBody());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertFalse(response.getBody().isEmpty());
    }

    private ModifyCartRequest createCartRequest(String username) {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername(username);
        cartRequest.setItemId(1);
        cartRequest.setQuantity(1);
        return cartRequest;
    }

    private CreateUserRequest createUserRequest(String username, String password) {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername(username);
        userRequest.setPassword(password);
        userRequest.setConfirmPassword(password);
        return userRequest;
    }

    private String generateUsername() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder username = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 12; i++) {
            username.append(chars.charAt(random.nextInt(chars.length())));
        }

        return username.toString();
    }

}
