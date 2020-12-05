package service.User;

import dao.BaseDao;
import dao.User.UserDao;
import dao.User.UserDaoImpl;
import org.junit.Test;
import pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{

    //业务层都会调用Dao层，所以要引入Dao层
    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;

        try {
            connection = BaseDao.getConnection();
            //通过业务层调用Dao层的方法实现数据库操作，得到用户
            user = userDao.getLoginUser(connection, userCode);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    @Override
    public boolean updatePassword(int id, String passWord) {
        Connection connection = null;
        boolean flag = false;
        //修改密码
        try {
            connection = BaseDao.getConnection();
            //通过业务层调用Dao层的方法实现数据库操作
            if (userDao.updatePassword(connection,id,passWord) > 0 ){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

        //查询记录数 调用Dao层
        @Override
        public int getUserCount(String userName, int userRole) {
            Connection connection = null;
            int count = 0;
            try {
                connection = BaseDao.getConnection();
                count = userDao.getUserCount(connection, userName, userRole);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                BaseDao.closeResource(connection,null,null);
            }
            return count;
        }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }

        return userList;
    }

    @Override
    public boolean add(User user) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int updateRows = userDao.add(connection, user);
            connection.commit();
            if (updateRows > 0){
                flag = true;
                System.out.println("add success!");
            }else {
                System.out.println("add failed!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public boolean deleteUserById(Integer delId) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int delRows = userDao.deleteUserById(connection, delId);
            connection.commit();
            if (delRows > 0){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResource(connection,null,null);
            }
        }
        return flag;
    }

    @Override
    public User getUserById(String id) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getUserById(connection, id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            user = null;
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    @Override
    public boolean modify(User user) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int modifyRows = userDao.modify(connection, user);
            connection.commit();
            if (modifyRows > 0){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                BaseDao.closeResource(connection,null,null);
            }
        }
        return flag;
    }

    @Test
    public void test(){
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount(null, 0);
        System.out.println(userCount);
    }
}
