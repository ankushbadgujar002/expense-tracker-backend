package com.et.expense_tracker_backed.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.et.expense_tracker_backed.entity.User;
import com.et.expense_tracker_backed.repository.ExpenseRepository;
import com.et.expense_tracker_backed.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	public User saveUser(User user)
	{
		return repository.save(user);
	}
	
	public Map<String, Double> updateBudget(Long userId, Double enteredBudget) {

	    User user = repository.findById(userId).orElse(null);

	    if (user == null) {
	        throw new RuntimeException("User not found");
	    }

	    double previousBudget = user.getTotalBudget();

	    double totalSpent = expenseRepository.getTotalExpensesByUserId(userId);

	    double carryForward = totalSpent - previousBudget;

	    double finalBudget;

	    if (carryForward > 0) {
	        finalBudget = enteredBudget - carryForward;
	    } else {
	        finalBudget = enteredBudget;
	    }

	    if (finalBudget < 0) {
	        finalBudget = 0;
	    }

	    user.setTotalBudget(finalBudget);
	    user.setCarryForward(carryForward);

	    repository.save(user);

	    return Map.of(
	        "enteredBudget", enteredBudget,
	        "carryForward", carryForward,
	        "finalBudget", finalBudget
	    );
	}
}
