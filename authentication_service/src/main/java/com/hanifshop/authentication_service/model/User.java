package com.hanifshop.authentication_service.model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "session_expiration_time")
    private Date sessionExpirationTime;

    @Column(name = "last_activity_time")
    private Date lastActivityTime;

}
