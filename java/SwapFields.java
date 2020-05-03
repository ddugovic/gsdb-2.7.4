import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class SwapFields 
{
	public static void main( String[] args ) 
	{
		if (args.length == 0)
		{
		 System.out.println("\nusage: java SwapFields <gamekey> ..\n");
		}
		else
		for (int nr=0;nr<args.length;nr++)
		{
			Database games = null;
			try 
			{ 
				games = new Database( "games"); 
			}
			catch (IOException e) 
			{
				e.printStackTrace();
				return;
			}
			//games.addIndex( new LinearIndex(), "key" );
			//Key key=games.getKeys();

			PrintStream out = System.out;

			Record g=games.first("key",args[nr]);
			Record f=games.first("key",args[nr]);

			if (f==null)
			{
			 System.out.println("ERR: ["+args[nr]+"] not found !");
			}
			else
			{
			 StringBuffer swapc;
			 StringBuffer swapg;
			 swapc=new StringBuffer((String)f.get("country"));
			 swapg=new StringBuffer((String)f.get(   "game"));

/*
			 for (int i=0;i<swapc.length();i++) 
				if ((int)swapc.charAt(i) < 4) 
					swapc.setCharAt(i,' ');
			 for (int i=0;i<swapg.length();i++) 
				if ((int)swapg.charAt(i) < 4) 
					swapg.setCharAt(i,' ');
*/
			 f.put("country",swapg.toString());
			 f.put(   "game",swapc.toString());
 
			 games.replace(g,f,true);
			 try { games.store(); } 
			 catch(Exception e) 
			 { 
				out.println("ERROR : "+e.toString()); 
			 }
			 System.out.println("c&g swapped for ["+args[nr]+"]");
			}
			try { games.unlock(); }
			catch(Exception e) 
			{ 
				out.println("ERROR : "+e.toString()); 
			}
		}
	}
}
