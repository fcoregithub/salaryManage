package cn.com.flaginfo.utils;

import java.util.ArrayList;
import java.util.List;

import cn.com.flaginfo.commons.utils.ssouser.pojo.SpInfo;
import cn.com.flaginfo.commons.utils.ssouser.pojo.SpRole;
import cn.com.flaginfo.commons.utils.ssouser.pojo.UserInfo;
import org.apache.commons.lang.StringUtils;


public class SsoUser {
	private SpInfo loginSpInfo;
	private UserInfo userInfo;
	private SpRole curRole;
	
	private List<ApiContactsGroup> departMentList = new ArrayList<ApiContactsGroup>();
	
	private String groupIds;
	
	public SpInfo getLoginSpInfo() {
		return loginSpInfo;
	}
	public void setLoginSpInfo(SpInfo loginSpInfo) {
		this.loginSpInfo = loginSpInfo;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public SpRole getCurRole() {
		return curRole;
	}
	public void setCurRole(SpRole curRole) {
		this.curRole = curRole;
	}
	public List<ApiContactsGroup> getDepartMentList() {
		return departMentList;
	}
	public void setDepartMentList(List<ApiContactsGroup> departMentList) {
		this.departMentList = departMentList;
	}
	public String getGroupIds() {
		if(departMentList!=null){
			groupIds = "";
			for(ApiContactsGroup group : departMentList){
				if(StringUtils.isNotEmpty(group.getId())){
					groupIds+=group.getId()+",";
				}
			}
		}
		return groupIds;
	}
	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}
	
	
	
	
}
