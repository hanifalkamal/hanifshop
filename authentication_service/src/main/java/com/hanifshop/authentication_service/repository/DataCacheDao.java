package com.hanifshop.authentication_service.repository;

import com.hanifshop.authentication_service.model.DataCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Repository
public interface DataCacheDao extends JpaRepository<DataCache, Integer> {
}
