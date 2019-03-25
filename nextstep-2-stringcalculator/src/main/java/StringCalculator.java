import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    /**
     * 요구사항
     * 1. 쉼표 또는 콜론을 구분자로 가지는 문자열을 전달하는 경우 구분자로 분리한 각 숫자의 합을 반환
     * 2. 1번의 쉼표 또는 콜론외에 커스텀 구분자를 지정할수 있고, '//'와 '\n'사이의 문자를 커스텀 구분자로 사용
     * 3. 숫자 값이 음수일때 RuntimeException 처리 해야함
     */

    public int add(String text) {
        // 문자열 빈 문자 확인
        if(text == null || text.isEmpty()) {
            return 0;
        }

        // 문자에 구분자 확인
        String customeSeparator = "";
        Matcher m = Pattern.compile("//(.)\n(.*)").matcher(text);
        if (m.find()) {
            customeSeparator = m.group(1);
        } else {

        }




        // 문자 숫자 변환
        //









        String[] numberToken = text.split(",|:");



        int addNumber = 0;
        for(int i=0; i<numberToken.length; i++) {
            addNumber += Integer.valueOf(numberToken[i]);
        }

        return addNumber;
    }
}
