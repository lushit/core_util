package lush.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lush.enm.ExceptionType;
import lush.enm.redis.RedisType;
import lush.exception.BaseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 기본적인 형변환, 데이터타입 변경 등
 * Util 성을 가진 메소드를 관리
 */
@Component
public class CommUtil {
	private static Gson gson = new Gson();
	
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * Json을 Object로 변경
	 *
	 */
	public static <T> T getJsonToObject (String jsonString, Class<T> type) {
		return gson.fromJson (jsonString, type);
	}

	protected <T> List<T> getJsonToListObject (String jsonString, Class<T> type) {
		List<T> list = new ArrayList<T>();
		JSONArray jsonArray = null;
		try {
			jsonString = "{\"list\":" + jsonString + "}";
			jsonArray = new JSONObject(jsonString).getJSONArray("list");

			for(int ii = 0; ii<jsonArray.length();ii++){
				list.add(gson.fromJson(jsonArray.get(ii).toString(), type));
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}


	/**
	 * Map을 JSONObject 로 변경
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getMapToJSON(Map map) throws Exception
	{
		JSONObject json = new JSONObject();

		if(map != null)
		{
			Iterator it = map.keySet().iterator();

			String key		= "";
			String value	= "";
			while(it.hasNext())
			{
				key		= String.valueOf(it.next());
				value	= String.valueOf(map.get(key));
				json.put(key, value);
			}
		}

		return json;
	}

	/**
	 * 쿼리결과를 JSON Array 형식으로 변경
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static List getListToJsonArray(List list) throws Exception
	{
		// JSON 배열용
		ArrayList resultList = new ArrayList();

		Map map = null;

		for(int i=0; i<list.size(); i++)
		{
			map = (Map)list.get(i);

			resultList.add(getMapToJSON(map));
		}

		return resultList;
	}

	/**
	 * Dto를 Map 으로 변경
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public static Map getDtoToMap(Object dto) throws Exception{
		Map map = new HashMap();

		// DTO의 모든 필드명
		Field[] fields = dto.getClass().getDeclaredFields();
		for(int i=0; i <fields.length; i++){
			fields[i].setAccessible(true);
			try{
				map.put(fields[i].getName(), fields[i].get(dto));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * JsonArray 형태를 List<HashMap<String,Object>> 으로 변경
	 * @param jsonArray
	 * @return
	 * @throws Exception
	 */
	public static List<HashMap<String,Object>> getJsonArrayToListHashMap (JsonArray jsonArray) {
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		try {
			//JsonArray값을 받아와서 List<HashMap>으로 변환
			for(int j = 0; j<jsonArray.size();j++){
				list.add(getJsonToMap(jsonArray.get(j).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * JSON String 형태를 HASHMAP으로 변경
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static HashMap getJsonToMap(String jsonString) throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		HashMap map = new HashMap();

		if(null !=  jsonString && jsonString.length() > 0)
		{
			map =  mapper.readValue(jsonString, new TypeReference<HashMap>(){});
		}

		return map;
	}
	
	/**
	 * BigInteger 를 String 으로 변환
	 * @param val
	 * @return
	 */
	public  String getIntToString(Object val) {
		// 우선 넘겨받은 값이 Null 인지 체크
		if(val == null) {
			return null;
		}
		// Null 이 아닐 경우 BigInteger Type 확인
		else if(val.getClass().getTypeName().equals("java.math.BigInteger")) {
			// Casting
			BigInteger bigVal = (BigInteger) val;
			
			// to String
			return bigVal.toString();
		}
		// Null 이 아닐 경우 BigInteger Type 확인
		else if(val.getClass().getTypeName().equals("java.lang.Integer")) {
			// Casting
			Integer intVal = (Integer) val;
			
			// to String
			return intVal.toString();
		}
		// 맞지 않을 경우에는 Null
		else {
			return (String) val;
		}
		
	}
	
	/**
	 * 클라이언트 아이피 확인
	 * @param request
	 * @return
	 */
	public static String getClientIP(HttpServletRequest request)
	{
		String ip = null;
		
		ip = request.getHeader("X-Forwarded-For");
		
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip  ==null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("X-Real-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("X-RealIP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("REMOTE_ADDR");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}
		
		return ip;
	}
	
	/**
	 * 공통 Null Check
	 * @param obj
	 * @return
	 */
	public boolean isNull(Object obj) {
		// 기본으로 널 체크
		if(obj == null) {
			return true;
		}
		
		// Object의 Type Name
		String typeName = obj.getClass().getTypeName();
		
		// Type 별 Null Check
		// String
		if(typeName.equals("java.lang.String")) {
			// Value 의 빈값 Check
			if(String.valueOf(obj).equals("")) {
				return true;
			}
		}
		// List 또는 ArrayList
		else if(typeName.equals("java.util.List") || typeName.equals("java.util.ArrayList")) {
			// 받은값을 List로 변형
			List list = (List) obj;
			
			// List가 비어있는지 Check
			if(list.isEmpty()) {
				return true;
			}
		}
		// HashMap
		else if(typeName.equals("java.util.HashMap")) {
			// 받은값을 List로 변형
			HashMap map = (HashMap) obj;
			
			// List가 비어있는지 Check
			if(map.isEmpty()) {
				return true;
			}
		}
		// LinkedHashMap
		else if(typeName.equals("java.util.LinkedHashMap")) {
			// 받은값을 List로 변형
			LinkedHashMap map = (LinkedHashMap) obj;
			
			// List가 비어있는지 Check
			if(map.isEmpty()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 파라미터 유무 체크만을 해서 없을 경우에는 Exception 처리
	 * @param paramData
	 * @throws Exception
	 */
	public void validationParamData(Object paramData) throws Exception {
		// 파라미터가 없을 경우에는
		if(isNull(paramData)) {
			// 잘 못된 요청으로 처리
			throw  new BaseException().setExceptionType(ExceptionType.BAD_REQUEST);
		}
	}
	
	/**
	 * 결과 데이터의 체크만을 해서 없을 경우에는 Exception 처리
	 * @param paramData
	 * @throws Exception
	 */
	public void validationResultData(Object paramData) throws Exception {
		// 파라미터가 없을 경우에는
		if(isNull(paramData)) {
			// 잘 못된 요청으로 처리
			throw  new BaseException().setExceptionType(ExceptionType.NOT_FOUND_DATA);
		}
	}

	/**
	 * 로그인을 완료한 사용자 정보를 세션에 저장
	 * @param session
	 * @param sessionKey
	 * @param memberMap
	 */
	public void setMemberSession(HttpSession session, String sessionKey, HashMap memberMap) throws Exception {
		// memberNo 추출
		String memberNo =  getIntToString(memberMap.get("MEMBER_NO"));
		// 세션에 사용자정보 담기
		session.setAttribute(sessionKey, memberMap);
		// Redis에 로그인한 ID 정보 저장(이미 로그인한 ID인지 체크용도)
		redisUtil.setRedisData(RedisType.LOGIN_MEMBER, memberNo, memberNo);
	}
	
	/**
	 * 로그인한 사용자의 세션 및 레디스 MEMBER_NO 삭제
	 * @param session
	 * @param sessionKey
	 * @param memberMap
	 */
	public void deleteMemberSession(HttpSession session, String sessionKey, HashMap memberMap) throws Exception {
		// memberNo 추출
		String memberNo =  getIntToString(memberMap.get("MEMBER_NO"));
		
		// 기존 세션 삭제
		session.removeAttribute(sessionKey);
		session.invalidate();
		// Redis에 저장된 로그인정보 삭제
		redisUtil.deleteRedisData(RedisType.LOGIN_MEMBER, memberNo);

	}
	
	/**
	 * 로그인 상태확인
	 * @param memberMap
	 */
	public boolean getLogined(HashMap memberMap) throws Exception {
		// memberNo 추출
		String memberNo =  getIntToString(memberMap.get("MEMBER_NO"));
		
		// Redis에 저장된 로그인정보 가져온다.
		String redisLoginMemberNo = (String) redisUtil.getRedisData(RedisType.LOGIN_MEMBER, memberNo);
		// 로그인 상태확인
		if(isNull(redisLoginMemberNo)) {
			return true;
		}
		// 이미 로그인 중이면
		else {
			return false;
		}
	}
	
	/**
	 * 파람으로 넘어온 JsonArray ListHashMap으로 변환
	 * @param request
	 * @param paramName
	 * @return
	 */
	public static List<HashMap<String,Object>> getParam (HttpServletRequest request, String paramName) throws  Exception{
		 new ArrayList<HashMap<String,Object>>();
		
		//Parameter에서 Tui-Grid Data 추출방법(update)
		String gridData = request.getParameter(paramName);
		
		//받아온 Parmeter 를 JasonParser로 파싱하여 JsonArray로 변환.
		JsonParser parser = new JsonParser();
		JsonArray jsonArray = parser.parse(gridData).getAsJsonArray();
		
		// JsonArray 형태의 값을 List<HashMap>으로 변형
		List<HashMap<String,Object>> list = getJsonArrayToListHashMap(jsonArray);
		
		return list;
	}
}
