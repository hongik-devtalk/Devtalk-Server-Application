package com.hongik.devtalk.service.admin;

import com.hongik.devtalk.domain.Admin;
import com.hongik.devtalk.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQueryService {

    private final AdminRepository adminRepository;

    public List<Admin> findAll() {

        return adminRepository.findAll();
    }
}
