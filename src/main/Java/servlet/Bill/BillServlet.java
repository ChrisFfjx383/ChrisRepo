package servlet.Bill;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import pojo.Bill;
import pojo.Provider;
import pojo.User;
import service.Bill.BillServiceImpl;
import service.Provider.ProviderServiceImpl;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method != null && method.equals("query")){
            this.query(req,resp);
        }else if(method != null && method.equals("add")){
            this.add(req,resp);
        }else if(method != null && method.equals("view")){
            this.getBillById(req,resp,"billview.jsp");
        }else if(method != null && method.equals("modify")){
            this.getBillById(req,resp,"billmodify.jsp");
        }else if(method != null && method.equals("modifysave")){
            this.modify(req,resp);
        }else if(method != null && method.equals("delbill")){
            this.delBill(req,resp);
        }else if(method != null && method.equals("getproviderlist")){
            this.getProviderlist(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void getProviderlist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        List<Provider> providerList = new ArrayList<Provider>();
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList("", "");
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(providerList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private void getBillById(HttpServletRequest req, HttpServletResponse resp,String url)
            throws ServletException, IOException{
        String id = req.getParameter("billid");
        if (!StringUtils.isNullOrEmpty(id)){
            BillServiceImpl billService = new BillServiceImpl();
            Bill bill = billService.getBillById(id);
            req.setAttribute("bill",bill);
            req.getRequestDispatcher(url).forward(req,resp);
        }
    }

    private void modify(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        String id = req.getParameter("id");
        String productName = req.getParameter("productName");
        String productDesc = req.getParameter("productDesc");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setId(Integer.valueOf(id));
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));

        bill.setModifyBy(((User)req.getSession().getAttribute(Constants.User_Session)).getId());
        bill.setModifyDate(new Date());

        BillServiceImpl billService = new BillServiceImpl();
        boolean b = billService.modify(bill);
        if (b){
            resp.sendRedirect(req.getContextPath() + "/jsp/bill.do?method=query");
        }else {
            req.getRequestDispatcher("billmodify.jsp").forward(req,resp);
        }
    }

    private void delBill(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        String id = req.getParameter("billid");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(id)){
            BillServiceImpl billService = new BillServiceImpl();
            boolean b = billService.delBillById(id);
            if (b){ //删除成功
                resultMap.put("delResult","true");
            }else { //删除失败
                resultMap.put("delResult","false");
            }
        }else {
            resultMap.put("delResult","notexist");
        }
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private void add(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productDesc = req.getParameter("productDesc");
        String productUnit = req.getParameter("productUnit");

        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setCreatedBy(((User)req.getSession().getAttribute(Constants.User_Session)).getId());
        bill.setCreationDate(new Date());

        BillServiceImpl billService = new BillServiceImpl();
        if (billService.add(bill)){
            resp.sendRedirect(req.getContextPath() + "/jsp/bill.do?method=query");
        }else {
            req.getRequestDispatcher("billadd.jsp").forward(req,resp);
        }
    }

    private void query(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{

        List<Provider> providerList = new ArrayList<Provider>();
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList("", "");
        req.setAttribute("providerList",providerList);

        String queryProductName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        if (StringUtils.isNullOrEmpty(queryProductName)){
            queryProductName = "";
        }

        List<Bill> billList = new ArrayList<Bill>();
        BillServiceImpl billService = new BillServiceImpl();
        Bill bill = new Bill();
        if (StringUtils.isNullOrEmpty(queryIsPayment)){
            bill.setIsPayment(0);
        }else {
            bill.setIsPayment(Integer.valueOf(queryIsPayment));
        }
        if (StringUtils.isNullOrEmpty(queryProviderId)){
            bill.setProviderId(0);
        }else {
            bill.setProviderId(Integer.valueOf(queryProviderId));
        }
        bill.setProductName(queryProductName);
        billList = billService.getBillList(bill);
        req.setAttribute("billList",billList);
        req.setAttribute("queryProductName",queryProductName);
        req.setAttribute("queryProviderId",queryProviderId);
        req.setAttribute("queryIsPayment",queryIsPayment);
        req.getRequestDispatcher("billlist.jsp").forward(req,resp);
    }
    public static void main(String[] args) {
        System.out.println(new BigDecimal("23.235").setScale(2,BigDecimal.ROUND_HALF_DOWN));
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }

}