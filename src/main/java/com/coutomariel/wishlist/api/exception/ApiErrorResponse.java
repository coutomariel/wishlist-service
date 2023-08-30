package com.coutomariel.wishlist.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    private HttpStatusCode status;
    private Map<String, String> errors;
    private String message;
}
