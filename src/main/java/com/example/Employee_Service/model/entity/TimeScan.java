package com.example.Employee_Service.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.obys.common.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "time_scan")
public class TimeScan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code_employee")
    private String codeEmployee;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "time_scan")
    @JsonFormat(pattern = "dd-mm-yyyy")
    private Date timeScan;

    @Column(name = "type_scan")
    private Integer typeScan;

    @Column(name = "date_scan")
    private Integer dateScan;

    @Column(name = "month_scan")
    private Integer monthScan;

    @Column(name = "year_scan")
    private Integer yearScan;

}
