
import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class Copy
{
	public static void main( String[] args ) 
	{
		PrintStream out = System.out;
		Database games = null;
		Database copy  = null;
		out.println("Start database copy");

		try { copy     = new Database( "copy" ); } catch (IOException e) { e.printStackTrace(); return; }
		try { games    = new Database( "games"); } catch (IOException e) { e.printStackTrace(); return; }

		out.println("Databases are open");

		games.setComparator( new WildCard() );
		copy .setComparator( new WildCard() );

		out.println("Comparator is WildCard");

		System.out.print("Searching for records..."); out.flush();
		Vector v = games.get("round","*");
		System.out.println("Vector received");
		Enumeration e=v.elements();
		System.out.println("Elements in vector");

		int n=1;
		while (e.hasMoreElements())
		{
			out.println("copy record "+n); n++;
			Record r = (Record)e.nextElement();
			copy.add(r);
		} 
		try { games.close(); } catch(Exception ex) { out.println("ERROR : "+ex.toString()); }
		try { copy .close(); } catch(Exception ex) { out.println("ERROR : "+ex.toString()); }
	}
}

