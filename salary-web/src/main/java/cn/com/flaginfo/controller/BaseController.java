package cn.com.flaginfo.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.com.flaginfo.commons.utils.json.JSONUtils;
import cn.com.flaginfo.commons.utils.ssouser.pojo.SpInfo;
import cn.com.flaginfo.commons.utils.ssouser.pojo.SpRole;
import cn.com.flaginfo.commons.utils.ssouser.pojo.SsoUser;
import cn.com.flaginfo.commons.utils.ssouser.pojo.UserInfo;
import cn.com.flaginfo.user.auth.common.StringUtil;
import cn.com.flaginfo.user.auth.common.JsonHelper;

public abstract class BaseController {
	private static final String USER_ROLE_SUPER_ADMIN = "0";// 超级管理员
	private static final String GROUP_AUTH = "write";// 超级管理员
	private static Logger log = Logger.getLogger(BaseController.class);

	public SsoUser getSsoUser(HttpSession session) {
		@SuppressWarnings("unchecked")
		Map<String, Object> ssoUesrMap = (Map<String, Object>) session.getAttribute("sso_user");
		UserInfo userInfo = null;
		SsoUser ssoUser = null;
		if (ssoUesrMap != null) {
			try {
				userInfo = (UserInfo) JSONUtils.toBean(JSONUtils.fromObject(ssoUesrMap.get("userInfo")), UserInfo.class);
				SpInfo spInfo = (SpInfo) JSONUtils.toBean(JSONUtils.fromObject(ssoUesrMap.get("loginSpInfo")),SpInfo.class);
				ssoUser = new SsoUser();
				ssoUser.setUserInfo(userInfo);
				ssoUser.setLoginSpInfo(spInfo);
				// 新增用户当前角色2016-4-28
				SpRole curRole = (SpRole) JSONUtils.toBean(JSONUtils.fromObject(JSONUtils.fromObject(ssoUesrMap.get("loginSpInfo")).get("curRole")),SpRole.class);
				ssoUser.setCurRole(curRole);
			} catch (Exception e) {
				log.error("get sso user,error msg:" + e.getMessage());
			}
		}
		return ssoUser;
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
