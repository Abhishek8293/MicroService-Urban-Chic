package com.urbanchic.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

   private T data;
   private String message;
   private String timestamp;
   private int statusCode;
   private  boolean success;

}
