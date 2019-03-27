import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    public int add(String text) {
        // 빈 문자열 확인
        if (isEmpty(text)) {
            return 0;
        }

        // 문자에 구분자 확인
        String[] splitTextArr = textSeparate(text);

        // 구분자 조건에 만족하지 않음
        if (splitTextArr == null) {
            return 0;
        }

        // 배열에서 숫자 변환후 합산
        return sum(parseToInt(textSeparate(text)));
    }

    private boolean isEmpty(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }
        return false;
    }

    private String[] textSeparate(String text) {
        String[] speratedArr = null;
        Matcher m = Pattern.compile("//(.)\n(.*)").matcher(text);
        if (m.find()) {
            speratedArr = m.group(2).split(m.group(1));
        } else if(text.contains(",") || text.contains(":")) {
            speratedArr = text.split(",|:");
        }

        return speratedArr;
    }

    private int[] parseToInt(String[] textSeparatedArr) {
        int[] numberArr = new int[textSeparatedArr.length];
        for(int i=0; i<textSeparatedArr.length; i++) {
            numberArr[i] = checkNegative(textSeparatedArr[i]);
        }

        return numberArr;
    }

    private int checkNegative(String numberText) {
        int number = Integer.parseInt(numberText);
        if(number < 0) {
            throw new RuntimeException();
        }

        return number;
    }

    private int sum(int[] numbers) {
        int sum = 0;
        for(int i=0; i<numbers.length; i++) {
            sum += numbers[i];
        }

        return sum;
    }
}
