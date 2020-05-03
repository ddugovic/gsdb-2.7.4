import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class ExportRecords
{
	public StringBuffer sb=new StringBuffer();

	public static void main( String[] args ) 
	{
		PrintStream out = System.out;
		Database games = null;
		try 
		{ 
			games    = new Database( "games"); 
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		games.setComparator( new WildCard() );

		Vector v = games.get("tournament", "*");
		Enumeration e=v.elements();

		String SEP=", ";
		Record f;

		// simple ASCII-file, ','-delimeted 
		//   with " " around each field...
		//   with a \n after each line
		while (e.hasMoreElements())
		{
			f = (Record)e.nextElement();
			String key        ="\""+((String)(f.get("key"        ))).replace('\"','\'')+"\"";
			String name       ="\""+((String)(f.get("name"       ))).replace('\"','\'')+"\"";
			String email      ="\""+((String)(f.get("email"      ))).replace('\"','\'')+"\"";
			String country    ="\""+((String)(f.get("country"    ))).replace('\"','\'')+"\"";
			String black_name ="\""+((String)(f.get("black_name" ))).replace('\"','\'')+"\"";
			String white_name ="\""+((String)(f.get("white_name" ))).replace('\"','\'')+"\"";
			String black_grade="\""+((String)(f.get("black_grade"))).replace('\"','\'')+"\"";
			String white_grade="\""+((String)(f.get("white_grade"))).replace('\"','\'')+"\"";
			String black_elo  ="\""+((String)(f.get("black_elo"  ))).replace('\"','\'')+"\"";
			String white_elo  ="\""+((String)(f.get("white_elo"  ))).replace('\"','\'')+"\"";
			String result     ="\""+((String)(f.get("result"     ))).replace('\"','\'')+"\"";
			String comment    ="\""+((String)(f.get("comment"    ))).replace('\"','\'')+"\"";
			String source     ="\""+((String)(f.get("source"     ))).replace('\"','\'')+"\"";
			String tournament ="\""+((String)(f.get("tournament" ))).replace('\"','\'')+"\"";
			String date       ="\""+((String)(f.get("date"       ))).replace('\"','\'')+"\"";
			String round      ="\""+((String)(f.get("round"      ))).replace('\"','\'')+"\"";
			String venue      ="\""+((String)(f.get("venue"      ))).replace('\"','\'')+"\"";
			String proam      ="\""+((String)(f.get("proam"      ))).replace('\"','\'')+"\"";
			String gametype   ="\""+((String)(f.get("gametype"   ))).replace('\"','\'')+"\"";
			String game       ="\""+((String)(f.get("game"       ))).replace('\"','\'')+"\"";
			String updated    ="\""+((String)(f.get("updated"    ))).replace('\"','\'')+"\"";

			System.out.println(
				key         + SEP +
				name        + SEP +
				email       + SEP +
				country     + SEP +
				black_name  + SEP +
				white_name  + SEP +
				black_grade + SEP +
				white_grade + SEP +
				black_elo   + SEP +
				white_elo   + SEP +
				result      + SEP +
				comment     + SEP +
				source      + SEP +
				tournament  + SEP +
				date        + SEP +
				round       + SEP +
				venue       + SEP +
				proam       + SEP +
				gametype    + SEP +
				game        + SEP +
				updated);
		} 
		try { games.unlock(); }
		catch(Exception ex) { out.println("ERROR : "+ex.toString()); }
	}
}

