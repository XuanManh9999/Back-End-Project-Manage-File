package com.back_end_TN.project_tn.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse implements Serializable {
    private int status;
    private String message;
    private Object data;
    private Integer totalPage;
}
