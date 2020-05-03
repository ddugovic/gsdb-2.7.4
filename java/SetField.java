import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class SetField 
{
	public static void main( String[] args ) 
	{
		if (args.length == 0)
		{
			System.out.println("\nDelEntry, usage : java SetField <gamekey> ...\n");
		}
		else
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

			Record g=games.first("key",args[0]);
			Record f=games.first("key",args[0]);

			if (f==null)
			{
				System.out.println("ERROR : key ["+args[0]+"] was not found !");
			}
			else
			{
				f.put(args[1],args[2]);

				if (games.replace(g,f))
				{
					System.out.println("["+args[1]+"] replaced with ["+args[2]+"]");
				}
				else
				{
					System.out.println("Error replacing field");
				}
				try { games.store(); } catch(Exception e) { out.println("ERROR : "+e.toString()); }
			}
			try { games.unlock(); }
			catch(Exception e) { out.println("ERROR : "+e.toString()); }
		}
	}
}
