package com.shadowsocks.service.impl;

import com.shadowsocks.dao.AdminDao;
import com.shadowsocks.dto.entity.Admin;
import com.shadowsocks.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminDao adminDao;

    @Override
    public Optional<Admin> login(String username, String password) {
        Admin admin = adminDao.login(username, password);
        return Optional.ofNullable(admin);
    }

    @Override
    public boolean updateAdminInfo(int adminId, String ip) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return adminDao.updateAdminInfo(time, ip, adminId);
    }
}
