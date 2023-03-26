package com.example.Employee_Service.model.entity.time_scan_manager;

import com.obys.common.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "time_scan_detail")
public class TimeScanDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_employee")
    private String codeEmployee;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "time_office")
    private Integer timeOffice;

    @Column(name = "time_reality")
    private Integer timeReality;

    private Boolean status;
}
