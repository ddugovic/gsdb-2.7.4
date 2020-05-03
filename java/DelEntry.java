import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class DelEntry 
{
	public static void main( String[] args ) 
	{

		if (args[0] == null)
		{
			System.out.println("\nDelEntry, usage : java DelEntry <gamekey>\n");
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
		Index Ikey       =new LinearIndex("key"       , new Equality());
		Index Itournament=new LinearIndex("tournament", new WildCard());

		games.addIndex( Ikey );
		games.addIndex( Itournament );
		games.setComparator( new WildCard() );

		PrintStream out = System.out;

		Record f=games.first("key",args[0]);

		if (f==null)
		{
			System.out.println("ERROR : key ["+args[0]+"] was not found !");
		}
		else
		{
			boolean rm=games.remove(f);
			try { games.store(); } catch(Exception e) { out.println("ERROR : "+e.toString()); }
			if (rm) System.out.println("game with key ["+args[0]+"] has been removed !");
			else
			        System.out.println("game with key ["+args[0]+"] has NOT been removed !");
		}
		try { games.unlock(); }
		catch(Exception e) { out.println("ERROR : "+e.toString()); }
	}
}
