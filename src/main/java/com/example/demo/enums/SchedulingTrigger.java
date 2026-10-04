//package com.example.demo.enums;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.example.demo.service.OrderCancellationService;
//
//@Component
//public class SchedulingTrigger {
//	@Autowired
//	public OrderCancellationService service;
//	@Scheduled(fixedDelay = 3600000)
//    public void sweepPendingOrders() {
//        service.pendingOrderFiltration();
//    }
//
//}
