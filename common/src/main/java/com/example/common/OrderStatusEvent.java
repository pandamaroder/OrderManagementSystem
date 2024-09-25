package com.example.common;


import java.time.LocalDateTime;

public record OrderStatusEvent(String status, LocalDateTime date) {
}
