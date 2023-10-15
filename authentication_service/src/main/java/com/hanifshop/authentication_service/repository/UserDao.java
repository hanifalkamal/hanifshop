package com.hanifshop.authentication_service.repository;

import com.hanifshop.authentication_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Hanif al kamal 15/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}
