
import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class ChangeField 
{
	public static void main( String[] args ) 
	{
		Database games = null;

		if (args.length == 0)
		{
			System.out.println("\nChangeField : java ChangeField <Field1> <Search String> <Field2> <New Value>");
			System.out.println("Example     : java ChangeField black_grade Mejin black_grade Meijin\n");
			return;
		}

		try 
		{ 
			games = new Database( "games"); 
		}
		catch (IOException e) { e.printStackTrace(); return; }

		games.setComparator( new WildCard() ); 

		PrintStream out = System.out;

		Vector v = games.get(args[0],args[1]);
		for (Enumeration e=v.elements();e.hasMoreElements();)
		{
			Record r1 = (Record)e.nextElement();
			Record r2 = (Record)r1.clone();

			String k=(String)r1.get("key");
			r2.put(args[2],args[3]);
			if (games.replace(r1,r2))
			{
				System.out.println("Changed record ["+k+"] --> ["+args[2]+"]=["+args[3]+"]");
			}
			else
			{
				System.out.println("ERROR : couldn't change record ["+k+"] --> ["+args[2]+"]=["+args[3]+"]");
			}
		} 
		try { games.close(); } catch(Exception e) { out.println("ERROR : "+e.toString()); }
	}
}
