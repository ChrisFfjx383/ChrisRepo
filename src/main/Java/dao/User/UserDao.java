package dao.User;

import pojo.Role;
import pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//dao层用户登录的接口
public interface UserDao {

    //得到要登录的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    //修改用户密码
    public int updatePassword(Connection connection, int id, String passWord) throws SQLException;

    //根据用户名或角色查询用户总数
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException;

    //获取用户列表 通过条件查询得出UserList
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;

    //增加用户
    public int add(Connection connection, User user) throws SQLException;

    //根据ID删除用户
    public int deleteUserById(Connection connection, Integer delId) throws SQLException;

    //根据ID获取用户信息以便修改
    public User getUserById(Connection connection, String id) throws SQLException;

    //修改用户信息
    public int modify(Connection connection,User user) throws SQLException;
}
