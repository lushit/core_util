package lush.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import org.springframework.stereotype.Component;

@Component
public class DateUtil {

	/**
	 * 오늘 날짜를 가져온다
	 * returnType Case :
	 * 1 : YYYYMMDD
	 * 2 : YYYY-MM-DD
	 * 3 : YYYY/MM/DD
	 * 4 : YYYY.MM.DD
	 * @return today
	 * @throws Exception
	 */
	public static String getToday(int typeNum) throws Exception {
		LocalDate now = LocalDate.now();
		String today = "";

		switch (typeNum){
			// type : YYYYMMDD
			case  1 :
				today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
				break;
			// type : YYYY-MM-DD
			case  2 :
				today = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				break;
			// type : YYYY/MM/DD
			case  3 :
				today = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
				break;
			// type : YYYY.MM.DD
			case  4 :
				today = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
				break;

			default:
				today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
				break;
		}
		return today;
	}
	
	/**
	 * 기본타입은 인자값이 없는걸로..
	 * @return
	 * @throws Exception
	 */
	public static String getToday() throws Exception {
		// 기본 1로 설정하여 호출
		return getToday(1);
	}

	/**
	 * 두 날짜 사이의 일수를 리턴
	 * returnType : YYYYMMDD
	 * @return period
	 * @throws Exception
	 */
	public static int betweenDays(String fromDate, String toDate) throws Exception {

		LocalDate fDate = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDate tDate = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

		long pe = tDate.toEpochDay() - fDate.toEpochDay();


		int period = (int) pe;

		return period;
	}

	/**
	 * 날짜 형변환
	 * returnType Case :
	 * 1 : YYYYMMDD
	 * 2 : YYYY-MM-DD
	 * 3 : YYYY/MM/DD
	 * 4 : YYYY.MM.DD
	 * @return formatDate
	 * @throws Exception
	 */
	public static String toDateFormat(String date, int typeNum) throws Exception {

		//기본 타겟형식 YYYYMMDD
		LocalDate target = LocalDate.parse(date.replaceAll("[^0-9]",""), DateTimeFormatter.ofPattern("yyyyMMdd"));

		String formatDate = "";

		switch (typeNum){
			// type : YYYYMMDD
			case  1 :
				formatDate = target.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			break;
			// type : YYYY-MM-DD
			case  2 :
				formatDate = target.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			break;
			// type : YYYY/MM/DD
			case  3 :
				formatDate = target.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			break;
			// type : YYYY.MM.DD
			case  4 :
				formatDate = target.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
			break;

			default:
				formatDate = target.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			break;
		}

		return formatDate;
	}

	/**
	 * 해당 일자에 지정된 일수 만큼을 더함
	 * returnType : YYYYMMDD
	 * @return addDate
	 * @throws Exception
	 */
	public static String addDays(String date, int add)  throws Exception {

		//기본 타겟형식 YYYYMMDD
		LocalDate target = LocalDate.parse(date.replaceAll("[^0-9]",""),
			DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays(add);

		//String 형변환
		String addDate = target.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		return addDate;
	}

	/**
	 * 해당 일자에 지정된 개월수 만큼을 더함
	 * returnType : YYYYMMDD
	 * @return addMonth
	 * @throws Exception
	 */
	public static String addMonth(String date, int add)  throws Exception {

		//기본 타겟형식 YYYYMMDD
		LocalDate target = LocalDate.parse(date.replaceAll("[^0-9]",""),
			DateTimeFormatter.ofPattern("yyyyMMdd")).plusMonths(add);

		//String 형변환
		String addMonth = target.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		return addMonth;
	}

	/**
	 * 해당 일자의 요일을 코드로 리턴한다
	 * returnType :  1~7 (월요일 ~ 일요일)
	 * @return dayOfWeek
	 * @throws Exception
	 */
	public static int getDayOfWeek(String date)  throws Exception {

		//기본 타겟형식 YYYYMMDD
		LocalDate target = LocalDate.parse(date.replaceAll("[^0-9]",""),
			DateTimeFormatter.ofPattern("yyyyMMdd"));

		//요일코드 추출
		int dayOfWeek = target.getDayOfWeek().getValue();

		return dayOfWeek;
	}

	/**
	 * 해당 날짜의 첫번째 날짜, 마지막 날짜 리턴
	 * returnType :  YYYYMMDD
	 * flag : 1:첫째 날짜, 2: 마지막날짜
	 * @return dayOfMonth
	 * @throws Exception
	 */
	public static String getDayOfMonth(String date, int flag)  throws Exception {

		//기본 타겟형식 YYYYMMDD
		LocalDate target = LocalDate.parse(date.replaceAll("[^0-9]",""),
			DateTimeFormatter.ofPattern("yyyyMMdd"));

		//해당 날짜의 첫번째 날짜 추출
		String dayOfMonth = "";

		switch (flag) {
			// type : 1 - 해당 날짜의 첫째 날짜
			case 1:
				dayOfMonth = target.with(TemporalAdjusters.firstDayOfMonth())
					.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
				break;
			// type : 2 - 해당 날짜의 마지막 날짜
			case 2:
				dayOfMonth = target.with(TemporalAdjusters.lastDayOfMonth())
					.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
				break;
		}

		return dayOfMonth;
	}

}
