
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;


public class Utils {


    public static <T extends Fractionable> T cache(T object) {


        return (T) Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                new Class<?>[]{Fractionable.class},
                new ObjectInvocationHandler(object)

        );
    }

}
