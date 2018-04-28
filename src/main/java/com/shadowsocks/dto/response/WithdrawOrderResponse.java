package com.shadowsocks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawOrderResponse {
    private int total;
    private List<WithdrawRecord> withdrawRecordList;
}
