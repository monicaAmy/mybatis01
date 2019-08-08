package com.su.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order
{
    private int id;
    private int userId;
    private String number;
    private User user;

}
