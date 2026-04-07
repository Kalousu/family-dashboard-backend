package com.example.dashboardbackend.models;

import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.models.enums.UserPfp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue( strategy= GenerationType.IDENTITY )
    private Long id;

    @Column
    private String name;

    @Column
    private String password;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    UserRole userRole;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "family_id")
    private Family family;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    UserPfp userPfp;

    @Column(nullable = true)
    private String pfpColour;
}
