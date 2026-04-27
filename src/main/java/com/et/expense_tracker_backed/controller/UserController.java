package com.et.expense_tracker_backed.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.et.expense_tracker_backed.entity.User;
import com.et.expense_tracker_backed.repository.ExpenseRepository;
import com.et.expense_tracker_backed.repository.UserRepository;
import com.et.expense_tracker_backed.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return service.saveUser(user);
    }

    @PutMapping("/budget")
    public ResponseEntity<?> updateBudget(@RequestBody Map<String, Object> request) {

        if (request.get("userId") == null || request.get("totalBudget") == null) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }

        Long userId = Long.valueOf(request.get("userId").toString());
        Double enteredBudget = Double.valueOf(request.get("totalBudget").toString());

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        double previousBudget = user.getTotalBudget() != null ? user.getTotalBudget() : 0;

        double totalSpent = expenseRepository.getTotalExpensesByUserId(userId);

        double carryForward = totalSpent > previousBudget
                ? totalSpent - previousBudget
                : 0;

        double finalBudget = enteredBudget - carryForward;

        if (finalBudget < 0) {
            finalBudget = 0;
        }

        user.setTotalBudget(finalBudget);
        user.setCarryForward(carryForward);

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("enteredBudget", enteredBudget);
        response.put("carryForward", carryForward);
        response.put("finalBudget", finalBudget);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/budget/{userId}")
    public ResponseEntity<?> getBudget(@PathVariable Long userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalBudget", user.getTotalBudget());
        response.put("carryForward", user.getCarryForward());

        return ResponseEntity.ok(response);
    }
}