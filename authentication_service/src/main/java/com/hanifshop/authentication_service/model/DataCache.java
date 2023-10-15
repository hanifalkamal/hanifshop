package com.hanifshop.authentication_service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import javax.persistence.*;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Getter
@Setter
@Entity
@Table(name = "data_cache")
public class DataCache {

    @Id
    @Column(name = "cache_key")
    private String cacheKey;

    @Column(name = "cache_value", columnDefinition = "TEXT")
    private String cacheValue;

    @Column(name = "expiration_time")
    private Date expirationTime;

}
