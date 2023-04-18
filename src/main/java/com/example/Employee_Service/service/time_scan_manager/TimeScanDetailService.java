package com.example.Employee_Service.service.time_scan_manager;

import com.example.Employee_Service.model.dto.request.time_scan_manager.GetListTimeScanDetailRequest;
import com.example.Employee_Service.model.dto.response.time_scan_manager.GetListTimeScanDetailResponse;
import com.example.Employee_Service.model.dto.response.time_scan_manager.GetTimeScanDetailResponse;
import com.example.Employee_Service.model.entity.time_scan_manager.TimeScanDateDetailEntity;
import com.example.Employee_Service.repository.time_scan_manager.TimeScanDateDetailRepository;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.obys.common.model.payload.response.BaseResponse;
import com.obys.common.service.BaseService;
import com.obys.common.system_message.SystemMessageCode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("TimeScanDetailService")
public class TimeScanDetailService extends BaseService {
  @Resource
  @Qualifier("EmployeeValidator")
  private EmployeeValidator employeeValidator;

  @Resource
  @Qualifier("ModelMapper")
  private ModelMapper modelMapper;

  @Resource
  @Qualifier("TimeScanDateDetailRepository")
  private TimeScanDateDetailRepository timeScanDateDetailRepository;

  public BaseResponse<?> search(GetListTimeScanDetailRequest request) {
    employeeValidator.employeeExist(request.getIdEmployee());
    List<TimeScanDateDetailEntity> getAll = timeScanDateDetailRepository.findByIdEmployee(request.getIdEmployee());

    Map<String, List<TimeScanDateDetailEntity>> responseMap = getAll.stream()
        .collect(Collectors.groupingBy
            (e -> String.format("%d-%d", e.getDateWork().getMonth().getValue(), e.getDateWork().getYear()),
                Collectors.toList()));

    List<GetListTimeScanDetailResponse> response = responseMap.entrySet().stream()
        .map(e -> {
          String[] yearMonth = e.getKey().split("-");
          List<GetTimeScanDetailResponse> itemRes = e.getValue().stream().map(i -> modelMapper.map(i, GetTimeScanDetailResponse.class)).collect(Collectors.toList());
          return new GetListTimeScanDetailResponse(YearMonth.of(Integer.parseInt(yearMonth[1]), Integer.parseInt(yearMonth[0])),
              itemRes);
        }).collect(Collectors.toList());

    return responseV1(
        SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.GET_SUCCESS,
        response
    );
  }
}
