package com.hanifshop.authentication_service.model;

import javax.persistence.*;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

}
