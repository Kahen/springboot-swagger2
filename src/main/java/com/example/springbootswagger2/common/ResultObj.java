package com.example.springbootswagger2.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 李家幸
 * @class 计科三班
 * @create 2020-01-14 19:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultObj {
    private Integer status;
    private Object msg;
}
