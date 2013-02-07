import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;

public class getUrls {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		try {
			if(args.length==0){
				System.out.println("please try again,type in like:java getUrls hashtag");
				System.exit(1);
			}
			String hashTag=args[0];
			Set<String> urls=new HashSet<String>();
			
			int counter=0;
			String nextUrl="http://search.twitter.com/search.json?rpp=100&q=%23"+hashTag;
			Pattern regex1=Pattern.compile("\"next_page\":\"([^\"]*)");
			Pattern regex2=Pattern.compile("\"text\":\"[^\"]*http:\\\\/\\\\/t.co\\\\/([a-zA-Z0-9]{8})");
			
			while(counter<100){
				URL url = new URL(nextUrl);
				URLConnection urlConnection = url.openConnection();

				BufferedReader bsr=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

				int	ch;
				StringBuffer sb=new StringBuffer();
				while((ch=bsr.read())!=-1){
					sb.append((char)ch);
				}
				String content=sb.toString();
				//System.out.println(content);
				
				try {
					Matcher regexMatcher = regex2.matcher(content);
					while (regexMatcher.find()) {
						String newUrl="http://t.co/"+regexMatcher.group(1);
						if(!urls.contains(newUrl)){
							urls.add(newUrl);
							
							counter++;
							if (counter>100) break;
							System.out.println(/*counter+" : "+*/newUrl);
						}
					} 
				} catch (PatternSyntaxException ex) {
					// Syntax error in the regular expression
				}
				
				
				
				String nextPage = null;
				try {

					Matcher regexMatcher = regex1.matcher(content);
					if (regexMatcher.find()) {
						nextPage = regexMatcher.group(1);
					} 
				} catch (PatternSyntaxException ex) {
					// Syntax error in the regular expression
				}
				nextUrl="http://search.twitter.com/search.json"+nextPage;
				//System.out.println(nextUrl);
			}

			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
