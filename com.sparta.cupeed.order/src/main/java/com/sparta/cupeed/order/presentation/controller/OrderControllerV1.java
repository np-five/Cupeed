package com.sparta.cupeed.order.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderControllerV1 {

	// @PostMapping
	// public ResponseEntity<> createOrder() {
	// 	return ResponseEntity.ok();
	// }
	//
	// @GetMapping
	// public ResponseEntity<> getOrders() {
	// 	return ResponseEntity.ok();
	// }
	//
	// @GetMapping("/{orderId}")
	// public ResponseEntity<> getOrder() {
	// 	return ResponseEntity.ok();
	// }
	//
	// @PutMapping("/{orderId}")
	// public ResponseEntity<> updateOrder() {
	//
	// }
	//
	// @DeleteMapping("/{orderId}")
	// public ResponseEntity<> deleteOrder() {
	//
	// }
	//
	// @PostMapping("/{orderId}")
	// public ResponseEntity<> processOrder() {
	//
	// }

}
