package com.mycomp.payments.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycomp.payments.pojo.CreateOrderReq;
import com.mycomp.payments.pojo.OrderResponse;
import com.mycomp.payments.service.interfaces.PaymentService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
	
private final PaymentService paymentService;
	
	@PostMapping("/orders")
	public OrderResponse createOrder(@RequestBody CreateOrderReq createOrderReq) {
		// TODO once the request & response is finalize, update this logic
		
		log.info("Creating order in PayPal provider service:{}", createOrderReq);
		
		OrderResponse response = paymentService.createOrder(createOrderReq);
		log.info("Order creation response from service: {}", response);
		
		return response;
		
	}
	
	@PostConstruct
	void init() {
		log.info("PaymentController initialized "
				+ "paymentService:{}", paymentService);
	}
 
}
