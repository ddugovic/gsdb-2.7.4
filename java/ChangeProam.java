
import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class ChangeProam 
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
		Key key=games.getKeys();

		Vector v = games.get("date","*");
		for (Enumeration e=v.elements();e.hasMoreElements();)
		{
			Record r1 = (Record)e.nextElement();
			Record r2 = new Record();
			r2.copy(r1);

			String proam=(String)r1.get("proam");
			String black=(String)r1.get("black_name");
			String country=(String)r1.get("country");

			if ( black.equals("Black") ) r2.put("proam","Opening");
			if ( country.equals("The Netherlands") ) r2.put("proam","Amateur");
			if ( country.equals("Belgium") ) r2.put("proam","Amateur");
			if ( country.equals("U.S.") ) r2.put("proam","Amateur");
			newgames.add(r2);
		} 
		try { games.close(); } catch(Exception e) { out.println("ERROR : "+e.toString()); }
		try { newgames.close(); } catch(Exception e) { out.println("ERROR : "+e.toString()); }

	}
}
