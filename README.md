在团队开发中，一个好的 API 文档不但可以减少大量的沟通成本，还可以帮助一位新人快速上手业务。传统的做法是由开发人员创建一份 RESTful API 文档来记录所有的接口细节，并在程序员之间代代相传。

这种做法存在以下几个问题：

- API 接口众多，细节复杂，需要考虑不同的HTTP请求类型、HTTP头部信息、HTTP请求内容等，想要高质量的完成这份文档需要耗费大量的精力；
- 难以维护。随着需求的变更和项目的优化、推进，接口的细节在不断地演变，接口描述文档也需要同步修订，可是文档和代码处于两个不同的媒介，除非有严格的管理机制，否则很容易出现文档、接口不一致的情况

**Swagger2** 的出现就是为了从根本上解决上述问题。它作为一个规范和完整的框架，可以用于生成、描述、调用和可视化 RESTful 风格的 Web 服务：

1. 接口文档在线自动生成，文档随接口变动实时更新，节省维护成本
2. 支持在线接口测试，不依赖第三方工具

## 1.创建项目

### （1）已idea为例，创建项目所需要的dependencies 

![image-20200117112949301](https://github.com/Kahen/springboot-swagger2/blob/master/image/image-20200117112949301.png)

### (2) 引入pom.xml依赖

```xml
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>
```

### (3) 创建Swagger2 配置类

```java
package com.example.springbootswagger2.config;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Kahen
 * @create 2020-01-14 21:17
 */
@Configuration
@EnableSwagger2 //启用Swagger2
public class Swagger2AutoConfiguration {
    @Bean
    public Docket swaggerSpringMvcPlugin(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))  扫描类上的Api注解也可以@RestController
                // .apis(RequestHandlerSelectors.basePackage("com.example.springbootswagger2.controller"))
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().description("这是一个很NB的Api工具")
                /*名片*/
                .contact(new Contact("Kahen", "http://www.Kahen.com", "2020@example.cn"))
                /*版本*/
                .version("1.0")
                /*所有者*/
                .license("kahen")
                //构造
                .build();
    }
}

```

**Swagger2Configuration.java** 配置类的内容不多，配置完成后也很少变化，简单了解即可。

如上代码所示，通过 `@Configuration` 注解，让 Spring 加载该配置类。再通过 `@EnableSwagger2` 注解来启用Swagger2。成员方法 `createRestApi` 函数创建 `Docket` 的Bean之后，`apiInfo()` 用来创建该 `Api` 的基本信息（这些基本信息会展现在文档页面中）。`select()` 函数返回一个 `ApiSelectorBuilder`实例用来控制哪些接口暴露给 Swagger 来展现，本例采用指定扫描的`ApiOperation`； 还有一种指定扫描类上的`Api`注解也可以`@RestController`，还有一种是指定扫描的包路径来定义，Swagger 会扫描该包下所有 Controller 定义的 API，并产生文档内容（除了被 `@ApiIgnore` 指定的请求）。

### （4）本例需要模拟数据，创建一个`ResultObj`类

```java
package com.example.springbootswagger2.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kahen
 * @create 2020-01-14 19:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultObj {
    private Integer status;  //返回状态
    private Object msg;    //返回消息
}
```

### （5）创建User类

```java
package com.example.springbootswagger2.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author kahen
 * @create 2020-01-14 18:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;
    private String userName;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date birth;
}

```

### (6) 创建 UserControlller

```java
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
 * @author kahen
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
}
```

首先用全查询测试

### （7）启动项目

查找Swagger的主页，在包的Resources的html文件名就对了

![image-20200117122639524](https://github.com/Kahen/springboot-swagger2/blob/master/image/image-20200117122639524.png)

访问http://localhost:8080/swagger-ui.html

![image-20200117123213451](https://github.com/Kahen/springboot-swagger2/blob/master/image/image-20200117123213451.png)

对应自己的Usercontroller，按try it out 进去调试，其余自行试探

![image-20200117123632718](https://github.com/Kahen/springboot-swagger2/blob/master/image/image-20200117123632718.png)

### （8）调试时验证

这里我介绍几个注解：

​		1、@RequestParam ：加了@RequestParam后解决调试时验证问题

​		2、@RestquestBody ：要求验证前端所有数非空同时要求必须提交json格式数据

​		3、@ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面

​						 name：参数名 

​						value：参数的汉字说明、解释 required：参数是否必须传 

​						paramType：参数放在哪个地方

​						 · header --> 请求参数的获取：@RequestHeader 

​						 · query --> 请求参数的获取：@RequestParam 

​						 · path（用于restful接口）--> 请求参数的获取：@PathVariable

​						 · body（不常用） · form（不常用） 

​						dataType：参数类型，默认String，其它值dataType="Integer" 

​                        defaultValue：参数的默认值

![image-20200117124851887](https://github.com/Kahen/springboot-swagger2/blob/master/image/image-20200117124851887.png)

展示其中一个，下面给出完整代码

```java
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
 * @author kahen
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

```

### (9)更换皮肤

使用方法，把原来的swagger-ui 注释了换上bootstrap-ui，启动项目

```xml
<!--        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>-->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>swagger-bootstrap-ui</artifactId>
            <version>1.9.6</version>
        </dependency>
```

按照原来的方法寻找主页

访问http://localhost:8080/doc.html，得到主页

![image-20200117125431961](https://github.com/Kahen/springboot-swagger2/blob/master/image/image-20200117125431961.png)





*由于本人实力有限，许多东西不够完美，欢迎大家沟通交流*
