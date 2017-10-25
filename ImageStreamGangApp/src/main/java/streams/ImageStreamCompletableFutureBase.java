package streams;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import filters.Filter;
import filters.FilterDecoratorWithImage;
import utils.Image;

/**
 * Super class that factors out common code so that implementation
 * strategies can use the Java 8 CompletableFutures framework to
 * download, process, and store images asynchronously.
 */
public abstract class ImageStreamCompletableFutureBase
       extends ImageStreamGang {
    /**
     * Constructor initializes the superclass.
     */
    public ImageStreamCompletableFutureBase(Filter[] filters,
                                            Iterator<List<URL>> urlListIterator) {
        super(filters, urlListIterator);
    }

    /**
     * Asynchronously download an image from the @a url parameter and
     * return a CompletableFuture that completes when the image
     * finishes downloading.
     */
    protected CompletableFuture<Image> downloadImageAsync(URL url) {
    	final Image image = downloadImage(url);
    	
    	return  CompletableFuture.supplyAsync( new Supplier<Image>() {

			@Override
			public Image get() {
				// TODO Auto-generated method stub
				return image;
			}
		}  , getExecutor());
    	
    	//return  CompletableFuture.supplyAsync((Supplier<Image>) downloadImage(url), getExecutor());
    	
    	
    	
        // Asynchronously download an Image from the url parameter.
//        return CompletableFuture.supplyAsync(() -> downloadImage(url),
//                                             getExecutor());
    }

    /**
     * Asynchronously filter the image and store it in an output file.
     * Returns a CompletableFuture that completes when the image has
     * been filtered and stored.
     */
    protected CompletableFuture<Image> filterImageAsync
        (FilterDecoratorWithImage filterDecoratorWithImage) {
    	
    	final Image image = filterDecoratorWithImage.run();
    	
    	return  CompletableFuture.supplyAsync( new Supplier<Image>() {

			@Override
			public Image get() {
				// TODO Auto-generated method stub
				return image;
			}
		}  , getExecutor());
    	
    	
    	 //return CompletableFuture.supplyAsync((Supplier<Image>) filterDecoratorWithImage.run(), getExecutor());
    	
//        // Asynchronously filter the image and store it in an output
//        // file.
//        return CompletableFuture.supplyAsync(filterDecoratorWithImage::run,
//                                             getExecutor());
    }
}
