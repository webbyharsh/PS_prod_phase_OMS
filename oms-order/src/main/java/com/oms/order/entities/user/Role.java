package com.oms.order.entities.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "oms_role")
@Getter @Setter
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long roleId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}