package com.example.Employee_Service.model.dto.request.employee;

import com.the.common.model.payload.response.MetaList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListLogVacationByAssignRequest {
  private Long idAssign;
  private MetaList meta;
}
