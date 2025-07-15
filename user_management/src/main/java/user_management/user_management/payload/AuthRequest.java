package user_management.user_management.payload;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}