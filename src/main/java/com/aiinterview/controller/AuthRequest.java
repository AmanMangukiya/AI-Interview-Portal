package com.aiinterview.controller;

import lombok.Data;

@Data
class AuthRequest {
    private String email;
    private String password;
}
