package com.example.Employee_Service.model.dto.response.employee;

import com.example.Employee_Service.model.entity.employee.LogVacationDayEntity;
import com.the.common.model.payload.response.MetaList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListVacationResponse {

  private List<LogVacationDayEntity> item;

  private MetaList meta;
}
