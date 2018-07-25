package webpages_parser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App 
{
    
    public static Document connect(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).timeout(0).get();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return doc;
    }
    
    
    public static void main( String[] args ) throws IOException
    {
    	System.out.println("starting to parse text from url ");
		long millis = System.currentTimeMillis();

//        
//        
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        List<Future<Void>> handles = new ArrayList<Future<Void>>();
//        Future<Void> handle;
//        for (int i=0;i < 12; i++) {
//            handle = executorService.submit(new Callable<Void>() {
//
//                public Void call() throws Exception {                    
//                    System.out.println(App.connect("http://www.google.hr").title());
//                    return null;
//                }
//            });
//            handles.add(handle);
//        }
//        
//        for (Future<Void> h : handles) {
//            try {
//                h.get();
//            } 
//            catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        
//        executorService.shutdownNow();
        
	/*
    Collection<Match> matches = new ArrayList<Match>();
    Collection<Future<Match>> results = new ArrayList<Future<Match>>();

    String[] elements = new String[12];
    Arrays.fill(elements, "http://movieweb.com/movie/iron-man/");
    
 for (String element : elements) {
        MatchWorker matchWorker = new MatchWorker(element);
        FutureTask<Match> task = new FutureTask<Match>(matchWorker);
        results.add(task);

        Thread matchThread = new Thread(task);
        matchThread.start();
    }
    for(Future<Match> match : results) {
        try {
            matches.add(match.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    for (Match m : matches) {
        System.out.println(m);
    }
	System.out.println("took " + (millis - System.currentTimeMillis()) +  " ms");
	*/
	
	ExecutorService executorService = Executors.newFixedThreadPool(40);
	List<Future<Match>> handles = new ArrayList<Future<Match>>();
	Future<Match> handle;
	String[] elements = new String[12];
	Arrays.fill(elements, "http://movieweb.com/movie/iron-man/");
	String element = "";
	for (int i=0; i < elements.length; i++) {
		element = elements[i];
		MatchWorker matchWorker = new MatchWorker(element);
		handle = executorService.submit(matchWorker);
		handles.add(handle);
	}
  
	for (Future<Match> h : handles) {
		try {
			h.get();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	executorService.shutdownNow();
	System.out.println("Running parallel took " + (System.currentTimeMillis() - millis) +  " ms");
	
	// vs. run sequentially 
	millis = System.currentTimeMillis();
	for (int i=0; i < elements.length; i++) {
		element = elements[i];
		retrievePage(element);
	}
	System.out.println("Running sequential took " + (System.currentTimeMillis() - millis) +  " ms");
    }
    
    private static Match retrievePage(String element) {
    	Match allTextFromPage = null;

    	String all_paragraphs = "";
        //match = new Match(App.connect(element).title());
        Elements divs = App.connect(element).select("div, p, b");
	      for (Element div : divs) {
	          //System.out.println(div.ownText());
	    	  String new_div = div.ownText();
	          all_paragraphs+=new_div;
	      }
	     allTextFromPage = new Match(all_paragraphs);
	     System.out.println("call in thread finished");
        return allTextFromPage;
    }
}