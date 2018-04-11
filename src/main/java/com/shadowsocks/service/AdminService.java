package com.shadowsocks.service;

import com.shadowsocks.dto.entity.Admin;

import java.util.Optional;

public interface AdminService {

    Optional<Admin> login(String username, String password);

    boolean updateAdminInfo(int adminId, String ip);
}
