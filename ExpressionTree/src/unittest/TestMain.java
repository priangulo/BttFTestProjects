package unittest;

import static org.junit.Assert.*;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.example.expressiontree.InputDispatcher;
import com.example.expressiontree.Options;
import com.example.expressiontree.Platform;
import com.example.expressiontree.PlatformFactory;

public class TestMain {

	@Test
	public void testCreatePlatform() throws UnsupportedEncodingException {
		Platform platform = Platform.instance (new PlatformFactory(System.in,
                System.out,
                null).makePlatform());
		
		assert(platform.platformName() != null);
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Test
	public void testFormat() throws UnsupportedEncodingException {
		
		String command = "format in-order";
		InputStream input = new ByteArrayInputStream(Charset.forName("UTF-16").encode(command).array());
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream, true, "utf-8");
		
		Platform.instance (new PlatformFactory(input,
                printStream,
                null).makePlatform());
		
		InputDispatcher.instance().makeHandler (false,
                input,
                printStream,
                null);
		InputDispatcher.instance().dispatchOneInput();
		String result = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
		printStream.close();

		System.out.println(result);
		assert(result.trim().equals("> 0"));
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Test
	public void testEvalAdd() throws UnsupportedEncodingException {
		String command = "eval 1 + 3";
		InputStream  input = new ByteArrayInputStream(Charset.forName("UTF-16").encode(command).array());
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream, true, "utf-8");
		
		Platform.instance (new PlatformFactory(input,
                printStream,
                null).makePlatform());
		
		InputDispatcher.instance().makeHandler (false,
                input,
                printStream,
                null);
		InputDispatcher.instance().dispatchOneInput();
		
		String result = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
		printStream.close();
		
		System.out.println(result);
		assert(result.trim().equals("> 4"));
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Test
	public void testEvalSubstract() throws UnsupportedEncodingException {
		String command = "eval 5 - 3";
		InputStream  input = new ByteArrayInputStream(Charset.forName("UTF-16").encode(command).array());
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream, true, "utf-8");
		
		Platform.instance (new PlatformFactory(input,
                printStream,
                null).makePlatform());
		
		InputDispatcher.instance().makeHandler (false,
                input,
                printStream,
                null);
		InputDispatcher.instance().dispatchOneInput();
		
		String result = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
		printStream.close();
		
		System.out.println(result);
		assert(result.trim().equals("> 2"));
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Test
	public void testEvalMultiply() throws UnsupportedEncodingException {
		String command = "eval 3 * 2";
		InputStream  input = new ByteArrayInputStream(Charset.forName("UTF-16").encode(command).array());
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream, true, "utf-8");
		
		Platform.instance (new PlatformFactory(input,
                printStream,
                null).makePlatform());
		
		InputDispatcher.instance().makeHandler (false,
                input,
                printStream,
                null);
		InputDispatcher.instance().dispatchOneInput();
		
		String result = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
		printStream.close();
		
		System.out.println(result);
		assert(result.trim().equals("> 6"));
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Test
	public void testEvalDivide() throws UnsupportedEncodingException {
		String command = "eval 4 / 2";
		InputStream  input = new ByteArrayInputStream(Charset.forName("UTF-16").encode(command).array());
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream, true, "utf-8");
		
		Platform.instance (new PlatformFactory(input,
                printStream,
                null).makePlatform());
		
		InputDispatcher.instance().makeHandler (false,
                input,
                printStream,
                null);
		InputDispatcher.instance().dispatchOneInput();
		
		String result = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
		printStream.close();
		
		System.out.println(result);
		assert(result.trim().equals("> 2"));
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Test
	public void testPrint() throws UnsupportedEncodingException {
		String command = "print";
		InputStream  input = new ByteArrayInputStream(Charset.forName("UTF-16").encode(command).array());
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream, true, "utf-8");
		
		Platform.instance (new PlatformFactory(input,
                printStream,
                null).makePlatform());
		
		InputDispatcher.instance().makeHandler (false,
                input,
                printStream,
                null);
		InputDispatcher.instance().dispatchOneInput();
		
		String result = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
		printStream.close();
		
		//System.out.println(result);
		assert(result.trim().equals("> 0"));
	}
}
