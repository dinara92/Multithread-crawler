package webpages_parser;
import java.io.IOException;
import java.util.logging.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
public class WebPageParser {

	

	
public static void getPageText(String url){
	
     String all_paragraphs = "";

	 try {
	      
	      Document doc = Jsoup.connect(url).timeout(0).get();
	      Elements divs = doc.select("div, p, b"); 
	      for (Element div : divs) {
	          System.out.println(div.ownText());
	    	  String new_div = div.ownText();
	          all_paragraphs+=new_div;
	      }
          System.out.println(all_paragraphs);
	      }
	    catch (IOException ex) {
	      Logger.getLogger(WebPageParser.class.getName())
	            .log(Level.SEVERE, null, ex);
	    }
	  }
	

	
public static void main(String[] args)  {
	
	String url = "http://www.cinemablend.com/previews/Iron-Man-1877.html";
	getPageText(url);
	
	
	
}
}