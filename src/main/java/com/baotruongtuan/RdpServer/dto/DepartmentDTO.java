package com.baotruongtuan.RdpServer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentDTO {
    int id;
    String name;
    String code;

    @JsonProperty("isJoined")
    @Builder.Default
    boolean isJoined = true;
}
