package com.metaverse.workflow.login.repository;

import com.metaverse.workflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<User, String> {

    /*@Query("FROM user WHERE mobile_no=:mobileNo")
    List<UserEntity> findAllByMobileNo(@Param("mobileNo") Long mobileNo);*/
    
    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(String s);

    Optional<User> findFirstByUserRoleIgnoreCase(String role);
}
