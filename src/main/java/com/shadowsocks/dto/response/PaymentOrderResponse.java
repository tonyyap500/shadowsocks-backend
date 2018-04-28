package com.shadowsocks.dto.response;

import com.shadowsocks.dto.entity.PayOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOrderResponse {
    private int total;
    private List<PayOrder> payOrderList;
}
