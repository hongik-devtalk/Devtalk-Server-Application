package com.hongik.devtalk.repository.admin;

import com.hongik.devtalk.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Boolean existsByLoginId(String loginId);

    Optional<Admin> findByLoginId(String loginId);

}
