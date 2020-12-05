package dao.Provider;

import com.mysql.cj.util.StringUtils;
import dao.BaseDao;
import pojo.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProviderDaoImpl implements ProviderDao{
    @Override
    public int add(Connection connection, Provider provider) throws Exception {
        PreparedStatement pstat = null;
        int updateRows = 0;
        if (connection != null){
            String sql = "insert into smbms.smbms_provider(proCode,proName,proDesc,proContact,proPhone,proAddress,proFax,createdBy,creationDate) values(?,?,?,?,?,?,?,?,?)";
            Object[] params = {provider.getProCode(),provider.getProName(),provider.getProDesc(),provider.getProContact(),provider.getProPhone(),provider.getProAddress(),provider.getProFax(),provider.getCreatedBy(),provider.getCreationDate()};
            updateRows = BaseDao.execute(connection, pstat, sql, params);
            BaseDao.closeResource(null,pstat,null);
        }
        return updateRows;

    }

    @Override
    public List<Provider> getProviderList(Connection connection, String proName, String proCode) throws Exception {
        PreparedStatement pstat = null;
        List<Provider> providerList = new ArrayList<Provider>();
        ResultSet rs = null;
        if (connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select * from smbms.smbms_provider where 1=1");
            List<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(proName)){
                sql.append("and proName like ?");
                list.add("%"+proName+"%");
            }
            if (!StringUtils.isNullOrEmpty(proCode)){
                sql.append(" and proCode like ?");
                list.add("%"+proCode+"%");
            }
            Object[] params = list.toArray();
            rs = BaseDao.execute(connection, pstat, rs, sql.toString(), params);

            while (rs.next()){
                Provider _provider = new Provider();
                _provider.setId(rs.getInt("id"));
                _provider.setProCode(rs.getString("proCode"));
                _provider.setProName(rs.getString("proName"));
                _provider.setProDesc(rs.getString("proDesc"));
                _provider.setProContact(rs.getString("proContact"));
                _provider.setProPhone(rs.getString("proPhone"));
                _provider.setProAddress(rs.getString("proAddress"));
                _provider.setProFax(rs.getString("proFax"));
                _provider.setCreationDate(rs.getTimestamp("creationDate"));
                providerList.add(_provider);
            }
            BaseDao.closeResource(null,pstat,rs);
        }
        return providerList;
    }

    @Override
    public int delProviderById(Connection connection, String delId) throws Exception {
        PreparedStatement pstat = null;
        int updateRows = 0;
        if (connection != null){
            String sql = "delete from smbms.smbms_provider where id = ?";
            Object[] params = {delId};
            updateRows = BaseDao.execute(connection, pstat, sql, params);
            BaseDao.closeResource(null,pstat,null);
        }
        return updateRows;
    }

    @Override
    public Provider getProviderById(Connection connection, String id) throws Exception {
        PreparedStatement pstat = null;
        ResultSet rs = null;
        Provider provider = null;
        if (connection != null){
            String sql = "select * from smbms.smbms_provider where id=?";
            Object[] params = {id};
            rs = BaseDao.execute(connection, pstat, rs, sql, params);
            if(rs.next()){
                provider = new Provider();
                provider.setId(rs.getInt("id"));
                provider.setProCode(rs.getString("proCode"));
                provider.setProName(rs.getString("proName"));
                provider.setProDesc(rs.getString("proDesc"));
                provider.setProContact(rs.getString("proContact"));
                provider.setProPhone(rs.getString("proPhone"));
                provider.setProAddress(rs.getString("proAddress"));
                provider.setProFax(rs.getString("proFax"));
                provider.setCreatedBy(rs.getInt("createdBy"));
                provider.setCreationDate(rs.getTimestamp("creationDate"));
                provider.setModifyBy(rs.getInt("modifyBy"));
                provider.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null, pstat, rs);
        }
        return provider;
    }

    @Override
    public int modify(Connection connection, Provider provider) throws Exception {
        PreparedStatement pstat = null;
        int updateRows = 0;
        if (connection != null){
            String sql = "update smbms.smbms_provider set proName=?,proDesc=?,proContact=?,proPhone=?,proAddress=?,proFax=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {provider.getProName(),provider.getProDesc(),provider.getProContact(),provider.getProPhone(),provider.getProAddress(),
                    provider.getProFax(),provider.getModifyBy(),provider.getModifyDate(),provider.getId()};
            updateRows = BaseDao.execute(connection, pstat, sql, params);
            BaseDao.closeResource(null,pstat,null);
        }
        return updateRows;
    }
}
