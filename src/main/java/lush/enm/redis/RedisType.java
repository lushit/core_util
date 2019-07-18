package lush.enm.redis;

/**
 * System에서 사용되는 모든 Redis Type을 관리
 */
public enum RedisType {
	/**
	 * 사용자 관리
	 */
	// 사용자 전체목록
	MEMBER_LIST("MEMBER_LIST","List")
	// 사용자 종합 정보 (기본 + 상세)
	, MEMBER_INFO("MEMBER_INFO", "HashMap")
	// 사용자 전체목록
	, CUSTOMER_LIST("CUSTOMER_LIST","List")
	// 사용자 기본정보
	, CUSTOMER_MST("CUSTOMER_MST", "HashMap")
	// 사용자 상세정보
	, CUSTOMER_DTL("CUSTOMER_DTL", "HashMap")
	// 사용자 종합 정보 (기본 + 상세)
	, CUSTOMER_INFO("CUSTOMER_INFO", "HashMap")
	// 로그인한 사용자정보
	, LOGIN_MEMBER("LOGIN_MEMBER", "Value")
	
	/**
	 * 시스템 관리
	 */
	// 코드 마스터 전체 목록
	, CODE_MST_LIST("CODE_MST_LIST", "List")
	// 코드 상세 목록
	, CODE_DTL_LIST("CODE_DTL_LIST", "List")
	
	/**
	 * 매장관리
	 */
	// 매장관리
	
	/**
	 * 예약관리
	 */
	//
	
	/**
	 * 결제 관리
	 */
	//
	
	/**
	 * 메세지 관리
	 */
	//
	
	/**
	 * Common
	 */
	// 공용 Key (사실상 쓸일은 없을듯)
	// 항상 제일 마지막에 위치
	// 추가 할일이 있다면 위쪽으로
	, COMMON("COMMON", "Value");
	
	
	// Redis Type
	private String redisType = "";
	
	// Data Type
	private String dataType = "";
	
	// Get Redis Type
	public String getRedisType()
	{
		return redisType;
	}
	
	// Get Data Type
	public String getDataType()
	{
		return dataType;
	}
	
	// 생성자
	RedisType(String redisType, String dataType)
	{
		this.redisType	= redisType;
		this.dataType	= dataType;
	}
}
