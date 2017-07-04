package cn.com.flaginfo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.flaginfo.ApiClient;
import cn.com.flaginfo.commons.utils.api.ApiPathVar;
import cn.com.flaginfo.commons.utils.api.ApiQuery;
import cn.com.flaginfo.utils.SystemConfig;
import cn.com.flaginfo.commons.utils.ssouser.pojo.SsoUser;
import cn.com.flaginfo.user.auth.common.JsonHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ApiGetList {
		
	public <T> List<T> returnList(ApiPathVar apiPathVar,SsoUser user){
		String apiVersion = SystemConfig.getString("api.user.version");
		String spId = SystemConfig.getString("api.user.spId");
		String apiKey = SystemConfig.getString("api.user.key");
		ApiQuery<T> apiQuery = ApiQuery.createDefault(apiPathVar,spId, apiKey, apiVersion);
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("spId", user.getLoginSpInfo().getId());
		query.put("userId",user.getUserInfo().getId());
		query.put("contactsId", user.getCurRole().getContactsId());
//		query.put("objectId", user.getCurRole().getRoleId());
		query.put("containDef", "0");
		apiQuery.setQueryCondition(query);
		return apiQuery.list();
	}
	

			
	public List<ApiContactsGroup> returnListByMemberId(String spId,String phoneNo,String contactsId){
		JSONObject queryMap = new JSONObject();
		queryMap.put("spId", spId);
//		queryMap.put("id", userId);
		queryMap.put("contactsId",contactsId);
		
		List<String> s = new ArrayList<String>();
		s.add(phoneNo);
		queryMap.put("mdnList",s);
		
		String reqParamCon = JsonHelper.parseToJson(queryMap);
		String jsonResultStr = new ApiClient("contacts_member_get_by_mdn", SystemConfig.getString("api.user.key"), SystemConfig.getString("api.user.version"))//contacts_member_get_by_id
				.addHeadPara("operator", "")
				.setJsonBody(reqParamCon)
				.postAsJson();
		System.out.println(jsonResultStr);
		List<ApiContactsGroup>  apiContactsGroups = new ArrayList<ApiContactsGroup>();
		JSONObject groupJson = JSONObject.fromObject(jsonResultStr);
        int returnCode = groupJson.optInt("returnCode");
		if( returnCode != 200 ){
			throw new RuntimeException("获取通讯录组失败");
		}
		JSONArray list = groupJson.getJSONArray("list");
		if(list instanceof JSONArray){
			for(int i=0;i<list.size();i++){
				JSONObject v = list.getJSONObject(i);
				JSONArray o = v.getJSONArray("groupList");
				for (int j = 0; j < o.size(); j++) {
					JSONObject result =  (JSONObject) o.get(i);
					ApiContactsGroup apiContactsGroup = new ApiContactsGroup();
					apiContactsGroup.setId(result.getString("id"));
					apiContactsGroup.setGroupLevel(result.getString("top"));
					apiContactsGroups.add(apiContactsGroup);
				}
					
				
			}
		}
		return apiContactsGroups;
	}
}
