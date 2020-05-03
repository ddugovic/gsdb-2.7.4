import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class ListGames 
{
	public static void main( String[] args ) 
	{
		Database games = null;
		try 
		{ 
			games = new Database("tm"); 
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}

		Index Ikey=new LinearIndex("key", new Equality());
		Index Itournament=new LinearIndex("tournament", new WildCard());

		games.addIndex( Ikey );
		games.addIndex( Itournament );
		games.setComparator( new WildCard() );

		PrintStream out = System.out;

		Record look=new Record();
		look.put("round","*");

		Vector v = games.get(look);
		for (Enumeration e=v.elements();e.hasMoreElements();)
		{
		 Record r = (Record)e.nextElement();

		 StringBuffer val1,val2,val3,val4,val5;
		 val1=new StringBuffer((String)r.get("tournament"));
		 val2=new StringBuffer((String)r.get("black_name"));
		 val3=new StringBuffer((String)r.get("white_name"));
		 val4=new StringBuffer((String)r.get("round"));
		 val5=new StringBuffer((String)r.get("date"));

		 val1.setLength(20); val2.setLength(15); val3.setLength(15);
		 val4.setLength( 2); val5.setLength( 8);

		 System.out.println(val5+"|"+val1+"|"+val4+"|"+val2+" vs "+val3);
		} 
		try { games.unlock(); }
		catch(Exception e) { out.println("ERROR : "+e.toString()); }
	}
}


