package service.User;

import pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public interface UserService {
    //用户登录
    public User login(String userCode, String password);
    //修改用户密码
    public boolean updatePassword(int id, String passWord);
    //查询记录数
    public int getUserCount(String userName, int userRole);
    //根据条件查询用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);
    //增加新用户
    public boolean add(User user);
    //根据用户ID删除用户
    public boolean deleteUserById(Integer delId);
    //根据用户ID查找用户已有信息以便于修改
    public User getUserById(String id);
    //修改用户信息
    public boolean modify(User user);
}
