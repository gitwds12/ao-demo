# Spring boot 使用AOP 的例子
使用spring boot Aop 记录操作日志，异常日志。

启动类：DemoApplication

实现的类：HelloController

访问：http://localhost:8089/hello 等
## OperateLogAspect 切面类

有切入点和通知（After returning）异常时通知（After throwing）
## OperateLog 注解类
在方法上加注解，表示需要打日志