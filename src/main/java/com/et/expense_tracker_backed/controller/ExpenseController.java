package com.et.expense_tracker_backed.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.et.expense_tracker_backed.entity.Expense;
import com.et.expense_tracker_backed.repository.ExpenseRepository;
import com.et.expense_tracker_backed.service.ExpenseService;

@RestController
@RequestMapping("/expenses")
@CrossOrigin(origins = "http://localhost:5173")
public class ExpenseController {

	@Autowired
	private ExpenseService service;
	
	@PostMapping("/user/{userId}")
	public Expense addExpense(@RequestBody Expense expense,
							  @PathVariable Long userId)
	{
		return service.addExpense(expense, userId);
	}
	
	@GetMapping
	public List<Expense> getAllExpenses()
	{
		return service.getAllExpenses();
	}
	
	@GetMapping("/user/{userId}")
	public List<Expense> getExpensesByUserId(@PathVariable Long userId)
	{
		return service.getExpensesByUserId(userId);
	}
	
	@GetMapping("/{id}")
	public Expense getExpenseById(@PathVariable Long id)
	{
		return service.getExpenseById(id);
	}
	
	@PutMapping("/{id}")
	public Expense updateExpense(@PathVariable Long id, 
								 @RequestBody Expense expense)
	{
		return service.updateExpense(id, expense);
		
	}
	
	@PatchMapping("/{id}")
	public Expense partialUpdate(@PathVariable Long id, @RequestBody Expense expense) {
	    return service.partialUpdate(id, expense);
	}
	
	@DeleteMapping("/{id}")
	public void deleteExpense(@PathVariable Long id)
	{
		service.deleteExpense(id);
	}
	
	@GetMapping("/category-summary/{userId}")
	public ResponseEntity<?> getCategorySummary(@PathVariable Long userId){
	    return ResponseEntity.ok(service.getCategorySummary(userId));
	}
	
	@GetMapping("/monthly-summary/{userId}")
	public List<Map<String,Object>> getMonthlySummary(@PathVariable Long userId){
	    return service.getMonthlySummary(userId);
	}
}
