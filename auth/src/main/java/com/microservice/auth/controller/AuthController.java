package com.microservice.auth.controller;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest req) {
        return service.login(req.getEmail(), req.getPassword());
    }
}