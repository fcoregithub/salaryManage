package cn.com.flaginfo.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
/**
 * spring----》读取配置文件
 * 
 * @author Administrator
 *
 */
public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private Map<String, String> cinfigCenterDatas = new HashMap<String, String>();

	private String mode = "auto";
	
	private final static Properties properties = new Properties();
	
	public void setCinfigCenterDatas(Map<String, String> cinfigCenterDatas) {
		this.cinfigCenterDatas = cinfigCenterDatas;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public Mode getMode(){
		return Mode.findByCode(this.mode);
	}
	/**
	 * 获取配置文件的值。
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getProperty( String key,String defaultValue ){
		String value = properties.getProperty(key, defaultValue);
		return value;
	}
	
	
	@Override
	protected Properties mergeProperties() throws IOException {
		Properties result = null;
		if(this.getMode() == Mode.CINFIG_CENTER){
			//只读取配置中心
			result = this.getPropertiesFromConfigCenter();
		}else{
			result = super.mergeProperties();
			if(this.getMode() == Mode.AUTO){
				//还要从配置中心读取
				result.putAll(this.getPropertiesFromConfigCenter());
			}
		}
		properties.putAll(result);
		return result;
	}
	
	/**
	 * 从配置中心读取配置。
	 * @return
	 */
	private Properties getPropertiesFromConfigCenter() {
		Properties result = new Properties();
		if(this.cinfigCenterDatas.isEmpty()) return result;
		for(Entry<String, String> entry : this.cinfigCenterDatas.entrySet()){
			DiamondManager manager_pub = new DefaultDiamondManager(entry.getValue(), entry.getKey(), new ManagerListener() {
				public void receiveConfigInfo(String configInfo) {
					//这里是监听变化的地方.
					properties.putAll(toProperties(configInfo));
				}
				public Executor getExecutor() {
					return null;
				}
			});
			Properties properties = manager_pub.getAvailablePropertiesConfigureInfomation(5000);
			result.putAll(properties);
		}
		return result;
	}
	
	
	
	
	private static Properties toProperties( String configInfo ){
		Properties p = new Properties();
		try {
			p.load(new StringReader(configInfo));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	static public enum Mode{
		
		AUTO("auto"),LOCAL("local"),CINFIG_CENTER("cinfigCenter");
		
		public String code;
		private Mode(String code){
			this.code = code;
		}
		static public Mode findByCode( String code ){
			Mode [] modes = Mode.values();
			for(Mode mode : modes){
				if(mode.code.equals(code)) return mode;
			}
			return AUTO;
		}
	}
	
	

}
