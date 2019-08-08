package com.su.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Employee
{
    private Integer empNo;
    private String ename;
    private String job;
    private Double sal;
    private Date hireDate;

}
