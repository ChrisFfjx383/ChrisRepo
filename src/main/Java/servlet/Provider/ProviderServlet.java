package servlet.Provider;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import pojo.Provider;
import pojo.User;
import service.Provider.ProviderServiceImpl;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method != null && method.equals("query")){
            this.query(req,resp);
        }else if(method != null && method.equals("add")){
            this.add(req,resp);
        }else if(method != null && method.equals("view")){
            this.getProviderById(req,resp,"providerview.jsp");
        }else if(method != null && method.equals("modify")){
            this.getProviderById(req,resp,"providermodify.jsp");
        }else if(method != null && method.equals("modifysave")){
            this.modify(req,resp);
        }else if(method != null && method.equals("delprovider")){
            this.delProvider(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void delProvider(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        String id = req.getParameter("proid");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(id)){
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            int flag = providerService.deleteProviderById(id);
            if (flag == 0){ // 删除成功
                resultMap.put("delResult","true");
            }else if (flag == -1){ // 删除失败
                resultMap.put("delResult","false");
            }else if (flag > 0){ //该供应商还有订单 无法删除
                resultMap.put("delResult", String.valueOf(flag));
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

    private void modify(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        String id = req.getParameter("id");

        Provider provider = new Provider();
        provider.setId(Integer.parseInt(id));
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setModifyBy(((User)req.getSession().getAttribute(Constants.User_Session)).getId());
        provider.setModifyDate(new Date());

        ProviderServiceImpl providerService = new ProviderServiceImpl();
        boolean b= providerService.modify(provider);
        if (b){
            resp.setContentType(req.getContextPath() + "/jsp/provider.do?method=query");
        }else {
            req.getRequestDispatcher("providermodify.jsp").forward(req,resp);
        }
    }

    private void getProviderById(HttpServletRequest req, HttpServletResponse resp,String url)
            throws ServletException, IOException{
        String id = req.getParameter("proid");
        if (!StringUtils.isNullOrEmpty(id)){
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            Provider provider = providerService.getProviderById(id);
            req.setAttribute("provider",provider);
            req.getRequestDispatcher(url).forward(req,resp);
        }

    }

    private void add(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User)req.getSession().getAttribute(Constants.User_Session)).getId());
        provider.setCreationDate(new Date());

        ProviderServiceImpl providerService = new ProviderServiceImpl();
        boolean a= providerService.add(provider);
        if (a){
            resp.sendRedirect(req.getContextPath() + "/jsp/provider.do?method=query");
        }else {
            req.getRequestDispatcher("provideradd.jsp").forward(req,resp);
        }
    }

    private void query(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException{
        String queryProName = req.getParameter("queryProName");
        String queryProCode = req.getParameter("queryProCode");
        if(StringUtils.isNullOrEmpty(queryProName)){
            queryProName = "";
        }
        if(StringUtils.isNullOrEmpty(queryProCode)){
            queryProCode = "";
        }

        List<Provider> providerList = new ArrayList<Provider>();
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        providerList = providerService.getProviderList(queryProName, queryProCode);
        req.setAttribute("providerList",providerList);
        req.setAttribute("queryProName",queryProName);
        req.setAttribute("queryProCode",queryProCode);
        req.getRequestDispatcher("providerlist.jsp").forward(req,resp);
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
