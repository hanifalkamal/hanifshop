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
@Table(name = "data_grid")
public class DataGrid {
    @Id
    @Column(name = "grid_key")
    private String gridKey;

    @Column(name = "grid_value", columnDefinition = "TEXT")
    private String gridValue;
}