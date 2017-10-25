package utils;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

import filters.FilterDecoratorWithImage;

/**
 * Helpful methods for manipulating various Java 8 Streams features.
 */
public class StreamsUtils {
    /**
     * A utility class should always define a private constructor.
     */
    private StreamsUtils() {
    }

    /**
     * Create a CompletableFuture that, when completed, will convert
     * all the completed CompletableFutures in the @a futureList
     * parameter into a list of joined results.
     * @param <U>
     *
     * @param futureList A list of completable futures.
     * @return A CompletableFuture to a list that will contain all the
     *         joined results.
     */
    public static <T, U> CompletableFuture<List<T>> 
        joinAll (final List<CompletableFuture<T>> futureList) {
        // Use CompletableFuture.allOf() to obtain a CompletableFuture
        // that will itself be complete when all CompletableFutures in
        // futureList parameter have completed.
        final CompletableFuture<Void>
            allDoneFuture = CompletableFuture.allOf
            (futureList.toArray(new CompletableFuture[futureList.size()]));

        // When all futures have completed get a CompletableFuture to
        // a list of joined elements of type T.
        
        
        CompletableFuture<List<T>> allDoneList = 
	        allDoneFuture.thenApply(
	        		new Function< Void, List<T> >() {
						@Override
						public List<T> apply(Void t) {
							List<T> result = new ArrayList<T>(); 
							for(CompletableFuture<T> cf : futureList){
								result.add(cf.join());
							}
							return result;
						}
	        		});
        
        
        
//        CompletableFuture<List<T>> allDoneList = allDoneFuture
//            .thenApply(v -> futureList
//                       // Convert futureList into a stream of
//                       // completable futures.
//                       .stream()
//
//                       // Use map() to join() all completablefutures
//                       // and yield objects of type T.  Note that
//                       // join() should never block.
//                       .map(CompletableFuture::join)
//
//                       // Collect the results of type T into a list.
//                       .collect(toList()));
//
        // Return the CompletableFuture. 
        return allDoneList;
    }

    /**
     * A generic negation predicate that can be used to negate the
     * return value of urlCached (used by Collection.filter() calls).
     *
     * @return The negation of the input predicate.
     */
    public static<T> Predicate<T> not(Predicate<T> p) {
        return p.negate();
    }

    /**
     * Maps the values of an Enum type to a corresponding array of
     * Strings.
     */
    public static String[] getNames(Class<? extends Enum<?>> e) {
    	ArrayList<String> results = new ArrayList<String>();
    	for(Enum<?> c : e.getEnumConstants()){
    		results.add(c.name());
    	}
    	return results.toArray(new String[results.size()]);
    	
//        return Arrays
//            .stream(e.getEnumConstants())
//            .map(Enum::name)
//            .toArray(String[]::new);
    }
}
