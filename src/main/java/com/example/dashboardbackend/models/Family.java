package com.example.dashboardbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint (columnNames = "familyName")
})
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String familyName;

    private String password;

    private String email;

    @OneToOne(mappedBy = "family", cascade = CascadeType.ALL)
    private Dashboard dashboard;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    private List<User> users;
}
