package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//操作数据库的公共类
public class BaseDao {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    //静态代码块，类加载时就初始化了，即类启动的时候就加载 用来读取配置文件
    static {
        Properties properties = new Properties();
        //通过类加载器读取对应的资源 （反射）
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    //获取数据库的连接，得到数据库的连接对象
    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    //编写查询公共方法
    public static ResultSet execute(Connection connection, PreparedStatement pstat, ResultSet rs, String sql, Object[] params) throws SQLException {
        pstat = connection.prepareStatement(sql);
        for (int i = 0; i<params.length;i++){
            pstat.setObject(i+1,params[i]);
        }
        rs = pstat.executeQuery();
        return rs;
    }

    //编写增删改公共方法
    public static int execute(Connection connection, PreparedStatement pstat,String sql, Object[] params) throws SQLException {
        pstat = connection.prepareStatement(sql);
        for (int i = 0; i<params.length;i++){
            pstat.setObject(i+1,params[i]);
        }
        int updateRows = pstat.executeUpdate();
        return updateRows;
    }

    //释放资源
    public static boolean closeResource(Connection connection,PreparedStatement pstat,ResultSet rs){
        boolean flag = true;
        if (connection != null){
            try {
                connection.close();
                connection = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        if (pstat != null){
            try {
                pstat.close();
                pstat = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        if (rs != null){
            try {
                rs.close();
                rs = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
