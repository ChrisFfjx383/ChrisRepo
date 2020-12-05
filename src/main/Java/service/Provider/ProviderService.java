package service.Provider;

import dao.Provider.ProviderDao;
import pojo.Provider;

import java.util.List;

public interface ProviderService {
    //增加新供应商
    public boolean add(Provider provider);

    //根据条件查询供应商列表
    public List<Provider> getProviderList(String proName,String proCode);

    /**
     * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
     * 若订单表中无该供应商的订单数据，则可以删除
     * 若有该供应商的订单数据，则不可以删除
     * 返回值billCount
     * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
     * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
     *
     * ---判断
     * 如果billCount = -1 失败
     * 若billCount >= 0 成功
     */
    //删除供应商
    public int deleteProviderById(String delId);

    //根据供应商id查找供应商信息以便修改
    public Provider getProviderById(String id);

    //修改供应商信息
    public boolean modify(Provider provider);
}
