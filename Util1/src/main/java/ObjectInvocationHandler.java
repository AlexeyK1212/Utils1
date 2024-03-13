import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectInvocationHandler  implements InvocationHandler {

    private  Object object1=null;
    private final ConcurrentHashMap<String, ObjectAndTimeSaver> cache = new ConcurrentHashMap<>();
    private Thread iterationThread =null;

    public ObjectInvocationHandler(Object object) {

        this.object1 = object;
        //Запускаем поток Отработает с периодичностью 5 сек
        Thread run = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        // чистим MAP
                        for (Object key : new ArrayList<Object>(cache.keySet()))
                            if (cache.get(key.toString()).interval != 0)
                                if (cache.get(key.toString()).time + cache.get(key.toString()).interval < System.currentTimeMillis())
                                    cache.remove(key.toString());
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        run.start();


    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method method1 = object1.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        Object result;



        //Проверка на то что метод помечен как Cache
        if (method1.isAnnotationPresent(Cache.class)) {
            String currentState = StateOfObject(object1,method);

            if (!cache.containsKey(currentState) ) {
                //если кэша нет, то выполняем метод и результат сохраняем в кэш потом
                result = method.invoke(object1, args);
           } else {
                // если кэш есть, то просто возвращаем кэш и продлеваем время жизни потом
                result=cache.get(currentState).obj;
            }
            //тут сохраняем кэш или обновляем время
            cache.put(currentState, new ObjectAndTimeSaver( result, System.currentTimeMillis(),method1.getAnnotation(Cache.class).value()) );
            return result;
        }

        // Если метод не cached
        return method.invoke(object1, args);
    }


//Метод для расчета ключа хэшмапа
    //тут название метода и состояние полей объекта
    private String StateOfObject(Object obj,Method method) {
        String result = method.getName();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                result = result+ "#" + (value != null ? value.toString() : "");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }



}
