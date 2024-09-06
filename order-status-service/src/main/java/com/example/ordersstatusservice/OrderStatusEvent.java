package com.example.ordersstatusservice;


import java.time.LocalDateTime;

public record OrderStatusEvent(String status, LocalDateTime date) {
}
