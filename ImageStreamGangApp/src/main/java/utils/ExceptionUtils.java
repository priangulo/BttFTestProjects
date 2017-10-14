package utils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ExceptionUtils {
    @FunctionalInterface
        public interface Consumer_WithExceptions<T> {
        void accept(T t) throws Exception;
    }

    @FunctionalInterface
        public interface Function_WithExceptions<T, R> {
        R apply(T t) throws Exception;
    }

    @FunctionalInterface
        public interface Supplier_WithExceptions<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
        public interface Runnable_WithExceptions {
        void accept() throws Exception;
    }

    /** .forEach(rethrowConsumer(name -> System.out.println(Class.forName(name)))); or .forEach(rethrowConsumer(ClassNameUtil::println)); */
    public static <T> Consumer<T> rethrowConsumer(Consumer_WithExceptions<T> consumer) {    	
    	try {
	    	T t = null;
	    	consumer.accept(t);
	    	return (Consumer<T>) consumer;
    	}
    	catch (Exception exception) { 
    		try {
				throwAsUnchecked(exception);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    	}
    	return null;
//    	return t -> {
//            try { consumer.accept(t); }
//            catch (Exception exception) { throwAsUnchecked(exception); }
//        };  
    }

    /** .map(rethrowFunction(name -> Class.forName(name))) or .map(rethrowFunction(Class::forName)) */
    public static <T, R> Function<T, R> rethrowFunction(Function_WithExceptions<T, R> function) {
    	try {
	    	T t = null;
	    	return (Function<T, R>) function.apply(t);
	    	//return (Consumer<T>) consumer;
    	}
    	catch (Exception exception) { 
    		try {
				throwAsUnchecked(exception);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    	}
    	return null;
    	
//    	return t -> {
//            try { return function.apply(t); }
//            catch (Exception exception) { throwAsUnchecked(exception); return null; }
//        };
    }

    /** rethrowSupplier(() -> new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))), */
    public static <T> Supplier<T> rethrowSupplier(Supplier_WithExceptions<T> function) {
    	try { return (Supplier<T>) function.get(); }
        catch (Exception exception) { try {
			throwAsUnchecked(exception);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} return null; }
//        return () -> {
//            try { return function.get(); }
//            catch (Exception exception) { throwAsUnchecked(exception); return null; }
//        };
    }

    /** uncheck(() -> Class.forName("xxx")); */
    public static void uncheck(Runnable_WithExceptions t)
    {
        try { t.accept(); }
        catch (Exception exception) { try {
			throwAsUnchecked(exception);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} }
    }

    /** uncheck(() -> Class.forName("xxx")); */
    public static <R> R uncheck(Supplier_WithExceptions<R> supplier)
    {
        try { return supplier.get(); }
        catch (Exception exception) { try {
			throwAsUnchecked(exception);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} return null; }
    }

    /** uncheck(Class::forName, "xxx"); */
    public static <T, R> R uncheck(Function_WithExceptions<T, R> function, T t) {
        try { return function.apply(t); }
        catch (Exception exception) { try {
			throwAsUnchecked(exception);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} return null; }
    }

    @SuppressWarnings ("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E { throw (E)exception; }
}
