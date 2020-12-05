package service.Bill;

import pojo.Bill;

import java.util.List;

public interface BillService {

    //增加订单
    public boolean add(Bill bill);

    //查询订单列表
    public List<Bill> getBillList(Bill bill);

    //删除订单
    public boolean delBillById(String delId);

    //根据订单id查询订单信息以便修改
    public Bill getBillById(String id);

    //修改订单信息
    public boolean modify(Bill bill);
}
