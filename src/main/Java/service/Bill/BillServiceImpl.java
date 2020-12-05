package service.Bill;

import dao.BaseDao;
import dao.Bill.BillDao;
import dao.Bill.BillDaoImpl;
import pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImpl implements BillService {
    BillDao billDao = null;

    public BillServiceImpl() {
        billDao = new BillDaoImpl();
    }

    @Override
    public boolean add(Bill bill) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false); //开启JDBC事务管理
            if (billDao.add(connection, bill) > 0){
                flag = true;
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public List<Bill> getBillList(Bill bill) {
        Connection connection = null;
        List<Bill> billList = null;
        try {
            connection = BaseDao.getConnection();
            billList = billDao.getBillList(connection, bill);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return billList;
    }

    @Override
    public boolean delBillById(String delId) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if (billDao.deleteBillById(connection,delId) > 0){
                flag = true;
            }
            connection.commit();
        } catch (Exception throwables) {
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
    public Bill getBillById(String id) {
        Connection connection = null;
        Bill bill = null;
        try {
            connection = BaseDao.getConnection();
            bill = billDao.getBillById(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
            bill = null;
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return bill;
    }

    @Override
    public boolean modify(Bill bill) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if (billDao.modify(connection,bill) > 0){
                flag = true;
            }
            connection.commit();
        } catch (Exception throwables) {
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
}
