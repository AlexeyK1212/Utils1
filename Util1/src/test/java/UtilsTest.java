import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {


    @Test
    void cache() throws InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Fraction fr = new Fraction(1,2);
        Utils ut =new Utils();
        Fractionable num = ut.cache(fr);

        //кэш был пустой значение посчитается
        num.setNum(5);
        assertEquals(num.doubleValue (),2.5);
        assertEquals(outContent.toString().replaceAll("[\n\r]",""),"invoke double value");
        outContent.reset();

        //кэш не пустой, но этого значения там нет, значение посчитается
        //устаревших значений в кэше пока нет
        num.setNum(1);
        assertEquals(num.doubleValue (),0.5);
        assertEquals(outContent.toString().replaceAll("[\n\r]",""),"invoke double value");
        outContent.reset();


        //кэш не пустой и нужное значение там есть, метод не выполнится, значение берется из кэша
        //устаревших значений в кэше пока нет
        num.setNum(5);
        assertEquals(num.doubleValue (),2.5);
        assertEquals(outContent.toString().replaceAll("[\n\r]",""),"");
        outContent.reset();

        //ждем 5 секунд
        sleep(5000);
        //кэш не пустой и нужное значение там есть, метод не выполнится, значение берется из кэша
        //Запущен поток для удаления устаревших значений
        num.setNum(1);
        assertEquals(num.doubleValue (),0.5);
        assertEquals(outContent.toString().replaceAll("[\n\r]",""),"");
        outContent.reset();

        //ждем 5 секунд
        sleep(5000);

        //кэш теперь пустой Удалены устаревшие значения
        //значение посчитается
        num.setNum(5);
        assertEquals(num.doubleValue (),2.5);
        assertEquals(outContent.toString().replaceAll("[\n\r]",""),"invoke double value");
        outContent.reset();


        System.setOut(originalOut);
    }




}