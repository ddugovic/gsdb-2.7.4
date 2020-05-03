
import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class ToHTML
{
	public Key Ikey, Itournament;

	public static void main( String[] args ) 
	{
		DataInputStream stdin=new DataInputStream(System.in);
		String s=new String();
		
		try { s=stdin.readLine(); }
			
		catch (Exception e) { System.out.println("ERROR: "+e.toString()); }
		//System.out.println("["+s+"]<p>");
		
		Database games = null;
		try 
		{ 
			games = new Database( "html_games"); 
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

		Key key=games.getKeys();
		String quote=new String("\"");

		char col=s.charAt(6);
		//System.out.println("col="+col+"<p>");

		Record r = games.first("key",s.substring(12,s.length()));
		if (r!=null)
		{
			System.out.print("<p><applet code="+quote+"Gsdb.class"+quote+" codebase="+quote+"/shogi/gsdb/java/client/"+quote+" Name="+quote+"G.S.D.B."+quote);
			if (col=='B')
			{
				System.out.println("  width=500 height=330>");
				System.out.println("<param name="+quote+"SmallBoard"+quote+" value="+quote+"true"+quote+">");
			}
			else
			{
				System.out.println("  width=620 height=410>");
			}
			System.out.println("<param name="+quote+"noServer"+quote+" value="+quote+"true"+quote+">");
			System.out.println("<param name="+quote+"PicBase"+quote+" value="+quote+"/shogi/gsdb/java/client/"+quote+">");

			for (Enumeration g=key.elements(); g.hasMoreElements();) 
			{
				String field = (String)g.nextElement();
				StringBuffer val2=new StringBuffer((String)r.get(field));

				if (field.equals("black_name")) field=new String("black_player"); //Sorry, my mistake !
				if (field.equals("white_name")) field=new String("white_player"); //Sorry, my mistake !
				if (!field.equals("key"))
				{
					System.out.print("<param name="+quote+field+quote+" value="+quote+val2+quote+">");
				}
			}
			System.out.println("</applet>");
		}
		else
		{
			System.out.println("<b>This record could not be found in the database !</b>");
		}
		try { games.unlock(); }
		catch(Exception e) { System.out.println("ERROR : "+e.toString()); }

	}
}
