package com.livraison.admins.repository;

import com.livraison.admins.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByEmail(String email);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
