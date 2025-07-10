package user_management.user_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission {

    @Id
    @GeneratedValue
    @Column(name = "permission_id", columnDefinition = "BINARY(16)")
    private UUID permissionId;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private String resource;

    @Column(nullable = false)
    private String action;
}

