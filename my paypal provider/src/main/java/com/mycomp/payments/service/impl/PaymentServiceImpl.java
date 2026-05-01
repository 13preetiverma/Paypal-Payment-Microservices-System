package com.mycomp.payments.service.impl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycomp.payments.http.HttpRequest;
import com.mycomp.payments.http.HttpServiceEngine;
import com.mycomp.payments.paypal.res.PaypalOrderRes;
import com.mycomp.payments.pojo.CreateOrderReq;
import com.mycomp.payments.pojo.OrderResponse;
import com.mycomp.payments.service.PaymentValidator;
import com.mycomp.payments.service.TokenService;
import com.mycomp.payments.service.helper.CreateOrderHelper;
import com.mycomp.payments.service.interfaces.PaymentService;
import com.mycomp.payments.util.JsonUtil;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	
    private final TokenService tokenService;
	
	private final HttpServiceEngine httpServiceEngine;
	private final PaymentValidator paymentValidator;
	
	private final JsonUtil jsonUtil;
	
	@Value("${paypal.create.order.url}")
	private String createOrderUrl;
	
	private final CreateOrderHelper createOrderHelper;
	
	
	@Override
	public OrderResponse createOrder(CreateOrderReq createOrderReq) {
		log.info("Creating order in PaymentServiceImpl|| createOrderReq:{}",
				createOrderReq);
		
		paymentValidator.validateCreateOrder(createOrderReq);
		
		
		String accessToken = tokenService.getAccessToken();
		log.info("Access token retrieved: {}", accessToken);
		
		HttpRequest httpRequest = createOrderHelper.prepareCreateOrderHttpRequest(
				createOrderReq, accessToken);
		log.info("Prepared HttpRequest for OAuth call: {}", httpRequest);
		
		ResponseEntity<String> successResponse = httpServiceEngine.makeHttpCall(httpRequest);
		log.info("HTTP response from HttpServiceEngine: {}", successResponse);

		PaypalOrderRes paypalOrderRes = jsonUtil.fromJson(
				successResponse.getBody(), PaypalOrderRes.class);
		log.info("Converted response body to PaypalOrder: {}", paypalOrderRes);
		

		OrderResponse orderResponse = createOrderHelper.toOrderResponse(paypalOrderRes);
		log.info("Converted OrderResponse: {}", orderResponse);
		
		return orderResponse;
	}
	
	
	public String method2() {
		return "method2 called";
	}
	
	@PostConstruct
	public void init() {
		log.info("PaymentServiceImpl initialized");
	}
}
