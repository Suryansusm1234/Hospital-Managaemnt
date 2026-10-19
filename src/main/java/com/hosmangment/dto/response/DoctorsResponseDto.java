package com.hosmangment.dto.response;

import lombok.Builder;

@Builder
public record DoctorsResponseDto(
          Long id ,
          String name ,
          String email ,
          String phone_no ,
          String username ,
          String designation ,
          Boolean isAvailable
) {
}
