package com.hosmangment.dto.response.helperDto;

import lombok.Builder;

@Builder
public record CreatorSummaryDto(
        Long id,
        String name,
        String role
) {
}
