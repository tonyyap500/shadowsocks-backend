package com.shadowsocks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GaotongCallbackDto {
    private String partner;
    private String orderNumber;
    private String orderStatus;
    private String payMoney;
    private String key;
    private String sign;

    @Override
    public String toString() {
        return "partner=" + partner + "&ordernumber=" + orderNumber + "&orderstatus=" + orderStatus + "&paymoney=" + payMoney + key;
    }
}
