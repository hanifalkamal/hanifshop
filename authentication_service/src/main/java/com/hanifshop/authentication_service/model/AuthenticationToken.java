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
@Table(name = "authentication_tokens")
public class AuthenticationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration_time")
    private Date expirationTime;

}
