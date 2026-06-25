package guru.springframework.msscbrewery.web;

import guru.springframework.msscbrewery.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        if (userId == null || userId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "userId is required"));
        }
        // In a real app: validate userId exists in DB here
        String token = jwtUtil.generateToken(userId);
        return ResponseEntity.ok(Map.of("token", token));
    }
}