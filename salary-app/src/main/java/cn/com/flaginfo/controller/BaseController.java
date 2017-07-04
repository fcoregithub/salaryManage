package cn.com.flaginfo.controller;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.com.flaginfo.utils.ApiContactsGroup;
import cn.com.flaginfo.utils.ApiGetList;
import cn.com.flaginfo.utils.SsoUser;
import cn.com.flaginfo.commons.utils.ssouser.pojo.SpInfo;
import cn.com.flaginfo.commons.utils.ssouser.pojo.UserInfo;
import cn.com.flaginfo.user.auth.common.JsonHelper;
import cn.com.flaginfo.user.auth.common.StringUtil;

/**
 * @2015-12-15
 * 
 */

public abstract class BaseController {
	public Logger logger = Logger.getLogger(getClass());

	public SsoUser getSsoUser(HttpSession session){
		SsoUser SsoUser = (SsoUser) session.getAttribute("_sso_user");
		ApiGetList apiGetList = new ApiGetList();
		logger.info("call returnListByMemberId.the incoming parameters  is:");
		logger.info("spid:"+SsoUser.getLoginSpInfo().getId());
		logger.info("phone:"+SsoUser.getUserInfo().getPersonalPhoneNo());
		logger.info("contactsId:"+SsoUser.getCurRole().getContactsId());
		List<ApiContactsGroup>  list = apiGetList.returnListByMemberId(SsoUser.getLoginSpInfo().getId(),SsoUser.getUserInfo().getPersonalPhoneNo(),SsoUser.getCurRole().getContactsId());
		SsoUser.setDepartMentList(list);
		return SsoUser;
	}

	//本地开发模拟用户
	@SuppressWarnings("unused")
	private SsoUser getExamleUser(){
		SsoUser sUser=new SsoUser();
		UserInfo userInfo=new UserInfo();
		userInfo.setId("16053115165910008943");
		sUser.setUserInfo(userInfo);
		
		SpInfo l=new SpInfo();
		l.setId("16052513315510006368");
		sUser.setLoginSpInfo(l);
		return sUser;
	}
	
	/**
	 * 绑定参数到Map
	 * 
	 * @param request
	 */
	@SuppressWarnings("rawtypes")
	protected Map<String, Object> bindParamToMap(HttpServletRequest request) {
		Enumeration enumer = request.getParameterNames();
		Map<String, Object> map = new HashMap<String, Object>();
		while (enumer.hasMoreElements()) {
			String key = (String) enumer.nextElement();
			String[] values = request.getParameterValues(key);
			if (values.length < 2) {
				String val = request.getParameter(key);
				if (!"randomId".equals(key)) {
					if ("orderBy".equals(key)) {
						if (!StringUtil.isEmpty(val)) {
							Object orderByList = JsonHelper.parseToObject(val, List.class);
							map.put(key, orderByList);
						}
						continue;
					}
					map.put(key, val);
				}
			} else {
				map.put(key, values);
			}
		}
		return map;
	}

}
