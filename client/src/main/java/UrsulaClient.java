
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrsulaClient {
	private static final Logger logger = LoggerFactory.getLogger( UrsulaClient.class );

	Scanner input;
	static HttpClient client;
	final String urlPrefix = "http://localhost:8080";
    static HttpResponse response;

    String do_get(String s) throws Exception {
    	response=client.execute(new HttpGet(urlPrefix+s));
    	String ress = EntityUtils.toString(response.getEntity());
    	if(response.getStatusLine().getStatusCode() > 299) { 
    		throw new Exception("Something went wrong (" + 
    				response.getStatusLine().getStatusCode() + "): "+ ress);
    	}
    	return ress;
    }
    String do_post(String s, String json)  throws Exception {
    	HttpPost hh = new HttpPost(urlPrefix+s);
    	hh.addHeader( "Content-Type", "application/json" );
    	hh.setEntity( new StringEntity( json ) );
    	HttpResponse response=client.execute(hh);
    	String ress = EntityUtils.toString(response.getEntity());
    	if(response.getStatusLine().getStatusCode() > 299) { 
    		throw new Exception("Something went wrong (" + 
    				response.getStatusLine().getStatusCode() + "): "+ ress);
    	}
    	return ress;
    }


    
	private class Option {
		String optname;
		Menu nextmenu;
		String url;
		List<String> args;
		
		public Option(String oname,Menu nm, String u, String... opts) {
			optname=oname;
			nextmenu=nm;
			url=u;
			args = new ArrayList<String>();
			for (int i = 0; i < opts.length; i++) args.add( opts[i] );
		}
	}
	private class Menu {
		String menuname;
		String startUrl=null;
		List<Option> options;
		public Menu(String s) {
			menuname = s;
			options = new ArrayList<Option>(10);
		}
		public Menu(String s, String u) {
			menuname = s;
			startUrl = u;
			options = new ArrayList<Option>(10);
		}
		void add(Option o) {
			options.add(o);
		}
		void display(String url, Map<String,String> args) {
			while(true) {
			System.out.println("SCREEN: " + menuname);
			System.out.println("=======================");
			String returned = null;
			String posted = null;
			if(url != null) {
				boolean post_mode = false;
				StringBuffer post_args = new StringBuffer("");
				if(url.startsWith( "P" )) {
					post_mode=true;
					url=url.substring( 1 );
				};
				for(String key: args.keySet()) {
					String value=args.get( key );
					if(url.contains( "/%"+key+"%" )) url=url.replace( "%"+key+"%", value );
					else if(!key.startsWith( "@" )) {
						post_mode=true;
						String jsonvalue;
						jsonvalue="\""+value+"\"";
						if(key.startsWith( "S" )) key=key.substring( 1 );
						else {
							try {
								Integer.parseInt( value );
								jsonvalue=value;
							} catch(NumberFormatException e) { ; }
						}
						if(post_args.length() != 0) post_args.append( ", " ); 
						post_args.append("\""+key+"\":"+jsonvalue);
					}
					if(post_args.length() > 0) {
					   	posted='{'+ post_args.toString() + '}';
					}
				}
				try {
					if(url.contains( "/%" )) throw new Exception("Unresolved wildcards in URL: " + url);
					if(post_mode) {
						System.out.println( "Posting: " + posted);
						returned = do_post(url, posted);
					}
					else returned = do_get(url);
			
				}
				catch(Exception e) {
					System.out.println( "Error:" + e);
					new Scanner(System.in).nextLine();
					return;
				}
			}
			if(returned != null) {
				returned.replace( "}", "}\n" );
			    System.out.println("RESULTS \n"+returned);
				System.out.println("=======================");
			}
			System.out.println("OPTIONS:");
			int n=1;
			for(Option o: options) {
				System.out.print(n++ +": "+o.optname +"(");
				for(String s:o.args) {
					System.out.print(s+",");
				}
				System.out.print("), ");
			}
			System.out.println("X: Return");
			try {
				String command=input.nextLine();
				StringTokenizer t = new StringTokenizer( command," ");
				String chead = t.nextToken();
				if(chead.equals( "X" )) return;
				int sel = Integer.parseInt(chead);
				Option selcmd = options.get( sel-1 );
				Map<String, String> argmap= new HashMap<String, String>();
				if(args != null) argmap.putAll( args );
				for(String a : selcmd.args ) {
					if(a.startsWith("@")) {
						// String realkey = a.substring( 1 );
						argmap.put( a, args.get( a ));
					}
					else argmap.put( a,  t.nextToken() );
				}
				if(t.hasMoreTokens()) throw new Exception("Too many args");
				selcmd.nextmenu.display( selcmd.url, argmap );
			}	
			catch(Exception e) {
				System.out.println("Input error:" + e.getMessage());
			}
		  }
		}
	}
	private class SelectorMenu extends Menu {
		Map<String, Menu> dispatch = new HashMap<String, Menu>();
		Map<String, String> dispatchvar = new HashMap<String, String>();
		public SelectorMenu( String s ) {
			super( s );
		}
		/**
		 * Add mapping
		 * @param m1
		 * @param m2
		 * @param idn an optional variable which will receive the value of the login user ID
		 */
		public void add(String m1, Menu m2, String idn) {
			dispatch.put(m1, m2);
			dispatchvar.put(m1, idn);
		}

		@Override
		void display(String url, Map<String,String> args) {
	        HttpPost post = new HttpPost(urlPrefix+"/login");
	        try {
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				for(String key: args.keySet()) {
					nameValuePairs.add(new BasicNameValuePair(key,args.get( key )));
				}
	            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        	HttpResponse response=client.execute(post);
	        	String ress = EntityUtils.toString(response.getEntity());
	        	System.out.println( "Redirect to: "+response.getFirstHeader( "Location" ).getValue() );
	        	if(response.getStatusLine().getStatusCode() > 320 ) {
	        		throw new Exception("Something went wrong (" + 
	        				response.getStatusLine().getStatusCode() + "): "+ ress);
	        		
	        	}
            String resp = do_get("/user/self");
            System.out.println("Self ret:" + resp);
            int l = resp.indexOf("\"id\":\"");
        	int uid=-1;
            if(l >= 0) {
            	String uidstr=resp.substring( l+6);
            	String uidname=uidstr.substring( 0, uidstr.indexOf( '"' ) );
            	uid=Integer.parseInt( uidname );
            }
            l = resp.indexOf("\"type\":\"");
            if(l >= 0) {
            	String typstr=resp.substring( l+8);
            	String typename=typstr.substring( 0, typstr.indexOf( '"' ) );
            	for(String k : dispatch.keySet()) {
            		if(k.equals( typename )) {
            			Menu m = dispatch.get(k);
            			Map<String,String> nargs = new HashMap<String,String>();
            			if(dispatchvar.get( k ) != null && uid >= 0) {
            				nargs.put( "@" + dispatchvar.get(k), Integer.toString(uid));
            			}
            			m.display( m.startUrl, nargs );
            			return;
            		}
            	}
            	System.out.println( "ERROR: no dispatch for type: " + typename );
            	input.nextLine();
        		return;
            }
        } catch(Exception e) { return; }
		}
	}
	
	public UrsulaClient () { 
    	try {
    	input = new Scanner(System.in);
		client = HttpClientBuilder.create().build();
		Menu loginmenu = new Menu("BEJELENTKEZÉS");
		SelectorMenu mainmenudispatch = new SelectorMenu(""); 
		Menu betegmmenu = new Menu("BETEG FŐMENU", "/beteg/self");
		Menu betegesetmenu = new Menu("ESETEK LISTÁJA");
		Menu betegkezmenu = new Menu("ELŐJEGYZETT KEZELÉSEK LISTÁJA");
		Menu passwordmenu = new Menu("PASSWORD VÁLTOZATÁS MEGTÖRTÉNT");
		Menu okmenu = new Menu("MŰVELET MEGTÖRTÉNT");
		
		
		Menu adminmmenu = new Menu("ADMIN FŐMENÜ");
		
		Menu recepcmmenu = new Menu("RECEPCIOS FŐMENÜ");
		
		mainmenudispatch.add("BETEG", betegmmenu,"TAJ");
		mainmenudispatch.add("RECEPCIO", recepcmmenu,null);
		mainmenudispatch.add("ORVOS", betegmmenu,"ELID");
		mainmenudispatch.add("LABOR", betegmmenu,"ELID");
		mainmenudispatch.add("ADMIN", adminmmenu,null);

	
		loginmenu.add(new Option("Bejelentkezés", mainmenudispatch, "/login", "username", "password"));
	
		betegmmenu.add(new Option("Kilep", loginmenu, "/logout"));
		betegmmenu.add(new Option("Esetek", betegesetmenu, "/beteg/%@TAJ%/esetek"));
		betegmmenu.add(new Option("Függő kezelések", betegkezmenu, "/beteg/%@TAJ%/fuggokezelesek"));
		betegmmenu.add(new Option("Passwd vált.", passwordmenu, "/user/password", "new_pass"));

		adminmmenu.add(new Option("Új osztaly", okmenu, "/ellato/osztaly/new", "nev"));
		adminmmenu.add(new Option("Új orvos", okmenu, "/ellato/orvos/new", "nev", "osztaly"));
		adminmmenu.add(new Option("Új labor", okmenu, "/ellato/labor/new", "nev", "Stelefon"));
		adminmmenu.add(new Option("Új user", okmenu, "/user/new", "username", "password", "type", "userid"));
		adminmmenu.add(new Option("Passwd vált.", passwordmenu, "/user/password/%userid%", "userid", "new_pass"));
		loginmenu.display( null, null );
		System.out.println("Exiting UrsulaClient...");
    	} catch (Exception e) {
    		System.err.println( "Error occurred:" + e.getMessage() );
            e.printStackTrace();
        }
	}
    
    /*
	void do_loginpost(String s, Map<String,String> args)  throws Exception  {
		;
	}*/

    public static void main(String[] args) {
    	new UrsulaClient();
	}
    
}
