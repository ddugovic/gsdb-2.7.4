import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class DelFirst
{
	public static void main( String[] args ) 
	{
		if (args.length<1)
		{
			System.out.println("\nDelEntry, usage : java DelFirst <field> <wildcard>\n");
			return;
		}
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
		games.setComparator( new WildCard() );

		PrintStream out = System.out;

		System.out.println("Looking for ["+args[0]+"]=["+args[1]+"]");
		Record f=games.first(args[0],args[1]);
		System.out.println("Query done.");

		if (f==null)
		{
			out.println("ERROR : ["+args[0]+"]==["+args[1]+"] !");
		}
		else
		{
			String key=(String)f.get("key");
			String black=(String)f.get("black_name");
			String white=(String)f.get("white_name");
			String date=(String)f.get("date");
			out.println("Found record with key ["+key+"], "+black+" vs "+white+", played on "+date);
			out.println("Are you sure you want to delete this record ? (y/n)");
			int in=0;
			boolean rm=false;
			try { in=System.in.read(); } catch(Exception e) { out.println("ERROR : "+e.toString()); }
			if (in==(int)'y' || in==(int)'Y')
			{
				rm=games.remove(f);
				try { games.store(); } catch(Exception e) { out.println("ERROR : "+e.toString()); }
			}
			if (rm) System.out.println("game with key ["+key+"] has been removed !");
			else
			        System.out.println("game with key ["+key+"] has NOT been removed !");
		}
		try { games.unlock(); }
		catch(Exception e) { out.println("ERROR : "+e.toString()); }
	}
}
