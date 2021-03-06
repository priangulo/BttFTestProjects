package streams;

import static java.util.stream.Collectors.toList;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import filters.Filter;
import filters.FilterDecoratorWithImage;
import utils.Image;
import utils.StreamsUtils;

/**
 * This asynchronous implementation strategy customizes the
 * ImageStreamCompletableFutureBase super class to download, process,
 * and store images asynchronously and concurrently in a thread in the
 * executor's thread pool.
 */
public class ImageStreamCompletableFuture1
       extends ImageStreamCompletableFutureBase {
    /**
     * Constructor initializes the superclass and data members.
     */
    public ImageStreamCompletableFuture1(Filter[] filters,
                                         Iterator<List<URL>> urlListIterator) {
        super(filters, urlListIterator);
    }

    /**
     * Use Java 8 CompletableFutures to download, process, and store
     * images concurrently and asynchronously.
     */
    @Override
    protected void processStream() {
    	List<CompletableFuture<Image>> listOfFutures = new ArrayList<CompletableFuture<Image>>(); 
    	
    	List<URL> listURL = getInput();
    	for(URL url : listURL){
    		if(!(this.urlCached(url))){
    			
    			CompletableFuture<Image> cf1 = this.downloadImageAsync(url);
    			Stream<CompletableFuture<Image>> cf2 = null;
				try {
					cf2 = this.applyFiltersAsync(cf1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			for(Object cf3 : cf2.collect(Collectors.toList())){
    				listOfFutures.add((CompletableFuture<Image>)cf3);
    			}
    			
    		}
    	}
    	
//        // Create a list of completable futures to images.
//        List<CompletableFuture<Image>> listOfFutures = getInput()
//            // Convert the URLs in the input list into a sequential
//            // stream.
//            .stream()
//
//            // Use filter() to ignore URLs that are already cached locally,
//            // i.e., only download non-cached images.
//            .filter(StreamsUtils.not(this::urlCached))
//
//            // Use map() to transform each URL to a completable future
//            // to an image (i.e., asynchronously download each image
//            // via its URL).
//            .map(this::downloadImageAsync)
//
//            // Use flatMap() to create a stream containing completable
//            // futures to multiple filtered/stored versions of each
//            // image.
//            .flatMap(this::applyFiltersAsync)
//
//            // Terminate the stream and collect the results into
//            // list of completable futures to images.
//            .collect(toList());

        // Create a CompletableFuture that can be used to wait for all
        // operations associated with the futures to complete.
        CompletableFuture<List<Image>> allImagesDone =
                StreamsUtils.joinAll(listOfFutures);

        // Print the results.
        System.out.println(TAG 
                           + ": processing of "
                           // This call blocks until all the images
                           // are downloaded, processed, and stored.
                           + allImagesDone.join().size()
                           + " image(s) is complete");
    }

    /**
     * Apply filters asynchronously and concurrently to the @a
     * imageFuture after it finishes downloading and store the results
     * in output files on the local computer.
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    @SuppressWarnings("unchecked")
	private Stream<CompletableFuture<Image>> applyFiltersAsync(CompletableFuture<Image> imageFuture) throws InterruptedException, ExecutionException {
    	List<CompletableFuture<Image>> results = new ArrayList<CompletableFuture<Image>>();
    	for(final Filter filter : mFilters){
    		final Image image = imageFuture.get();
    		
    		CompletableFuture<FilterDecoratorWithImage> cffilterDecoratorWithImage = 
    				imageFuture.thenApply(new Function <Image, FilterDecoratorWithImage>(){
						@Override
						public FilterDecoratorWithImage apply(Image t) {
							return makeFilterDecoratorWithImage(filter, image);
						}
		    		});
    		
    		CompletableFuture<Image> cfImage = 
    				cffilterDecoratorWithImage.thenCompose(new Function<FilterDecoratorWithImage, CompletableFuture<Image>>(){
						@Override
						public CompletableFuture<Image> apply(FilterDecoratorWithImage t) {
							return CompletableFuture.supplyAsync(
									new Supplier<Image>() {
										@Override
										public Image get() {
											// TODO Auto-generated method stub
											return image;
										}
									},
									getExecutor());
						}
    				});
    		
    		results.add(cfImage);                       
    	}
    	
    	return results.stream();
    	
    	
//        return mFilters
//            // Convert the list of filters to a sequential stream.
//            .stream()
//
//            // Use map() to create a completable future to a
//            // FilterDecoratorWithImage object for each filter/image.
//            .map(filter ->
//                 // Returns a new CompletionStage that, when this
//                 // stage completes normally, is executed with this
//                 // stage's result as the argument to the supplied
//                 // lambda expression.
//                 imageFuture.thenApply(image ->
//                                       makeFilterDecoratorWithImage(filter,
//                                                                    image)))
//                                                 
//            // Asynchronously filter the image and store it in an
//            // output file.
//            .map(filterFuture ->
//                 // Returns a new CompletionStage that, when this
//                 // stage completes normally, is executed with this
//                 // stage's result as the argument to the supplied
//                 // lambda expression.
//                 filterFuture.thenCompose(filter ->
//                                          CompletableFuture.supplyAsync(filter::run,
//                                                                        getExecutor())));
    }
}
