package dao.Provider;

import pojo.Provider;

import java.sql.Connection;
import java.util.List;

public interface ProviderDao {

    //增加供应商
    public int add(Connection connection, Provider provider) throws Exception;

    //根据名字和账号查询供应商列表
    public List<Provider> getProviderList(Connection connection, String proName, String proCode) throws Exception;

    //删除供应商
    public int delProviderById(Connection connection, String delId) throws Exception;

    //根据ID获取供应商信息以便修改
    public Provider getProviderById(Connection connection, String id) throws Exception;

    //修改供应商信息
    public int modify(Connection connection, Provider provider) throws Exception;
}
