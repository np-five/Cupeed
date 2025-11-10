package com.sparta.cupeed.user.auth.presentation.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthException extends RuntimeException {

	private final AuthError authError;
}
