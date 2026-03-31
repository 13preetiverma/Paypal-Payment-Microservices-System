package com.mycomp.payments.service.impl;
import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.mycomp.payments.constant.Constant;
import com.mycomp.payments.paypal.req.Amount;
import com.mycomp.payments.paypal.req.ExperienceContext;
import com.mycomp.payments.paypal.req.OrderRequest;
import com.mycomp.payments.paypal.req.PaymentSource;
import com.mycomp.payments.paypal.req.Paypal;
import com.mycomp.payments.paypal.req.PurchaseUnit;
import com.mycomp.payments.pojo.CreateOrderReq;
import com.mycomp.payments.service.TokenService;
import com.mycomp.payments.service.interfaces.PaymentService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	
	private final TokenService tokenService;
	private final ObjectMapper objectMapper;
	
	
	@Override
	public String createOrder() {
		log.info("Creating order in PaymentServiceImpl");
		
		String accessToken = tokenService.getAccessToken();
		log.info("Access token retrieved: {}", accessToken);
		
		/* TODO 
		 	1. getAccessToken (OAuth)
			2. Call paypal createOrder
			3. Success/Failure/TimeOut - Proper response handling
			4. What to return to your calling service (payment-processing-service)
		 */
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		//set payment-request-id with uuid
		String uuid = java.util.UUID.randomUUID().toString();
		headers.add("payment-request-id", uuid);
		
	
		
		/*httpRequest body */
		// Create amount object
				Amount amount = new Amount();
				amount.setCurrencyCode("USD");
				amount.setValue("1.50");

				// read the amount from createOrderReq and convert to 2 decimal places format string
				//String amtStr = String.format(Constant.TWO_DECIMAL_FORMAT, createOrderReq.getAmount());
				//amount.setValue(amtStr);

				// Create purchase unit
				PurchaseUnit unit = new PurchaseUnit();
				unit.setAmount(amount);

				// Experience context
				ExperienceContext ctx = new ExperienceContext();
				ctx.setPaymentMethodPreference(Constant.IMMEDIATE_PAYMENT_REQUIRED);
				ctx.setLandingPage(Constant.LANDINGPAGE_LOGIN);
				ctx.setShippingPreference(Constant.SHIPPING_PREF_NO_SHIPPING);
				ctx.setUserAction(Constant.USER_ACTION_PAY_NOW);
				ctx.setReturnUrl("http://localhost:8080/payments/success");
				ctx.setCancelUrl("http://localhost:8080/payments/cancel");

				// Paypal object
				Paypal paypal = new Paypal();
				paypal.setExperienceContext(ctx);

				// Payment source
				PaymentSource ps = new PaymentSource();
				ps.setPaypal(paypal);

				// Final order request
				OrderRequest order = new OrderRequest();
				order.setIntent(Constant.INTENT_CAPTURE);
				order.setPurchaseUnits(Collections.singletonList(unit));
				order.setPaymentSource(ps);

				log.info("Constructed OrderRequest object: {}", order);
				
				// converting java obj to json
				String jsonObj = objectMapper.writerWithDefaultPrettyPrinter() .writeValueAsString(order);
				
				return jsonObj;


				

		
		//return "Order Created from service - " + accessToken;
	}
	
	public String method2() {
		return "method2 called";
	}
	
	@PostConstruct
	public void init() {
		log.info("PaymentServiceImpl initialized");
	}
}
