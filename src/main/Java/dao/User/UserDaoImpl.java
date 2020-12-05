package dao.User;

import com.mysql.cj.util.StringUtils;
import dao.BaseDao;
import pojo.Role;
import pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//dao接口的实现类

    //得到要登录的用户
public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pstat = null;
        User user = null;
        if (connection != null){
            String sql = "select * from smbms_user where userCode = ? ";
            Object[] params = {userCode};
                rs = BaseDao.execute(connection, pstat, rs, sql, params);
                user = new User();
                if (rs.next()){
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("Gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getDate("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getDate("modifyDate"));
                }
                BaseDao.closeResource(connection,pstat,rs);
        }
        return user;
    }

    //修改用户密码
    @Override
    public int updatePassword(Connection connection, int id, String passWord) throws SQLException {

        PreparedStatement pstat = null;
        int execute = 0;
        if (connection != null){
            String sql = "update smbms.smbms_user set userPassword = ? where id = ?";
            Object params[] = {passWord,id};
            execute = BaseDao.execute(connection, pstat, sql, params);
            BaseDao.closeResource(null,pstat,null);
        }
        return execute;
    }

        //根据用户名或角色查询用户总数
        @Override
        public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
            PreparedStatement pstat = null;
            ResultSet rs = null;
            int count = 0;
            if (connection != null){
                StringBuffer sql = new StringBuffer();
                sql.append("select count(1) as count from smbms.smbms_user u, smbms.smbms_role r where u.userRole = r.id");
                ArrayList<Object> list = new ArrayList<Object>(); //用List存放sql语句查询的参数

                if (!StringUtils.isNullOrEmpty(userName)){
                    sql.append(" and u.userName like ?");
                    list.add("%"+userName+"%"); //index:0
                }
                if (userRole >0){
                    sql.append(" and u.userRole = ?");
                    list.add(userRole); //index:1
                }
                //把list转化为数组
                Object[] params = list.toArray();
                rs = BaseDao.execute(connection, pstat, rs, sql.toString(), params);
                if (rs.next()){
                   count = rs.getInt("count");
                }
            }
            BaseDao.closeResource(null,pstat,rs);
            return count;
        }

        //获取用户列表 通过条件查询UserList
        @Override
        public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
            PreparedStatement pstat = null;
            ResultSet rs = null;
            List<User> userList= new ArrayList<User>();
            if (connection != null){
                StringBuffer sql = new StringBuffer();
                sql.append("select u.*, r.roleName as userRoleName from smbms.smbms_user u, smbms.smbms_role r where u.userRole = r.id");
                List<Object> list = new ArrayList<Object>();
                if (!StringUtils.isNullOrEmpty(userName)){
                    sql.append(" and u.userName like ?");
                    list.add("%"+userName+"%");
                }
                if (userRole >0){
                    sql.append(" and u.userRole = ?");
                    list.add(userRole);
                }

                //在数据库中，分页使用limit 有两个值：startIndex, pageSize
                //当前页startIndex = （当前页-1）*pageSize
                //0,5   01234
                //5,5   56789
                //10,5  10 11 12 13 14
                sql.append(" order by creationDate DESC limit ?,?");
                currentPageNo = (currentPageNo - 1) * pageSize;
                list.add(currentPageNo);
                list.add(pageSize);

                //把list转化为数组
                Object[] params = list.toArray();
                rs = BaseDao.execute(connection, pstat, rs, sql.toString(), params);
                //把查出的用户信息存入userList并返回
                while (rs.next()){
                    User _user = new User();
                    _user.setId(rs.getInt("id"));
                    _user.setUserCode(rs.getString("userCode"));
                    _user.setUserName(rs.getString("userName"));
                    _user.setUserPassword(rs.getString("userPassword"));
                    _user.setGender(rs.getInt("Gender"));
                    _user.setBirthday(rs.getDate("birthday"));
                    _user.setPhone(rs.getString("phone"));
                    _user.setUserRole(rs.getInt("userRole"));
                    _user.setUserRoleName(rs.getString("userRoleName"));
                    userList.add(_user);
                }
                BaseDao.closeResource(null,pstat,rs);
            }
                return userList;
        }

        //增加用户

        @Override
        public int add(Connection connection, User user) throws SQLException {
        PreparedStatement pstat = null;
        int updateRows = 0;
        if (connection != null){
            String sql = "insert into smbms.smbms_user(userCode, userName, userPassword, userRole, gender, birthday, phone, address, creationDate createdBy) values\n" +
                    "(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),user.getUserRole(),user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress(),user.getCreationDate(),user.getCreatedBy()};
            updateRows = BaseDao.execute(connection, pstat, sql, params);
            BaseDao.closeResource(null,pstat,null);
        }
            return updateRows;
        }

        @Override
        public int deleteUserById(Connection connection, Integer delId) throws SQLException {

        PreparedStatement pstat = null;
        int delRows = 0;
        if (connection != null){
            String sql = "delete from smbms.smbms_user where id = ?";
            Object[] params = {delId};
            delRows = BaseDao.execute(connection, pstat, sql, params);
        }
            return delRows;
        }

        @Override
        public User getUserById(Connection connection, String id) throws SQLException {
        User user = null;
        ResultSet rs = null;
        PreparedStatement pstat = null;
        if (connection != null){
            String sql = "select u.*, r.roleName as userRoleName from smbms.smbms_user u, smbms.smbms_role r where u.id = ? and u.userRole = r.id";
            Object[] params = {id};
            rs = BaseDao.execute(connection, pstat, rs, sql, params);
            if (rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("Gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null,pstat,rs);
        }
            return user;
        }

        @Override
        public int modify(Connection connection, User user) throws SQLException {
            PreparedStatement pstat = null;
            int updaterRows = 0;
            if (connection != null){
                String sql = "update smbms.smbms_user set userName=?,gender =?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ?";
                Object[] params = {user.getUserName(),user.getGender(),user.getBirthday(),
                        user.getPhone(),user.getAddress(),user.getUserRole(),user.getModifyBy(),
                        user.getModifyDate(),user.getId()};
                updaterRows = BaseDao.execute(connection, pstat, sql, params);
            }
            return updaterRows;
        }
    }
