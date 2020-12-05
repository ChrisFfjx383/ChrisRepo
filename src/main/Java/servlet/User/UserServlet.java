package servlet.User;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import pojo.Role;
import pojo.User;
import service.Role.RoleService;
import service.Role.RoleServiceImpl;
import service.User.UserServiceImpl;
import util.Constants;
import util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//实现Servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("savepwd") && method != null){
            this.updatePwd(req, resp);
        }else if(method.equals("pwdmodify") && method !=null){
            this.pwdmodify(req, resp);
        }else if (method.equals("query") && method !=null){
            this.query(req, resp);
        }else if (method.equals("add" ) && method !=null){
            this.add(req,resp);
        }else if (method.equals("deluser" ) && method !=null){
            this.delUser(req,resp);
        }else if (method.equals("getrolelist" ) && method !=null){
            this.getRoleList(req,resp);
        }else if (method.equals("modify" ) && method !=null){
            this.getUserById(req,resp,"usermodify.jsp");
        }else if (method.equals("modifyexe" ) && method !=null){
            this.modify(req,resp);
        }else if(method != null && method.equals("view")) {
            this.getUserById(req, resp, "userview.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //从Session中获取用户的id，用户的所有信息在注册时已通过LoginServlet存入Session中
        Object o = req.getSession().getAttribute(Constants.User_Session);
        boolean flag = false;
        String newpassword = req.getParameter("newpassword");

        if (o != null && newpassword != null){
            UserServiceImpl UserServiceImpl  = new UserServiceImpl();
            flag = UserServiceImpl.updatePassword(((User)o).getId(), newpassword);
            if (flag){
                req.setAttribute("message","修改密码成功，请使用新密码登录");
                //密码修改成功，移除当前Session
                req.getSession().removeAttribute(Constants.User_Session);
            }else {
                req.setAttribute("message","修改密码失败");
            }
        }else {
            req.setAttribute("message","新密码有问题");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //验证旧密码
    public void pwdmodify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //从Session里面拿用户ID 然后对比旧密码
        Object o = req.getSession().getAttribute(Constants.User_Session);
        String oldpassword = req.getParameter("oldpassword");

        //万能的Map : 结果集
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (o == null){ //Session过期了，Session失效
            resultMap.put("result","sessionerror");
        }else if (StringUtils.isNullOrEmpty(oldpassword)){ //旧密码输入为空
            resultMap.put("result","error");
        }else { //旧密码输入
            String userPassword = ((User) o).getUserPassword();
            if (oldpassword.equals(userPassword)){ //旧密码输入正确，用户输入的旧密码和Session中存储的旧密码比对一致
                resultMap.put("result","true");
            }else { //旧密码输入错误
                resultMap.put("result","false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            //JSONArray 阿里巴巴的Json工具 转换格式 把结果集转换成JSON字符串 传递给前端进行交互
            /*
                resultMap = ["result" , "Sessionerror" , "result" , "error"]
                JSON格式 = {key : value}
             */
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //重点、难点 查找用户及角色
    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        //查询用户列表
        //从前端获取来页面所需要的数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;  //为了不为空，默认赋为0 可以进入下面条件判断
        // 等于0时为请选择的选项 是默认值，选择用户角色之后所传参数则大于0，可以分辨

        //获取用户列表(需要进行下面的分页操作)
        UserServiceImpl userService = new UserServiceImpl();
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<User> userList = null;
        List<Role> roleList = null;

        //分页操作开始
        int pageSize = 5;
        int currentPageNo = 1;

        //判断请求是否需要执行
        if (queryUserName == null){
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }
        if (pageIndex != null){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //获取用户总数
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount(); //最大页数 总共有几页

        //控制首页和尾页 当页码小于1时，当前页面显示首页，尾页同理 分页操作结束
        if (currentPageNo < 1){
            currentPageNo = 1;
        }else if (currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }

        //获取用户列表展示
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList",userList);
        roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //增加新用户
    public void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.parseInt(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.parseInt(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.User_Session)).getId());

        UserServiceImpl userService = new UserServiceImpl();
        if (userService.add(user)){
            resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
        }else {
            req.getRequestDispatcher("useradd.jsp").forward(req,resp);
        }
    }

    //给增加的新用户选定角色
    public void getRoleList (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        List<Role> roleList= null;
        RoleServiceImpl roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();

        //把roleList转换成json对象传递给前端
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    //根据用户ID删除用户
    public void delUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String id = req.getParameter("uid");
        Integer delId = 0;
        try {
            delId = Integer.parseInt(id);
        } catch (Exception e){
            delId = 0;
        }
        HashMap<String,String> resultMap = new HashMap<String, String>();
        if (delId <= 0){
            resultMap.put("delResult","notexist");
        } else {
            UserServiceImpl userService = new UserServiceImpl();
            boolean b = userService.deleteUserById(delId);
            if (b){
                resultMap.put("delResult","true");
            }else {
                resultMap.put("delResult","false");
            }
        }

        //把resultMap转换成json对象输出给前端js
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    //修改用户时，先查找到用户已有的信息
    public void getUserById(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException{
        String id = req.getParameter("uid");
        if (!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
            UserServiceImpl userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            req.setAttribute("user",user);
            req.getRequestDispatcher(url).forward(req,resp);
        }
    }

    //修改用户中信息
    public void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String id = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setId(Integer.parseInt(id));
        user.setUserName(userName);
        user.setGender(Integer.parseInt(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.parseInt(userRole));
        user.setModifyBy(((User)req.getSession().getAttribute(Constants.User_Session)).getId());
        user.setModifyDate(new Date());

        UserServiceImpl userService = new UserServiceImpl();
        boolean m = userService.modify(user);
        if (m){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else {
            req.getRequestDispatcher("usermodify.jsp").forward(req,resp);
        }
    }
}
