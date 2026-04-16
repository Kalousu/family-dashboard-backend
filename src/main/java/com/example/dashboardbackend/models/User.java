package com.example.dashboardbackend.models;

import com.example.dashboardbackend.models.enums.UserColorMode;
import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.models.enums.UserAvatarType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    @Nullable
    private String pin;

    @Column(nullable = true)
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @Enumerated(EnumType.STRING)
    private UserAvatarType avatarType;

    @Column
    private String avatar;

    @Column
    private String color;

    @Enumerated(EnumType.STRING)
    private UserColorMode userColorMode;
}
