package cn.com.flaginfo.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import cn.com.flaginfo.ApiClient;
import cn.com.flaginfo.utils.SystemConfig;
import cn.com.flaginfo.commons.utils.ssouser.pojo.SpInfo;
import cn.com.flaginfo.commons.utils.ssouser.pojo.SpRole;
import cn.com.flaginfo.commons.utils.ssouser.pojo.UserInfo;
import cn.com.flaginfo.umsapp.oauth.OAuthUtils;
import cn.com.flaginfo.user.auth.common.JsonHelper;
import cn.com.flaginfo.utils.SsoUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AppSSOFilter implements Filter {
	
	private Logger log = Logger.getLogger(AppSSOFilter.class);
	private RestTemplate restTemplate;
	private static final String USER_LOGIN_KEY = "user_login_key_info";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
	/**
	 * 用户登录过滤，如果获取到用户信息，则放入session{_sso_user=SsoUser},有无获取到用户信息流程都会继续进行
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("------------------------");
		System.out.println("This is AppSSoFilter!!!");
		System.out.println("------------------------");
		HttpServletRequest req = (HttpServletRequest)request;
		checkSession(req);
		HttpSession session = req.getSession();
		SsoUser ssoUser = (SsoUser)(session==null?null:session.getAttribute("_sso_user"));
		if(ssoUser == null){//可能是第一次进入
			String userJson = null;
			try{
				
				userJson = OAuthUtils.getOAuthInfo(req);//getOAuthInfo();//
				//userJson = getOAuthInfo();//getOAuthInfo();//
				if(log.isInfoEnabled()){
					System.out.println("//==================用户信息============================//");
					System.out.println("*");
					System.out.println("*");
					System.out.println("*");
					System.out.println("*");
					System.out.println("*");
					System.out.println("*");
					System.out.println(userJson);
					System.out.println("*");
					System.out.println("*");
					System.out.println("*");
					System.out.println("*");
					System.out.println("*");
					System.out.println("*");
					System.out.println("*");
					System.out.println("//==================用户信息============================//");
					log.info("oauth result:"+userJson);
				}
			}catch(Exception e){
				log.error(e.getMessage(), e);
			}
			if(userJson != null){
				try{
					Map<String,Object> userMap = (Map<String,Object>)JSONObject.toBean(JSONObject.fromObject(userJson), HashMap.class);
					String userPhone = (String)userMap.get("mobile");
				    ssoUser = new SsoUser();
					
					SpInfo sp = new SpInfo();
					JSONObject corp = JSONObject.fromObject(userMap.get("corp"));
					sp.setId(corp.optString("spId"));
					sp.setSpName(corp.optString("name"));
					ssoUser.setLoginSpInfo(sp);
					//获取用户信息
					log.info("Call getUserInfo and param is : userPhone="+userPhone+",spId="+sp.getId());
					UserInfo userInfo = getUserInfo(userPhone, sp);
					if(userInfo!=null){
						log.info("Call getUserInfo and return : "+userInfo.getId());
					}
					ssoUser.setUserInfo(userInfo);

					// 获取ContactId
					String contactId = getContactId(ssoUser.getLoginSpInfo().getId());
					SpRole curRole = new SpRole();
					curRole.setContactsId(contactId);
					ssoUser.setCurRole(curRole);
					
					session = req.getSession(true);
					session.setAttribute("_sso_user", ssoUser);
					session.setAttribute("_user_map", userMap);
					session.setAttribute(USER_LOGIN_KEY, getUserLoginKey(req));
				}catch(Exception e){
					log.error(e.getMessage(), e);
				}
			}else{
				log.error("OAuth error,no user information found!");
			}
			
		}
		chain.doFilter(request, response);
	}
	
	
	/**
	 * 获取用户contactsId
	 * 
	 * @param spId
	 * @return
	 */
	public String getContactId(String spId) {
		log.error("Begin call getContactId.");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("spId", spId);
		String apiKey = SystemConfig.getString("api.user.key");
		String apiVersion = SystemConfig.getString("api.user.version");
		String contactsId = "";
		String reqParamCon = JsonHelper.parseToJson(param);

		log.error("Call contacts_conf_list and the params is : apiKey=" + apiKey + ",apiVersion=" + apiVersion
				+ ",reqParamCon=" + reqParamCon);
		// 获取通讯录id
		String resConId = new ApiClient("contacts_conf_list", apiKey, apiVersion).addHeadPara("operator", "")
				.setJsonBody(reqParamCon).postAsJson();
		log.error("Call contacts_conf_list and return : " + resConId);
		JSONObject resultCon = JSONObject.fromObject(resConId);
		if (resultCon.optInt("returnCode") == 200) {
			JSONArray jsonArrayCon = resultCon.optJSONArray("list");
			if (jsonArrayCon != null && jsonArrayCon.size() > 0) {
				for (int i = 0; i < jsonArrayCon.size(); i++) {
					JSONObject con = jsonArrayCon.getJSONObject(i);
					if ("3".equals(con.optString("type"))) {
						contactsId = con.optString("contactsId");
					}
				}
			}
		}
		log.error("Call getContactId end, return : " + contactsId);
		return contactsId;
	}
	
	
	public JSONObject post(String url, Map<String,Object> params, Map<String,String> headers) {
		HttpClient client = new HttpClient();
		HttpClientParams cparams = client.getParams();
		cparams.setConnectionManagerTimeout(10000);
		cparams.setContentCharset("utf-8");
		cparams.setSoTimeout(10000);
		cparams.setHttpElementCharset("utf-8");
		PostMethod post = new PostMethod(url);
		if(headers != null){
			Iterator<Map.Entry<String, String>> itr = headers.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<String, String> ent = itr.next();
				post.addRequestHeader(ent.getKey(), ent.getValue());
			}
		}
		if(params != null){
			Iterator<Map.Entry<String, Object>> itr = params.entrySet().iterator();
			NameValuePair[] n_v = new NameValuePair[params.size()];
			int i = 0;
			while(itr.hasNext()){
				Entry<String, Object> ent = itr.next();
				n_v[i++] = new NameValuePair(ent.getKey(), (ent.getValue()==null?"":ent.getValue().toString()));
			}
			post.setRequestBody(n_v);
		}
		try {
			int code = client.executeMethod(post);
			if(code == 200){
				return JSONObject.fromObject(new String(post.getResponseBody(),"utf-8"));
			}
		} catch (HttpException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}finally{
			post.releaseConnection();
		}
		return null;
	}
	
	@Override
	public void destroy() {}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	//用来处理客户端切换登录用户后，session还是原来的session的问题
		private void checkSession(HttpServletRequest request){		
			//登录参数
			String userLoginKey = getUserLoginKey(request);
		    if(userLoginKey != null){
		    	HttpSession session = request.getSession();
		    	if(session != null){
		    		String loginKey = (String)session.getAttribute(USER_LOGIN_KEY);
		    		log.info("The loginKey is "+loginKey+" and userLoginKey is "+userLoginKey);
		    		if(loginKey != null && !userLoginKey.equals(loginKey)){
		    			log.info("Invalidate user......................................");
		    			session.removeAttribute("_sso_user");
		    			session.invalidate();
		    		}
		    	}
		    }
		}
		
		private String getUserLoginKey(HttpServletRequest request){
			log.info("Call getUserLoginKey begin.");
			String key = request.getParameter("__mId");
		    String host = request.getParameter("__h");
		    log.info("The __mId is "+key+",host is "+host);
		    if(key != null || host != null){
		    	StringBuilder strBuf = new StringBuilder();	  
		    	strBuf.append("__mId:");
		    	strBuf.append(key);
		    	strBuf.append(";");
		    	strBuf.append("__h");
		    	strBuf.append(host);
		    	log.info("The result is "+strBuf);
		    	return strBuf.toString();
		    }
		    log.info("The result is "+null);
		    log.info("Call getUserLoginKey end.");
		    return null;
		}
		
		private UserInfo getUserInfo(String phone,SpInfo sp){
			log.info("Begin call getUserInfo method, the params is :phone="+phone+",spId="+sp.getId());
			if(phone == null){
				return null;
			}
			UserInfo userInfo = null;
			JSONArray userArray = new JSONArray();
			userArray = getSpUserList(sp.getId());
		
			if(userArray.size() > 0){
				int size = userArray.size();
				for(int i=0;i<size;i++){
					JSONObject user = userArray.getJSONObject(i);
					if(phone.equals(user.optString("personalPhoneNo").trim())||phone.equals(user.optString("mobile").trim())){
						userInfo = new UserInfo();
						userInfo.setId(user.optString("id"));
						userInfo.setName(user.optString("name"));
						userInfo.setPersonalPhoneNo(phone);
						break;
					}
				}
			}else{
				log.error("no user found for sp["+sp.getId()+"]");
			}
			log.info("End call getUserInfo method, return : userInfo="+userInfo);
			return userInfo;
		}
		private JSONArray getSpUserList(String spId){
			String apiKey = SystemConfig.getString("api.user.key");
			String apiVersion = SystemConfig.getString("api.user.version");
			JSONObject groupParam = new JSONObject();
			groupParam.put("spId", spId);
			String reqGroupParam = groupParam.toString();
			String resGroup = "{\"returnCode\":\"500\"}";
			try{
				log.info("Call get_user_list and params is : apiKey="+apiKey+",apiVersion="+apiVersion+",reqGroupParam="+reqGroupParam);
				resGroup = new ApiClient("get_user_list", apiKey, apiVersion)
							.addHeadPara("operator", "").setJsonBody(reqGroupParam).postAsJson();
			}catch(Exception e){
				log.error(e.getMessage());
			}
			JSONObject result = JSONObject.fromObject(resGroup);
			log.info("Call get_user_list and return : "+result);
			if (result.optInt("returnCode") == 200) {
				return result.getJSONArray("list");
			}else{
				return new JSONArray();
			}
			
		}
		
		private String getOAuthInfo(){
			String info = "{\"name\": \"张智\",\"gender\": \"M\",\"memberId\": \"571877828e82410345d53d18\","
					+"\"corp\": {\"icon\": \"http://echat-sit.oss-cn-hangzhou.aliyuncs.com/echat_sit/corp-icon/570621708e8241277c6c9fb6\","
                     +"\"name\": \"测试开户计费jason\","
                     + "\"spId\": \"16011414335210020708\","
                     + "\"shortName\": \"测试开户计费jason\","
                     +"\"note\": \"16011414335210020708\","
                      +"\"corpId\": \"570621708e8241277c6c9fb6\"},"
                     + "\"avatar\": \"http://echat-sit.oss-cn-hangzhou.aliyuncs.com/echat_sit/avatar/571877828e82410345d53d18?_d=1461229889000\","
                      +"\"success\": true,\"ext\": [],\"errcode\": 0,\"mobile\": \"15882298560\"}";
			return info;
		}
}
