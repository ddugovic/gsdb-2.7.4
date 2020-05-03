import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class ListRecords
{
	public static void main( String[] args ) 
	{
		PrintStream out = System.out;
		Database games = null;
		try 
		{ 
			games    = new Database( args[0] ); 
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		games.setComparator( new WildCard() );

		Vector v = games.get("round", "*");
		Enumeration e=v.elements();
		//System.out.println(""+v.size());

		String KSEP="\t";
		Record f;

		// simple ASCII-file, tab-delimeted : tournament, round,
		//   date, black_name, white_name, result, game
		// a \n after each line
		while (e.hasMoreElements())
		{
			f = (Record)e.nextElement();
			String key        =(String)(f.get("key"        ));
			String name       =(String)(f.get("name"       ));
			String email      =(String)(f.get("email"      ));
			String country    =(String)(f.get("country"    ));
			String black_name =(String)(f.get("black_name" ));
			String white_name =(String)(f.get("white_name" ));
			String black_grade=(String)(f.get("black_grade"));
			String white_grade=(String)(f.get("white_grade"));
			String black_elo  =(String)(f.get("black_elo"  ));
			String white_elo  =(String)(f.get("white_elo"  ));
			String result     =(String)(f.get("result"     ));
			String comment    =(String)(f.get("comment"    ));
			String source     =(String)(f.get("source"     ));
			String tournament =(String)(f.get("tournament" ));
			String date       =(String)(f.get("date"       ));
			String round      =(String)(f.get("round"      ));
			String venue      =(String)(f.get("venue"      ));
			String proam      =(String)(f.get("proam"      ));
			String gametype   =(String)(f.get("gametype"   ));
			String game       =(String)(f.get("game"       ));

			System.out.println(
				"[Name \""+name       +"\"]\n"+
				"[Email \""+email      +"\"]\n"+
				"[Country \""+country    +"\"]\n"+
				"[Sente \""+black_name +"\"]\n"+
				"[Gote \""+white_name +"\"]\n"+
				"[Black_grade \""+black_grade+"\"]\n"+
				"[White_grade \""+white_grade+"\"]\n"+
				"[Result \""+result     +"\"]\n"+
				"[Comment \""+comment    +"\"]\n"+
				"[Source \""+source     +"\"]\n"+
				"[Event \""+tournament +"\"]\n"+
				"[Date \""+date       +"\"]\n"+
				"[Round \""+round      +"\"]\n"+
				"[Venue \""+venue      +"\"]\n"+
				"[Proam \""+proam      +"\"]\n"+
				game +"\n" );
		} 
		try { games.unlock(); }
		catch(Exception ex) { out.println("ERROR : "+ex.toString()); }
	}
}

