import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    public int add(String text) {
        // 문자열 빈 문자 확인
        if (text == null || text.isEmpty()) {
            return 0;
        }

        // 문자에 구분자 확인
        String[] splitTextArr = null;
        Matcher m = Pattern.compile("//(.)\n(.*)").matcher(text);
        if (m.find()) {
            splitTextArr = m.group(2).split(m.group(1));
        } else if(text.contains(",") || text.contains(":")) {
            splitTextArr = text.split(",|:");
        }

        // 구분자 조건에 만족하지 않음
        if (splitTextArr == null) {
            return 0;
        }

        // 배열에서 숫자 변환후 합산
        int addNumber = 0;
        for(int i=0; i<splitTextArr.length; i++) {
            // 음수값 확인
            if (Integer.valueOf(splitTextArr[i]) < 0) {
                throw new RuntimeException();
            }

            addNumber += Integer.valueOf(splitTextArr[i]);
        }

        return addNumber;
    }
}
