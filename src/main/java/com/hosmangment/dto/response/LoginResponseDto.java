package com.hosmangment.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        String jwt
) {
}
