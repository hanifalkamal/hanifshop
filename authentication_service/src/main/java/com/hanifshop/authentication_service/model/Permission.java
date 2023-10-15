package com.hanifshop.authentication_service.model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(name = "permission_name", nullable = false)
    private String permissionName;

}
