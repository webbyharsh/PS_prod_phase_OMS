package com.oms.order.entities.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Set;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@Entity
@Table(name = "oms_user")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Email(message = "Email should be valid")
    @Column(name = "username")
    private String emailId;

    private boolean enabled;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    @Column(name = "contact")
    private String contact;

    @Type(type = "jsonb")
    @Column(name = "address", columnDefinition = "jsonb")
    private Address address;

    @Column(name = "age")
    private Integer age;


    //Remove either roleId or roles

    @Column(name = "role_id")
    private Integer roleId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "oms_user_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles;


    @PrePersist
    public void created() {
        this.createdAt = LocalDateTime.now();
    }
}