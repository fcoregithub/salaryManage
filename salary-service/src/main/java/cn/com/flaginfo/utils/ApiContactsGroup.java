package cn.com.flaginfo.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import cn.com.flaginfo.commons.utils.api.ApiPathVar;
import cn.com.flaginfo.commons.utils.api.ApiQuery;
import cn.com.flaginfo.commons.utils.api.pojo.ApiMember;
import cn.com.flaginfo.utils.SystemConfig;
import net.sf.json.JSONObject;

public class ApiContactsGroup  implements Serializable{
	/**
	 * 分组ID
	 */
	private String id;
	/**
	 * 创建日期
	 */
	private String createTime;
	/**
	 * 分组级
	 */
	private String groupLevel;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建人Id
	 */
	private String userId;
	/**
	 * 分组名称
	 */
	private String name;

	private String seq;

	private String contactsId;
	/**
	 * 父级ID
	 */
	private String pid;

	private String spId;
	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * 查询一个组下面的用户
	 * @return
	 */
	public List<ApiMember> findApiUseres(){
		Map<String, Object> param = new HashMap<String, Object>(3);
		param.put("spId", this.getSpId());
		param.put("contactsId", this.getContactsId());
		List<Map<String, String>> queryList = new ArrayList<Map<String, String>>();
		Map<String, String> query = new HashMap<String, String>();
		query.put("groupId", this.getId());
		queryList.add(query);
		param.put("list", queryList);
		String apiVersion = SystemConfig.getString("api.user.version");
		String spId = SystemConfig.getString("api.user.spId");
		String apiKey = SystemConfig.getString("api.user.key");
		ApiQuery<ApiMember> apiQuery = ApiQuery.createDefault(ApiPathVar.通讯录_所有联系人_分页, spId, apiKey, apiVersion);
		return apiQuery.setQueryCondition(param).list();
	}
	
	public List<ApiContactsGroup> findChildren(){
		Map<String, Object> param = new HashMap<String, Object>(3);
		param.put("spId", this.getSpId());
		param.put("contactsId", this.getContactsId());
		param.put("groupId", this.getId());
		String apiVersion = SystemConfig.getString("api.user.version");
		String spId = SystemConfig.getString("api.user.spId");
		String apiKey = SystemConfig.getString("api.user.key");
		ApiQuery<ApiContactsGroup> apiQuery = ApiQuery.createDefault(ApiPathVar.通讯录_分组子级,spId, apiKey, apiVersion);
		apiQuery.setQueryCondition(param);
		return apiQuery.list();
	}
	
	
	static public ApiContactsGroup tranfer(JSONObject json){
		ApiContactsGroup apiContacts = new ApiContactsGroup();
		Field [] fields = ApiContactsGroup.class.getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			String name = field.getName();
			String value = json.optString(name);
			setValue(apiContacts, name, value);
			field.setAccessible(false);
		}
		return apiContacts;
	}
	
	public static void setValue(Object bean,String name,Object value){
		try {
			PropertyUtils.setProperty(bean, name, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(String groupLevel) {
		this.groupLevel = groupLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getContactsId() {
		return contactsId;
	}

	public void setContactsId(String contactsId) {
		this.contactsId = contactsId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
