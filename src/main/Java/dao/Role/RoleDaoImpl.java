package dao.Role;

import dao.BaseDao;
import dao.Role.RoleDao;
import pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    //获取角色列表
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement pstat = null;
        ResultSet rs = null;
        ArrayList<Role> roleList = new ArrayList<Role>();

        if (connection != null){
            String sql = "select * from smbms.smbms_role";
            Object[] params = {};
            rs = BaseDao.execute(connection, pstat, rs, sql, params);

            while (rs.next()){
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                roleList.add(role);
            }
            BaseDao.closeResource(null,pstat,rs);
        }
        return roleList;
    }
}
