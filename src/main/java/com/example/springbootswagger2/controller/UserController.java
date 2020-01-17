package com.example.springbootswagger2.controller;

import com.example.springbootswagger2.common.ResultObj;
import com.example.springbootswagger2.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 李家幸
 * @class 计科三班
 * @create 2020-01-14 21:42
 */
@RestController
@RequestMapping("user")
@Api(value = "A", consumes = "用户管理", produces = "B")
public class UserController {
    /**
     * 全查询
     */
    @ApiOperation(value = "用户查询", consumes = "查询所有用户")
    @GetMapping("queryAllUser")
    public List<User> queryAllUser() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            list.add(new User(1, "小明" + i, "地球" + i, new Date()));
        }
        return list;
    }

    /**
     * 根据id查询用户
     */
    @ApiOperation(value = "根据id查询用户",consumes = "根据id查询用户")
    @GetMapping("queryUserById")

    public User queryUserByid(@RequestParam("UserId") Integer id) {//加了@RequestParam后解决调试时验证问题
        return new User(id, "小明", "广东", new Date());
    }
    /**
     *添加一个用户
     */
    @PostMapping("addUser")
    @ApiOperation(value = "添加用户")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name="userId",value = "用户标识",required = true,paramType = "Integer",dataType = "Integer")
            }
    )
    // public ResultObj addUser(@RestquestBody User user){//要求验证前端所有数非空同时要求必须提交json格式数据
        public ResultObj addUser(User user){
        System.out.println(user);
        return new ResultObj(200,"添加成功");
    }
    /**
     *修改一个用户
     */
    @PostMapping("updateUser")
    @ApiOperation(value = "修改用户")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name="userId",value = "用户标识",required = true,paramType = "Integer",dataType = "Integer")
            }
    )
    // public ResultObj addUser(@RestquestBody User user){//要求验证前端所有数非空同时要求必须提交json格式数据
    public ResultObj updateUser(User user){
        System.out.println(user);
        return new ResultObj(200,"修改成功");
    }
    /**
     * 根据id删除用户
     */
    @DeleteMapping("deleteUser")
    @ApiOperation(value = "删除用户")
    public ResultObj deleteUser(@RequestParam("userId") Integer id){
        System.out.println(id);
        return new ResultObj(200,"删除成功");
    }




}
