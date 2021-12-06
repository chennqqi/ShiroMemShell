# ShiroMemShell

**工具仅用于安全研究以及内部自查，禁止使用工具发起非法攻击，造成的后果使用者负责**

关于`Shiro`反序列化如何在`SpringBoot`环境下注入`Tomcat`内存马以及请求头过大如何解决

这一系列过程网上很多文章都写地晦涩难懂，要么藏一手不写全，让Java安全小白难以学习，于是我把这一系列过程代码写出来

使用配套环境即可成功：https://github.com/EmYiQing/ShiroEnv

运行`Exp.java`即可注入，访问`http://127.0.0.1/xxx(任何路径)?cmd=whoami`

最后：感谢[天下大木头](https://github.com/KpLi0rn)师傅的帮助

## 免责申明

**未经授权许可使用`JSPHorse`攻击目标是非法的**

**本程序应仅用于授权的安全测试与研究目的**

