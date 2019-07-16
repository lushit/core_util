package lush.util;

import java.util.HashMap;
import java.util.List;
import lush.enm.ExceptionType;
import lush.enm.redis.RedisType;
import lush.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	// Redis Template
	private RedisTemplate<String, Object> redisTemplate;
	// 단일값
	private ValueOperations<String, Object> valueOperations;
	// HashMap
	private HashOperations<String, String, Object> hashOperations;
	// List
	private ListOperations<String, Object> listOperations;
	
	/**
	 * Redis 관련항목 초기화
	 * @param redisTemplate
	 * @throws Exception
	 */
	@Autowired
	@SuppressWarnings("unchecked")
	public RedisUtil(RedisTemplate redisTemplate) throws  Exception {
		// Template 부여
		this.redisTemplate	= redisTemplate;
		
		// 단일값 부여
		valueOperations		= redisTemplate.opsForValue();
		// Hash 부여
		hashOperations		= redisTemplate.opsForHash();
		// List 부여
		listOperations		= redisTemplate.opsForList();
	}
	
	/**
	 * Key가 따로 없을 경우
	 * @param redisType
	 * @return
	 */
	public Object getRedisData(RedisType redisType) {
		// 그냥 호출해준다
		return getRedisData(redisType, "");
	}
	
	/**
	 * Redis에 있는 HashMap Data를 불러옴
	 *
	 * @param redisType
	 * @param id
	 * @return
	 */
	public Object getRedisData(RedisType redisType, String id) {
		// Parameter 로 Redis Key를 생성
		String redisKey = getRedisKey(redisType, id);
		
		// Return
		Object result = null;
		
		// Redis에 들어있는 Data가 HashMap 일 경우
		if(redisType.getDataType().equals("HashMap")) {
			// Data 찾기
			result = hashOperations.entries(redisKey);
		}
		// Redis에 들어있는 Data가 List 일 경우
		else if(redisType.getDataType().equals("List")) {
			// Data 찾기
			result = hashOperations.get(redisKey,"");
		}
		// Redis에 들어있는 Data가 Value 일 경우
		else if(redisType.getDataType().equals("Value")) {
			// Data 찾기
			result = valueOperations.get(redisKey);
		}
		
		return result;
	}
	
	/**
	 * Redis에 단일 Data 셋팅 (객체가 아닌 데이터)
	 *
	 * @param redisType
	 * @param id
	 * @param val
	 * @throws Exception
	 */
	public void setRedisData(RedisType redisType, String id, String val) throws Exception {
		// Setting 할 Data 가 있는지 체크
		if(val != null) {
			// Parameter 로 Redis Key를 생성
			String redisKey = getRedisKey(redisType, id);
			
			// 데이터 셋팅
			valueOperations.set(redisKey, val);
		}
		// Setting 할 Data 가 없다면
		else {
			// 데이터가 없다는 Exception 발생
			throw new BaseException().setExceptionType(ExceptionType.NOT_FOUND_DATA);
		}
	}
	
	/**
	 * Redis에 HashMap Data 셋팅
	 *
	 * @param redisType
	 * @param id
	 * @param valueMap
	 * @throws Exception
	 */
	public void setRedisData(RedisType redisType, String id, HashMap<String, Object> valueMap) throws Exception {
		// Setting 할 Data 가 있는지 체크
		if(valueMap != null) {
			// Parameter 로 Redis Key를 생성
			String redisKey = getRedisKey(redisType, id);
			
			// 데이터 셋팅
			hashOperations.putAll(redisKey, valueMap);
		}
		// Setting 할 Data 가 없다면
		else {
			// 데이터가 없다는 Exception 발생
			throw new BaseException().setExceptionType(ExceptionType.NOT_FOUND_DATA);
		}
	}
	
	/**
	 * Redis에 List Data 셋팅
	 *
	 * @param redisType
	 * @param list
	 * @throws Exception
	 */
	public void setRedisData(RedisType redisType, List list) throws Exception {
		// Setting 할 Data 가 있는지 체크
		if(list != null) {
			// Parameter 로 Redis Key를 생성
			String redisKey = getRedisKey(redisType, "");
			
			// 데이터 셋팅
			// listOperation은 LinkedHashMap 형태를 받아주지 못함.
			hashOperations.put(redisKey,"",list);
		}
		// Setting 할 Data 가 없다면
		else {
			// 데이터가 없다는 Exception 발생
			throw new BaseException().setExceptionType(ExceptionType.NOT_FOUND_DATA);
		}
	}
	
	/**
	 * Redis Key에 해당하는 Data 삭제
	 * Key가 이미 만들어져 있는 경우
	 * @param redisKey
	 */
	public void deleteRedisData(String redisKey) {
		// redis data 삭제
		redisTemplate.delete(redisKey);
	}
	
	/**
	 * Redis Key에 해당하는 Data 삭제
	 * Key를 만들어서 삭제해야 하는 경우
	 * @param redisType
	 * @param id
	 */
	public void deleteRedisData(RedisType redisType, String id) {
		// Parameter 로 Redis Key를 생성
		String redisKey = getRedisKey(redisType, id);
		
		// 실제 데이터 삭제
		deleteRedisData(redisKey);
	}

	/**
	 * 여러개의 Redis Data를 삭제할때
	 *
	 * @param keys
	 */
	public void deleteRedisDataList(List<String> keys) {
		// Key값이 깨지지 않도록 직렬화
		redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
		
		// 삭제
		redisTemplate.delete(keys);
	}
	
	/**
	 * Redis 에 사용될 키를 만들어서 리턴
	 * ex) DETAIL:100004
	 * @param redisType
	 * @param id
	 * @return
	 */
	public String getRedisKey(RedisType redisType, String id) {
		// Redis Type은 기본 Key
		String redisKey = String.valueOf(redisType.getRedisType());
		
		// ID 값이 있을 경우 Key 값에 추가
		if(!id.equals("")) {
			redisKey += ":" + id;
		}
		
		return redisKey;
	}
}
