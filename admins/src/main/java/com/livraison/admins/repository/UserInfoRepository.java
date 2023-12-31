package com.livraison.admins.repository;

import com.livraison.admins.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    UserInfo findByLivreurId(Long id);

}
