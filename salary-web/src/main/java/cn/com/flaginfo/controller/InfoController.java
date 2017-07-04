package cn.com.flaginfo.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.flaginfo.utils.SystemConfig;
import cn.com.flaginfo.commons.utils.ssouser.pojo.SsoUser;
import cn.com.flaginfo.utils.MapResult;
import cn.com.flaginfo.utils.MapResult.Invoker;

@RestController
@RequestMapping(value = "info")
public class InfoController extends BaseController {
	
	@RequestMapping(value = "/loginUser")
	public Map<String, Object> loginUser(HttpSession session) {
		final SsoUser ssoUser = this.getSsoUser(session);
		Map<String, Object> resultMap = MapResult.invoke(new Invoker() {
			@Override
			public void writeBody(Map<String, Object> body) throws Exception {
				body.put("ssoUser", ssoUser);
			}
		});
		return resultMap;
	}
	
	@RequestMapping(value = "/config")
	public Map<String, Object> config(final String keys) {
		Map<String, Object> resultMap = MapResult.invoke(new Invoker() {
			@Override
			public void writeBody(Map<String, Object> body) throws Exception {
				JSONObject object = new JSONObject();
				if(StringUtils.isNotEmpty(keys)){
					String[] arr = keys.split(",");
					for (int i=0;i<arr.length;i++) {
						object.put(arr[i],SystemConfig.getString(arr[i]));
					}
				}
				body.put("data", object);
			}
		});
		return resultMap;
	}
	
}
