
import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class AddFields 
{
	public static void main( String[] args ) 
	{
		Database games = null;
		Database newgames = null;
		Index Ikey, Itournament;

		try 
		{ 
			games = new Database( "games"); 
			newgames = new Database( "newgames"); 
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}

		Ikey=new LinearIndex("key", new Equality());
		Itournament=new LinearIndex("tournament", new WildCard());

		games.addIndex( Ikey );                newgames.addIndex( Ikey );
		games.addIndex( Itournament );         newgames.addIndex( Itournament );
		games.setComparator( new WildCard() ); newgames.setComparator( new WildCard() );

		PrintStream out = System.out;

		Vector v = games.get("date","*");
		for (Enumeration e=v.elements();e.hasMoreElements();)
		{
			Record r1 = (Record)e.nextElement();
			Record r2 = new Record();
			r2.copy(r1);
			r2.put("gametype","E");
			r2.put("proam","pro");

			newgames.add(r2);
		} 
		try { games.close(); } catch(Exception e) { out.println("ERROR : "+e.toString()); }
		try { newgames.close(); } catch(Exception e) { out.println("ERROR : "+e.toString()); }

	}
}
