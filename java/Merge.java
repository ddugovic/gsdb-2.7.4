import jDB.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

public class Merge 
{
	public static void main( String[] args ) 
	{
		if (args.length!=3)
		{
			System.out.println("Usage: Merge <original database> <records to add> <resulting database>");
			return;
		}
		PrintStream out = System.out;

		Database org = null;
		Database add = null;
		Database tot = null;
		try 
		{ 
			org   = new Database( args[0]); 
			add   = new Database( args[1]); 
			tot   = new Database( args[2]); 
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}

		Index Ikey=new LinearIndex("key", new Equality());
		Index Itournament=new LinearIndex("tournament", new WildCard());
		Ikey.setComparator( new WildCard() );
		Itournament.setComparator( new WildCard() );

		org.addIndex( Ikey ); org.setComparator( new WildCard() );
		add.addIndex( Ikey ); add.setComparator( new WildCard() );
		tot.addIndex( Ikey ); tot.setComparator( new WildCard() );

		out.println("------------------------------------------");
		out.println("Database size="+org.size());
		out.println("Add db   size="+add.size());
		out.println("------------------------------------------");

		Vector o = org.get("date","*");
		int i=0;
		for (Enumeration e=o.elements();e.hasMoreElements();)
		{
		 Record r = (Record)e.nextElement();
		 String ks= (String)r.get("key");
		 out.println("adding record with key ["+ks+"]");

		 tot.add(r);
 		 i++;
		} 
		out.println("added "+i+" records tot games.");

		Vector a = add.get("date","*");
		out.println("found "+a.size()+" record with in add -->Vector");
		out.println("------------------------------------------");
		for (Enumeration e=a.elements();e.hasMoreElements();)
		{
		 Record r = (Record)e.nextElement();
		 String ks= (String)r.get("key");
		 out.print("Checking record with key ["+ks+"]");
		 Record check=new Record();
		 check.put("key",ks);
		 Record look=tot.first( check );
		 if (look==null)
		 {
		 	tot.add(r);
			out.println(" : One more record !");
		 }
		 else out.println(".");
		}

		out.println("org : "+org.toString());
		out.println("add : "+add.toString());
		out.println("tot : "+tot.toString());

		out.println("------------------------------------------");
		out.println("total size="+tot.size());
		out.println("------------------------------------------");

		try{tot.close();}catch(Exception e){out.println(e.toString());}
		try{add.close();}catch(Exception e){out.println(e.toString());}
		try{org.close();}catch(Exception e){out.println(e.toString());}
	}
}

