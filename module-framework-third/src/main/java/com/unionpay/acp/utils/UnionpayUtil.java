package com.unionpay.acp.utils;

import com.lsxy.framework.config.SystemConfig;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.SDKUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 名称： demo中用到的方法<br>
 * 日期： 2015-09<br>
 * 版本： 1.0.0 
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class UnionpayUtil {

	//商户号码
	public static String merId = SystemConfig.getProperty("third.join.gateway.unionpay.mer_id","802440048160857");

	//默认配置的是UTF-8
	public static String encoding_UTF8 = "UTF-8";
	
	public static String encoding_GBK = "GBK";
	//全渠道固定值
	public static String version = "5.0.0";
	
	//后台服务对应的写法参照 FrontRcvResponse.java
	public static String frontUrl = SystemConfig.getProperty("third.join.gateway.unionpay.front_url","http://localhost/console/cost/recharge/unionpay_return");

	//后台服务对应的写法参照 BackRcvResponse.java
	public static String backUrl = SystemConfig.getProperty("third.join.gateway.unionpay.back_url","http://third.dev.yunhuni.com/third/unionpay/notify");//受理方和发卡方自选填写的域[O]--后台通知地址

	// 商户发送交易时间 格式:YYYYMMDDhhmmss
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	
   /**
	 * 组装请求，返回报文字符串用于显示
	 * @param data
	 * @return
	 */
    public static String genHtmlResult(Map<String, String> data){

    	TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			String key = en.getKey(); 
			String value =  en.getValue();
			if("respCode".equals(key)){
				sf.append("<b>"+key + SDKConstants.EQUAL + value+"</br></b>");
			}else
				sf.append(key + SDKConstants.EQUAL + value+"</br>");
		}
		return sf.toString();
    }
    /**
	 * 功能：解析全渠道商户对账文件中的ZM文件并以List<Map>方式返回
	 * 适用交易：对账文件下载后对文件的查看
	 * @param filePath ZM文件全路径
	 * @return 包含每一笔交易中 序列号 和 值 的map序列
	 */
	public static List<Map> parseZMFile(String filePath){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,131};
		return parseFile(filePath,lengthArray);
	}
	
	/**
	 * 功能：解析全渠道商户对账文件中的ZME文件并以List<Map>方式返回
	 * 适用交易：对账文件下载后对文件的查看
	 * @param filePath ZME文件全路径
	 * @return 包含每一笔交易中 序列号 和 值 的map序列
	 */
	public static List<Map> parseZMEFile(String filePath){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,131};
		return parseFile(filePath,lengthArray);
	}
	
	/**
	 * 功能：解析全渠道商户 ZM,ZME对账文件
	 * @param filePath
	 * @param lengthArray 参照《全渠道平台接入接口规范 第3部分 文件接口》 全渠道商户对账文件 6.1 ZM文件和6.2 ZME 文件 格式的类型长度组成int型数组
	 * @return
	 */
	 private static List<Map> parseFile(String filePath,int lengthArray[]){
	 	List<Map> ZmDataList = new ArrayList<Map>();
	 	try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	//解析的结果MAP，key为对账文件列序号，value为解析的值
        		 	Map<Integer,String> ZmDataMap = new LinkedHashMap<Integer,String>();
                    //左侧游标
                    int leftIndex = 0;
                    //右侧游标
                    int rightIndex = 0;
                    for(int i=0;i<lengthArray.length;i++){
                    	rightIndex = leftIndex + lengthArray[i];
                    	String filed = lineTxt.substring(leftIndex,rightIndex);
                    	leftIndex = rightIndex+1;
                    	ZmDataMap.put(i, filed);
                    }
                    ZmDataList.add(ZmDataMap);
                }
                read.close();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
	 	for(int i=0;i<ZmDataList.size();i++){
	 		System.out.println("行数: "+ (i+1));
	 		Map<Integer,String> ZmDataMapTmp = ZmDataList.get(i);
	 		
	 		for(Iterator<Integer> it = ZmDataMapTmp.keySet().iterator();it.hasNext();){
	 			Integer key = it.next();
	 			String value = ZmDataMapTmp.get(key);
		 		System.out.println("序号："+ key + " 值: '"+ value +"'");
		 	}
	 	}
		return ZmDataList;	
	}


	public static void main(String[] args) {
		Map map = new HashMap();
		map.put("aaa","aaa");
		map.put("bbbb","bbbb");

        Map<String, String> sign = AcpService.sign(map, UnionpayUtil.encoding_UTF8);

        System.out.println(sign);

        System.out.println(AcpService.encryptTrack("12", "utf-8"));

		Map<String,String> customerInfoMap = new HashMap<String,String>();
		//customerInfoMap.put("certifTp", "01");
		//customerInfoMap.put("certifId", "341126197709218366");
		//customerInfoMap.put("customerNm", "互联网");
		customerInfoMap.put("phoneNo", "13552535506");
		//customerInfoMap.put("smsCode", "123456");
		//customerInfoMap.put("pin", "626262");						//密码加密
		//customerInfoMap.put("cvn2", "123");           				//卡背面的cvn2三位数字
		//customerInfoMap.put("expired", "1711");  				    //有效期 年在前月在后

		//System.out.println(getCustomerInfoWithEncrypt(customerInfoMap,"6217001210048797565"));

		parseZMFile("C:\\Users\\wulh\\Desktop\\802310048993424_20150905\\INN15090588ZM_802310048993424");
	}



}