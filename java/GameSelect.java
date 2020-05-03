//
// GNU SHOGI DATABASE PROJECT
// GN                      CT
// GN       G.S.D.B.       CT
// GN                      CT
// GNU SHOGI DATABASE PROJECT
//
// Matt Casters : 24/11/1996, first attempt
// Matt Casters : 25/11/1996, added promotion
// Matt Casters : 26/11/1996, added promotion question, some checks, back/forward
//
//   This source falls under the    
// 
//              GNU GENERAL PUBLIC LICENSE
//
//   please check the file COPYING in this directory
//
//

import java.awt.*;
import java.applet.*;
import java.net.URL;
import java.net.Socket;
import java.io.*;

////////////////////////////////////////////////////////////////
//////         Score class                              ////////
////////////////////////////////////////////////////////////////

public class GameSelect extends Frame
{
	public Gsdb gsdb;
	public List games;
	public List keys;
	public Panel buttons;
	public Button ok;
	public Button cancel;
	public Label header;
	public int number;
	public Font fnt;
	public Color col;

	public GameSelect(Gsdb g)
	{
		super("Choose a game");

		gsdb=g;

	        fnt=new Font("Courier",Font.PLAIN,10);

		setLayout(new BorderLayout());

		col=Const.COLOR_BACKGR;
		setBackground(col);

		ok=new Button("Select game");
		ok.setBackground(Const.COLOR_OK);

		cancel=new Button("Hide window");
		cancel.setBackground(Const.COLOR_CANCEL);

		buttons=new Panel();
		buttons.setLayout(new FlowLayout());
		buttons.add(ok);
		buttons.add(cancel);
		buttons.setFont(new Font("Helvetica",Font.BOLD, 14));
		
		header=new Label("Nr     Date         Black Player        White Player        Result   Tournament                    Round");
		header.setBackground(Const.COLOR_HEADER);
		header.setFont(fnt);

		games = new List(10,false);
		games.setBackground(col);
		games.setFont(fnt);
		keys = new List(10,false);

                add("North",header);
                add("Center",games );
		add("South",buttons);

		number=0;
		pack();
		if (gsdb.black_white) resize(560,300); else resize(670,400);
	}

	public void newSelection()
	{
		while(games.countItems()>0)
		{
			games.delItem(0);
		}
		while(keys.countItems()>0)
		{
			keys.delItem(0);
		}
	}

        public void addGame(String g)
        {
		games.addItem(g);
        }

        public void addKey(String g)
        {
		keys.addItem(g);
        }

	public boolean keyExists(String k)
	{
		for (int i=0;i<keys.countItems();i++)
		{
			if (keys.getItem(i).compareTo(k)==0) return true;
		}
		return false;
	}

	public void showGames()
	{
		show();
	}

	public void hideGames()
	{
		hide();
	}

	public boolean action(Event e, Object arg)
        {
		if (e.target == ok)
		{ 
			if (games.getSelectedItem()==null) return true;
			StringBuffer buf=new StringBuffer( games.getSelectedItem() );

			if (buf.length()>3)
			{
				buf.setLength(4);
				number=Integer.parseInt( buf.toString().trim() )-1;
				selectGame( number);
				if (!gsdb.PSN_source) 
				{
					keys.select(number);
					gsdb.client.askGame( keys.getSelectedItem() );
					gsdb.game.begin();
				}
				else
				{
					gsdb.PSN_gameNumber=number;
					gsdb.draw_moves=false;
					gsdb.gametype=gsdb.PSN_gametype[ number ];

					gsdb.newGame();

					gsdb.w_black_player.setText(games.getSelectedItem().substring( 19, 36));
					gsdb.w_white_player.setText(games.getSelectedItem().substring( 39, 56));
					gsdb.w_result      .setText(games.getSelectedItem().substring( 59, 65));
					gsdb.w_tournament  .setText(games.getSelectedItem().substring( 68, 98));
					gsdb.w_round       .setText(games.getSelectedItem().substring(101    ));

					gsdb.dec_frame.decodeGame( gsdb.PSN_games[ number ] );
					gsdb.game.begin();

					gsdb.repaint();
					gsdb.draw_moves=true;
				}
			}
			hideGames();
		}
		else
		if (e.target == cancel || e.id==Event.WINDOW_DESTROY)
		{
			hideGames();
		}

		return true;
	}

	public boolean firstGame()
	{
		number=0;
		selectGame(number);
		StringBuffer buf=new StringBuffer( games.getSelectedItem() );
		if (buf.length()>3)
		{
			buf.setLength(4);
			number=Integer.parseInt( buf.toString().trim() )-1;
			selectGame( number);
			keys.select(number);
			gsdb.client.askGame( keys.getSelectedItem() );
			return true;
		}
		return false;
	}

	public boolean previousGame()
	{
		if (number>0)
		{
			number--;
			selectGame(number);
			StringBuffer buf=new StringBuffer( games.getSelectedItem() );
			if (buf.length()>3)
			{
				buf.setLength(4);
				number=Integer.parseInt( buf.toString().trim() )-1;
				selectGame( number);
				keys.select(number);
				gsdb.client.askGame( keys.getSelectedItem() );
				return true;
			}
		}
		return false;
	}

	public boolean nextGame()
	{
		if (number<games.countItems()-1)
		{
			number++;
			selectGame(number);
			StringBuffer buf=new StringBuffer( games.getSelectedItem() );
			if (buf.length()>3)
			{
				buf.setLength(4);
				number=Integer.parseInt( buf.toString().trim() )-1;
				selectGame( number);
				keys.select(number);
				gsdb.client.askGame( keys.getSelectedItem() );
				return true;
			}
		}
		return false;
	}

	public void selectGame(int nr)
	{
		games.select(nr);
	}
}




