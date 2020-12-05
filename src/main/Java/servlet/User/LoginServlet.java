package servlet.User;


import pojo.User;
import service.User.UserServiceImpl;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    //Servlet:控制层，调用业务层的代码
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        //和数据库中的账号密码对比，调用业务层
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword); //这里已经把登录的那个人User 查出来了

        //&& user.getUserPassword().equals(userPassword)
        if (user != null && user.getUserPassword().equals(userPassword)){  //查有此人，可以登录 且判断密码是否正确
            //将用户的信息存入Session中
            req.getSession().setAttribute(Constants.User_Session,user);
            //登陆成功，则跳转到内部主页
            resp.sendRedirect("jsp/frame.jsp");
        }else { //查无此人，无法登录
            //转发回登录页面，顺便提示用户名或密码错误
            req.setAttribute("error","用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }


    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
