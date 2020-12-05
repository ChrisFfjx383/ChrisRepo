<html>
<body>
<h2>Hello World!</h2>
    登录功能实现：
   1.配置maven(pom.xml)、web.xml、Tomcat、前端页面、dao数据层、filter过滤器层及过滤器、pojo实体类层及数据库对应的实体类、service层、
servlet层、util层、 db.resources文件、基础数据库连接类BaseDao（包含数据库连接对象、查询及增删改方法）
   2.在web.xml中设置首页欢迎页
   3.编写dao层中登录所用的用户登录接口
   4.编写用户登录接口的实现类
   5.编写业务层接口
   6.编写业务层实现类
   7.编写Servlet层
   8.注册Servlet
   9.测试访问

   逻辑顺序：
   前端login.jsp启用login.do，即到web.xml中找Servlet路径来调Servlet;
   Servlet处理代码，查询，调用业务层service层中UserServiceImpl中的Login方法；
   Login方法中再调用数据库层dao层中UserDaoImpl中的getLoginUser方法,查出数据库中的数据

    登录功能优化：
    10.注销Servlet编写
    11.注册Servlet
    12.测试注销
    13.登录拦截优化过滤器编写 （退出账户后应该不能访问主页）
    14.注册过滤器

    密码修改：
    15.从底层开始 补充UserDao接口 然后补充UserDao实现类
    16.根据Dao层修改UserService接口和UserService实现类
    17.补充Servlet层，完成Servlet注册和Servlet复用 实现复用需要提取出方法 实现密码修改功能
    18.测试

    密码修改优化：
    19.使用Ajax优化密码修改（旧密码验证部分）补充pwdmodify方法 用HashMap储存判断数据 用JSONArray将resultMap转换为Json数据和前台交互

    用户管理实现：
    20.导入分页布局页面（rollpage.jsp） 用户列表页面（userlist.jsp） 分页工具类（PageSupport.java）
    21.获取用户数量 从底层开始 UserDao层： 增加一个方法，根据用户名或角色查询用户总数，参数根据sql语句来决定
                            UserDaoImpl实现类： 实现这个操作（Connection，rs，pstat，sql，params）
                            UserService层： 增加一个方法，调用Dao层，查询用户记录
                            UserServiceImpl实现类：实现这个操作（Connection）
    22.获取用户列表  从底层开始 UserDao层： 增加一个方法,获取用户列表 通过条件查询得出UserList
                             UserDaoImpl实现类： 实现这个操作（Connection，rs，pstat，sql，params）
                             UserService层： 增加一个方法，调用Dao层，查询用户列表
                             UserServiceImpl实现类：实现这个操作（Connection）
    23.获取角色列表  从底层开始 为了职责统一，把角色的操作单独放在一个包中，和pojo类一一对应，其他如上所示
    24.用户显示的Servlet(难点) 1.获取用户前端的数据（查询） 即从前端获取要查询的数据
                             2.看参数的值 判断请求是否需要执行
                             3.实现分页，控制页面大小等
                             4.用户列表展示，和前端请求相结合，记得实现Servlet复用
                             5.返回前端
    25.增加用户     编写从底层开始，逻辑顺序从前端表单提交开始 整体架构如下 大部分功能都由此逻辑实现：
                    1.前端页面通过表单提交请求/jsp/user.do?method=add 调用Servlet层
                    2.Servlet层通过  获取前端参数并封装成对象(占90%代码) 实现逻辑上的操作(如增加用户)，之后编写跳转页面，
                      最后通过userService.add(user) 调用Service层
                    3.Service层连接数据库并处理事务(增删改时操作可能失败，需要编写回滚以进行事务管理)，然后通过
                      userDao.add(connection,user)调用Dao层
                    4.Dao层执行sql语句并准备除connection之外的参数，之后通过BaseDao.execute(connection,pstat,rs,sql,params)
                      执行sql语句，即用JDBC帮我们操作数据库
</body>
</html>
<span>${user.age}</span>57
<span>${user.userRoleName}</span>63