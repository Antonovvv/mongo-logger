mongo-logger
=
    使用MongoDB的Spring Boot项目日志应用
引入及配置
-
直接通过Maven引入
```
<dependency>
 <groupId>io.github.Antonovvv</groupId>
 <artifactId>mongo-logger-core</artifactId>
 <version>1.0</version>
</dependency>
```
在application.yml(或application.properties)中进行配置：
```
mongologger:
    uri: mongodb://[username:password@]host1[:port1][,...hostN[:portN]][/[defaultauthdb][?options]]
    host: xxx(default localhost)
    port: xxx(default 27017)
    database: xxx(default test)
    username: xxx
    password: xxx
    replicaSetName: xxx
```
其中，当uri选项存在时其他选项将被忽略
uri的详细格式参见[MongoDB Connection String](https://docs.mongodb.com/manual/reference/connection-string/#connections-connection-options)

使用
-
在SpringApplication上添加@EnableMongoLogger注解，MongoLogger将根据配置文件自动完成组件扫描等工作；
```
@SpringBootApplication
@EnableMongoLogger
public class ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
```
按需引入MongoLogger对象，通过add()方法暂存key-value，调用commit()方法完成日志提交，可以通过commit指定提交到MongoDB的集合名称
```
@Controller
public class HomeController {
    @Autowired
    private MongoLogger mongoLogger;

    @ResponseBody
    @RequestMapping("/test")
    public String custom() {
        mongoLogger.add("test", 1);
        mongoLogger.commit("custom");
        return "";
    }
}
```
可以在方法上使用@MLog注解，mongo-logger将根据情况决定日志的类型(LogType.EXEC or LogType.WEB)并自动提交日志，
在被注解的方法中add的信息也将被一并提交。

可以指定@MLog的type参数改变日志类型，指定collectionName参数改变默认的集合名称；
```
@MLog(collectionName = "param")
@ResponseBody
@RequestMapping("/log")
public String home(HttpServletRequest request, HttpServletResponse response) {
    return "";
}
```
特别地，在没有@MLog的方法中调用被@MLog注解了的方法（实现上面的能力），还没有得到很好的支持，暂时不推荐在自调用方法上打日志

如果一定要这样做，MongoLogger提供了下面的API
```
public class HomeController {
    @Autowired
    private MongoLogger mongoLogger;

    @ResponseBody
    @RequestMapping("/custom-log")
    public String custom() {
        mongoLogger.getSelf(HomeController.class).test();
        return "";
    }

    @MLog()
    public String test() {
        mongoLogger.add("test method", true);
        return "";
    }
}
```

日志分析
---
mongo-logger的核心部分(mongo-logger-core)部分深度依赖了Spring Boot，尽量做到了减少配置，开箱即用

考虑到MongoDB有限的数据聚合能力，并且实际项目(中国大学搜索)中使用了Hadoop平台，所以mongo-logger可以通过Hadoop进行日志分析

mongo-logger-mapreduce包提供了这样的能力

TODO: 提供Spring Service or 打成jar包