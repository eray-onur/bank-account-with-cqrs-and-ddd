package com.techbank.account.cmd.application.dto;

import com.techbank.account.common.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAccountResponse extends BaseResponse {
    private UUID id;

    public OpenAccountResponse(String message, UUID id) {
        super(message);
        this.id = id;
    }
}
