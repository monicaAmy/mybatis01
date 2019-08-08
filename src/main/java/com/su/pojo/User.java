package com.su.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class User
{
    private Integer id;
    private String username;
    private Date birthday;

    /* * 数据库默认
     * 1 true
     * 0 false
     */
    // private int sex;
    // private Boolean sex;
    private String sex;
    private String address;

    private String nich;
}
