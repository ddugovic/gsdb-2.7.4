
import java.applet.*;
import java.awt.*;
import java.io.*;

class StreamListener extends Thread 
{
    DataInputStream in;
    public Gsdb gsdb;
    public int position;

    public StreamListener(DataInputStream in, Gsdb g) 
    {
        this.in = in;
        this.start();
	gsdb=g;
    }

    public void run() 
    {
        String line;

        try 
	{
            for(;;) 
	    {
                line = in.readLine();
                if (line == null) break;
                if (line.compareTo("GAME EXISTS") == 0)
		{
			if (gsdb.silent_save)
			{
				gsdb.sendGame();
			}
			else
			{
				MyDialog md=new MyDialog(gsdb,"This game allready exists. Do you want to change this game ?",MyDialog.YESNO,MyDialog.OVERWRITE);
				md.show();
			}
		}
                if (line.compareTo("GAME SAVED") == 0)
		{
			gsdb.setMessage("Your game has been stored in the games database.");
			gsdb.game_saved=true;
			if (!gsdb.silent_save)
			{
				MyDialog md=new MyDialog(gsdb,"Your game has been stored in the games database.",MyDialog.OK,MyDialog.NOTHING);
				md.show();
			}
		}
                if (line.compareTo("BITMAP SAVED") == 0)
		{
			line=in.readLine();
			gsdb.setMessage("The file "+line+" has been saved in the server directory.");
			MyDialog md=new MyDialog(gsdb,"The file "+line+" has been saved in the server directory.",MyDialog.OK,MyDialog.NOTHING);
			md.show();
		}
                if (line.compareTo("GAME NOT FOUND") == 0)
		{
			gsdb.setMessage("Your selection could not be found in the database.");
		}
                if (line.compareTo("GAME READ") == 0)
		{ 
			gsdb.setMessage("Reading your game ...");
			// read another line to see what the game is !
			line = in.readLine();
			if (line == null) break;

			position=0;
			StringBuffer game=new StringBuffer("");
			StringBuffer key =new StringBuffer("");

			game.setLength(0);

			readString(line, key                  );
			readString(line, gsdb.game.name       );
			readString(line, gsdb.game.email      );
			readString(line, gsdb.game.country    );
			readString(line, gsdb.game.black_name );
			readString(line, gsdb.game.white_name );
			readString(line, gsdb.game.black_grade);
			readString(line, gsdb.game.white_grade);
			readString(line, gsdb.game.black_elo  );
			readString(line, gsdb.game.white_elo  );
			readString(line, gsdb.game.result     );
			readString(line, gsdb.game.comment    );
			readString(line, gsdb.game.source     );
			readString(line, gsdb.game.tournament );
			readString(line, gsdb.game.date       );
			readString(line, gsdb.game.round      );
			readString(line, gsdb.game.venue      );
			readString(line, gsdb.game.proam      );
			readString(line, gsdb.game.gametype   ); 
				gsdb.gametype=new String(gsdb.game.gametype);
				gsdb.oldtype =new String(gsdb.game.gametype);
			readGame  (line, game           );

			gsdb.convertGame(game.toString());

			gsdb.w_name        .setText(gsdb.game.name        .toString());
			gsdb.w_email       .setText(gsdb.game.email       .toString());
			gsdb.w_country     .setText(gsdb.game.country     .toString());
			gsdb.w_black_player.setText(gsdb.game.black_name  .toString());
			gsdb.w_white_player.setText(gsdb.game.white_name  .toString());
			gsdb.w_black_grade .select (gsdb.game.black_grade .toString());
			gsdb.w_white_grade .select (gsdb.game.white_grade .toString());
			gsdb.w_black_elo   .setText(gsdb.game.black_elo   .toString());
			gsdb.w_white_elo   .setText(gsdb.game.white_elo   .toString());
			gsdb.w_result      .setText(gsdb.game.result      .toString());
			gsdb.w_comment     .setText(gsdb.game.comment     .toString());
			gsdb.w_source      .setText(gsdb.game.source      .toString());
			gsdb.w_tournament  .setText(gsdb.game.tournament  .toString());
			//gsdb.w_date        .setText(gsdb.game.date        .toString());
			//System.out.println("Cent.:"+ gsdb.game.date.toString().substring(0,2));
			//System.out.println("Year :"+ gsdb.game.date.toString().substring(2,4));
			//System.out.println("Month:"+ gsdb.game.date.toString().substring(4,6));
			//System.out.println("Day  :"+ gsdb.game.date.toString().substring(6));
			gsdb.data_frame.century.select( gsdb.game.date.toString().substring(0,2));
			gsdb.data_frame.year   .select( gsdb.game.date.toString().substring(2,4));
			gsdb.data_frame.month  .select( gsdb.game.date.toString().substring(4,6));
			gsdb.data_frame.day    .select( gsdb.game.date.toString().substring(6));
			gsdb.w_round       .setText(gsdb.game.round       .toString());
			gsdb.w_venue       .setText(gsdb.game.venue       .toString());
			gsdb.w_proam       .select (gsdb.game.proam       .toString());
			
			gsdb.draw_moves=false; gsdb.game.begin(); gsdb.draw_moves=true;

			gsdb.repaint();
			gsdb.setMessage("The game has been read ("+gsdb.game.size()+" moves)");
			gsdb.game_loaded=true;
		}
                if (line.compareTo("GAME LIST") == 0)
		{
			line = in.readLine();
			int querysize = Integer.parseInt(line);

			StringBuffer fkey = new StringBuffer("");
			StringBuffer fdate = new StringBuffer("");
			StringBuffer fblack_name = new StringBuffer("");
			StringBuffer fwhite_name = new StringBuffer("");
			StringBuffer fresult = new StringBuffer("");
			StringBuffer ftournament = new StringBuffer("");
			StringBuffer fround = new StringBuffer("");

			if (gsdb.keep_selection==false) gsdb.select_frame.newSelection();
			gsdb.PSN_source=false;
			boolean add;
			
			int i=gsdb.select_frame.keys.countItems();
			for (int j=0;j<querysize;j++)
			{
				line = in.readLine();
				position=0;

				readString(line, fkey       );
				readString(line, fdate      );
				if (fdate.length()==8) { fdate.insert(4,'/'); fdate.insert(7,'/'); } else { fdate.setLength(10); }
				readString(line, fblack_name); while (fblack_name.length()<17) fblack_name.append(" "); fblack_name.setLength(17);
				readString(line, fwhite_name); while (fwhite_name.length()<17) fwhite_name.append(" "); fwhite_name.setLength(17);
				readString(line, fresult    ); while (fresult    .length()< 6) fresult    .append(" "); fresult    .setLength( 6);
				readString(line, ftournament); while (ftournament.length()<30) ftournament.append(" "); ftournament.setLength(30);
				readString(line, fround     );

				add=!gsdb.keep_selection;
				if (gsdb.keep_selection) add=!gsdb.select_frame.keyExists(fkey.toString());
				
				if (add)
				{
					gsdb.select_frame.addGame((i<9?"   ":i<99?"  ":i<999?" ":"")+(i+1)+" | "+fdate+" | "+fblack_name+" | "+fwhite_name+" | "+fresult+" | "+ftournament+" | "+fround);
					gsdb.select_frame.addKey(fkey.toString());
					i++;
				}
			}
			gsdb.select_frame.showGames();
			gsdb.setMessage("Select a game from the list...("+querysize+" games)");
		}
            }
        }
        catch (IOException e) 
	{ 
		System.out.println("ERROR : "+e.toString()); 
	}
        finally 
	{ 
		System.out.println("Connection closed by server."); 
		gsdb.connected=false;
		gsdb.menufileconnect.enable();
		gsdb.menufilesend.disable();
		gsdb.menufilebitmap.disable();
		gsdb.menuwindowsearch.disable();
		gsdb.menusearch.disable();
	}
    }

    public void readString(String in, StringBuffer s)
    {
        s.setLength(0);

        while ( position<in.length() && in.indexOf(Const.FSEP, position) != position)
        {
		if ((char)in.charAt(position)<' ')
			s.append(' ');
		else
			s.append((char)in.charAt(position));
                position++;
        }
	position++;

	String tr=s.toString().trim();
	s=new StringBuffer( tr );
	
    }

    public void readGame(String in, StringBuffer g)
    {
        int max=in.length();

        while(position<max)
        {
                g.append((char)in.charAt(position));
                position++;
        }
    }

}


