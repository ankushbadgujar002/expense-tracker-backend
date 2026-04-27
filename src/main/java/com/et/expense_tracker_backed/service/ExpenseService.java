package com.et.expense_tracker_backed.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.et.expense_tracker_backed.entity.Expense;
import com.et.expense_tracker_backed.entity.User;
import com.et.expense_tracker_backed.repository.ExpenseRepository;
import com.et.expense_tracker_backed.repository.UserRepository;

@Service
public class ExpenseService {

	@Autowired
	private ExpenseRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
    public Expense addExpense(Expense expense, Long userId)
    {
    	User user = userRepository.findById(userId)
    			.orElseThrow(() -> new RuntimeException("User Not Found"));
    	
    	expense.setUser(user);
    	return repository.save(expense);
    }
    
    public List<Expense> getAllExpenses()
    {
    	return repository.findAll();
    }
    
    public Expense getExpenseById(Long id)
    {
    	return repository.findById(id)
    			.orElseThrow(() -> new RuntimeException("Expense not found"));
    }
    
    public List<Expense> getExpensesByUserId(Long userId)
    {
    	return repository.findByUserId(userId);
    }
    
    public Expense updateExpense(Long id, Expense expense)
    {
    	Expense existing = getExpenseById(id);
    	
    	 System.out.println("Category from request: " + expense.getCategory());
    	
    	existing.setTitle(expense.getTitle());
    	existing.setAmount(expense.getAmount());
    	existing.setCategory(expense.getCategory());
    	existing.setDate(expense.getDate());
    	
    	return repository.save(existing);
    }
    
    public Expense partialUpdate(Long id, Expense expense) {

        Expense existing = getExpenseById(id);

        if (expense.getTitle() != null) {
            existing.setTitle(expense.getTitle());
        }

        if (expense.getAmount() != 0) {
            existing.setAmount(expense.getAmount());
        }

        if (expense.getCategory() != null) {
            existing.setCategory(expense.getCategory());
        }
        
        if (expense.getDate() != null) {
            existing.setDate(expense.getDate());
        }

        return repository.save(existing);
    }
    
    public void deleteExpense(Long id)
    {  	
    	repository.deleteById(id);
    }
    
    public Map<String, Double> getCategorySummary(Long userId){

        List<Object[]> results = repository.getCategorySummary(userId);

        Map<String, Double> data = new HashMap<>();

        for(Object[] row : results){

            if(row[0] == null || row[1] == null){
                continue;
            }

            String category = row[0].toString();
            Double total = ((Number) row[1]).doubleValue();

            data.put(category, total);
        }

        return data;
    }
    
    public List<Map<String,Object>> getMonthlySummary(Long userId){

        List<Object[]> results = repository.getMonthlyExpenses(userId);

        List<Map<String,Object>> data = new ArrayList<>();

        for(Object[] row : results){

            Map<String,Object> map = new HashMap<>();

            map.put("month", row[0]);
            map.put("total", ((Number)row[1]).doubleValue());

            data.add(map);
        }

        return data;
    }
}
