package com.et.expense_tracker_backed.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

	private String username;
	private String email;
	private String password;
}
