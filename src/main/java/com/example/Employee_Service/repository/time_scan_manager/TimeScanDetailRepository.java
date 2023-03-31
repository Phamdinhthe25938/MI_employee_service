package com.example.Employee_Service.repository.time_scan_manager;

import com.example.Employee_Service.model.entity.time_scan_manager.TimeScanDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("TimeScanDetailRepository")
public interface TimeScanDetailRepository extends CrudRepository<TimeScanDetail, Long> {
}
