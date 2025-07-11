package user_management.user_management.dto;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private Set<String> permissions; // or List<PermissionDTO>
    private Set<UserSummaryDTO> users;
}
