package com.shadowsocks.dto.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShadowsocksConfig {
    private String server;

    @SerializedName("local_address")
    private String localAddress;

    @SerializedName("local_port")
    private int localPort;

    @SerializedName("port_password")
    private String portAndPassword;

    private int timeout;

    private String method;

    @SerializedName("fast_open")
    private boolean fastOpen;
}
