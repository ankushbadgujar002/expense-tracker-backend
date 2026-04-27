package com.et.expense_tracker_backed.repository;

import java.util.List;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.et.expense_tracker_backed.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long>{

	List<Expense> findByUserId(Long userId);
	
	@Query("""
		       SELECT e.category, SUM(e.amount)
		       FROM Expense e
		       WHERE e.user.id = :userId
		       GROUP BY e.category
		       """)
		List<Object[]> getCategorySummary(@Param("userId") Long userId);
		
	@Query("""
			   SELECT MONTH(e.date), SUM(e.amount)
			   FROM Expense e
			   WHERE e.user.id = :userId
			   GROUP BY MONTH(e.date)
			   ORDER BY MONTH(e.date)
			    """)
		List<Object[]> getMonthlyExpenses(@Param("userId") Long userId);
		
		@Query("""
			    SELECT COALESCE(SUM(e.amount), 0)
			    FROM Expense e 
			    WHERE e.user.id = :userId
			""")
			double getTotalExpensesByUserId(@Param("userId") Long userId);
}
