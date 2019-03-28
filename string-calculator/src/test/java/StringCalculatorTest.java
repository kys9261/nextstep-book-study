import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringCalculatorTest {

    private StringCalculator stringCalculator;

    @Before
    public void setting() {
        stringCalculator = new StringCalculator();
    }

    // 입력된 문자열값이 빈 문자열 일때
    @Test
    public void paramStringEmpty() {
        assertEquals(0, stringCalculator.add(""));
    }

    // 입력된 문자열값이 null일때
    @Test
    public void paramStringNull() {
        assertEquals(0, stringCalculator.add(null));
    }

    // 쉼표 또는 콜론으로 구분자를 가진 문자열 계산
    @Test
    public void sum() {
        assertEquals(6, stringCalculator.add("1,2,3"));
    }

    // 커스텀 구분자를 가진 문자열 계산
    @Test
    public void sumCustomSeparator() {
        assertEquals(6, stringCalculator.add("//;\n1;2;3"));
    }

    // 문자열에 음수값이 있을때
    @Test(expected = RuntimeException.class)
    public void paramStringNegative() throws RuntimeException {
        stringCalculator.add("1,-1,3");
    }

    // 커스텀 구분자를 정하지 않고, 쉼표 또는 콜론 구분자가 아닐때
    @Test
    public void paramInvalidSeparator() {
        assertEquals(0, stringCalculator.add("1/2/3"));
    }

    // 입력된 문자열에 숫자 앞뒤 공백이 있을때
    @Test(expected = NumberFormatException.class)
    public void paramWhiteSpace() {
        stringCalculator.add("1, 2,3 ");
    }

    // 입력된 문자열에 숫자 앞뒤 공백이 있을때
    @Test(expected = NumberFormatException.class)
    public void paramWhiteSpace2() {
        stringCalculator.add("//;\n1 ; 2; 3 ;");
    }
}
