package user_management.user_management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSummaryDTO {
    private Long userId;
    private String username;
    private String email;
}
