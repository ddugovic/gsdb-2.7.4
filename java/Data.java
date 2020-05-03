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
//////         Data class                               ////////
////////////////////////////////////////////////////////////////

public class Data extends Frame
{
	public Gsdb  gsdb;
	public Font  fnt;
	public Color col;
	public Choice century;
	public Choice year;
	public Choice month;
	public Choice day;
	public Panel date_panel;

	public Data(Gsdb g)
	{
		super("GSDB Info Screen");

		gsdb=g;

	        if (gsdb.black_white) fnt=new Font("Helvetica",Font.BOLD,10); 
		else                  fnt=new Font("Helvetica",Font.BOLD,12);

		setLayout(new BorderLayout());

		col=Const.COLOR_BACKGR;
                setBackground(col);

                gsdb.w_name          = new TextField(50); gsdb.w_name          .setFont(fnt); gsdb.w_name          .setBackground(col);
                gsdb.w_email         = new TextField(50); gsdb.w_email         .setFont(fnt); gsdb.w_email         .setBackground(col);
                gsdb.w_country       = new TextField(50); gsdb.w_country       .setFont(fnt); gsdb.w_country       .setBackground(col);
                gsdb.w_black_player  = new TextField(50); gsdb.w_black_player  .setFont(fnt); gsdb.w_black_player  .setBackground(col);
                gsdb.w_white_player  = new TextField(50); gsdb.w_white_player  .setFont(fnt); gsdb.w_white_player  .setBackground(col);
                gsdb.w_black_grade   = new Choice();      gsdb.w_black_grade   .setFont(fnt); gsdb.w_black_grade   .setBackground(col);
                gsdb.w_white_grade   = new Choice();      gsdb.w_white_grade   .setFont(fnt); gsdb.w_white_grade   .setBackground(col);
                gsdb.w_black_elo     = new TextField(50); gsdb.w_black_elo     .setFont(fnt); gsdb.w_black_elo     .setBackground(col);
                gsdb.w_white_elo     = new TextField(50); gsdb.w_white_elo     .setFont(fnt); gsdb.w_white_elo     .setBackground(col);
                gsdb.w_result        = new TextField(50); gsdb.w_result        .setFont(fnt); gsdb.w_result        .setBackground(col);
                gsdb.w_comment       = new TextField(50); gsdb.w_comment       .setFont(fnt); gsdb.w_comment       .setBackground(col);
                gsdb.w_source        = new TextField(50); gsdb.w_source        .setFont(fnt); gsdb.w_source        .setBackground(col);
                gsdb.w_tournament    = new TextField(50); gsdb.w_tournament    .setFont(fnt); gsdb.w_tournament    .setBackground(col);
                gsdb.w_date          = new TextField(50); gsdb.w_date          .setFont(fnt); gsdb.w_date          .setBackground(col);
                gsdb.w_round         = new TextField(50); gsdb.w_round         .setFont(fnt); gsdb.w_round         .setBackground(col);
                gsdb.w_venue         = new TextField(50); gsdb.w_venue         .setFont(fnt); gsdb.w_venue         .setBackground(col);
                gsdb.w_proam         = new Choice();      gsdb.w_proam         .setFont(fnt); gsdb.w_proam         .setBackground(col);

		century=new Choice();
		year   =new Choice();
		month  =new Choice();
		day    =new Choice();

		century.setBackground(Const.COLOR_BACKGR);
		year   .setBackground(Const.COLOR_BACKGR);
		month  .setBackground(Const.COLOR_BACKGR);
		day    .setBackground(Const.COLOR_BACKGR);

		century.addItem("  "); century.addItem("14"); century.addItem("15"); century.addItem("16");
		century.addItem("17"); century.addItem("18"); century.addItem("19"); century.addItem("20");
		century.select("19");

		year.addItem("  ");
		year.addItem("00"); year.addItem("01"); year.addItem("02"); year.addItem("03"); year.addItem("04");
		year.addItem("05"); year.addItem("06"); year.addItem("07"); year.addItem("08"); year.addItem("09");
		for (int i=10;i<100;i++) year.addItem(""+i);
		year.select("98");

		month.addItem("  ");
		month.addItem("01"); month.addItem("02"); month.addItem("03"); month.addItem("04"); month.addItem("05"); month.addItem("06");
		month.addItem("07"); month.addItem("08"); month.addItem("09"); month.addItem("10"); month.addItem("11"); month.addItem("12");
		month.select("  ");

		day.addItem("  ");
		day.addItem("00"); day.addItem("01"); day.addItem("02"); day.addItem("03"); day.addItem("04");
		day.addItem("05"); day.addItem("06"); day.addItem("07"); day.addItem("08"); day.addItem("09");
		for (int i=10;i<32;i++) day.addItem(""+i);
		day.select("  ");

                gsdb.w_hide          = new Button("Hide window");
                gsdb.w_hide.setBackground(Const.COLOR_HIDE);
                gsdb.w_hide.setFont(new Font("Helvetica",Font.BOLD, 14));


                // Choices for black and white grades :
                gsdb.w_black_grade.addItem("Grade");  gsdb.w_white_grade.addItem("Grade");
                gsdb.w_black_grade.addItem("Meijin"); gsdb.w_white_grade.addItem("Meijin");
                gsdb.w_black_grade.addItem("Kio"  );  gsdb.w_white_grade.addItem("Kio"  );
                gsdb.w_black_grade.addItem("Kisei");  gsdb.w_white_grade.addItem("Kisei");
                gsdb.w_black_grade.addItem("Ryu-o");  gsdb.w_white_grade.addItem("Ryu-o");
                gsdb.w_black_grade.addItem("Chall");  gsdb.w_white_grade.addItem("Chall");
                gsdb.w_black_grade.addItem("10dan");  gsdb.w_white_grade.addItem("10dan");
                gsdb.w_black_grade.addItem("9dan" );  gsdb.w_white_grade.addItem("9dan" );
                gsdb.w_black_grade.addItem("8dan" );  gsdb.w_white_grade.addItem("8dan" );
                gsdb.w_black_grade.addItem("7dan" );  gsdb.w_white_grade.addItem("7dan" );
                gsdb.w_black_grade.addItem("6dan" );  gsdb.w_white_grade.addItem("6dan" );
                gsdb.w_black_grade.addItem("5dan" );  gsdb.w_white_grade.addItem("5dan" );
                gsdb.w_black_grade.addItem("4dan" );  gsdb.w_white_grade.addItem("4dan" );
                gsdb.w_black_grade.addItem("3dan" );  gsdb.w_white_grade.addItem("3dan" );
                gsdb.w_black_grade.addItem("2dan" );  gsdb.w_white_grade.addItem("2dan" );
                gsdb.w_black_grade.addItem("1dan" );  gsdb.w_white_grade.addItem("1dan" );
                gsdb.w_black_grade.addItem("1kyu" );  gsdb.w_white_grade.addItem("1kyu" );
                gsdb.w_black_grade.addItem("2kyu" );  gsdb.w_white_grade.addItem("2kyu" );
                gsdb.w_black_grade.addItem("3kyu" );  gsdb.w_white_grade.addItem("3kyu" );
                gsdb.w_black_grade.addItem("4kyu" );  gsdb.w_white_grade.addItem("4kyu" );
                gsdb.w_black_grade.addItem("5kyu" );  gsdb.w_white_grade.addItem("5kyu" );
                gsdb.w_black_grade.addItem("6kyu" );  gsdb.w_white_grade.addItem("6kyu" );
                gsdb.w_black_grade.addItem("7kyu" );  gsdb.w_white_grade.addItem("7kyu" );
                gsdb.w_black_grade.addItem("8kyu" );  gsdb.w_white_grade.addItem("8kyu" );
                gsdb.w_black_grade.addItem("9kyu" );  gsdb.w_white_grade.addItem("9kyu" );
                gsdb.w_black_grade.addItem("10kyu");  gsdb.w_white_grade.addItem("10kyu");
                gsdb.w_black_grade.addItem("11kyu");  gsdb.w_white_grade.addItem("11kyu");
                gsdb.w_black_grade.addItem("12kyu");  gsdb.w_white_grade.addItem("12kyu");
                gsdb.w_black_grade.addItem("13kyu");  gsdb.w_white_grade.addItem("13kyu");
                gsdb.w_black_grade.addItem("14kyu");  gsdb.w_white_grade.addItem("14kyu");
                gsdb.w_black_grade.addItem("15kyu");  gsdb.w_white_grade.addItem("15kyu");
                gsdb.w_black_grade.select ("Grade");  gsdb.w_white_grade.select ("Grade");

                gsdb.w_proam.addItem("Professional");
                gsdb.w_proam.addItem("Amateur");
                gsdb.w_proam.addItem("Opening");           gsdb.w_proam.select("Professional");

		date_panel=new Panel();
		date_panel.add(century);
		date_panel.add(year);
		date_panel.add(new Label("/"));
		date_panel.add(month);
		date_panel.add(new Label("/"));
		date_panel.add(day);

                gsdb.txt_panel=new Panel();
                gsdb.txt_panel.setLayout(new GridLayout(17,1));
                gsdb.txt_panel.setFont(fnt);
                gsdb.txt_panel.setBackground(Const.COLOR_BACKGR);
                gsdb.inp_panel=new Panel();
                gsdb.inp_panel.setLayout(new GridLayout(17,1));

                gsdb.txt_panel.add(new Label("Name"));               gsdb.inp_panel.add(gsdb.w_name          );
                gsdb.txt_panel.add(new Label("E-mail"));             gsdb.inp_panel.add(gsdb.w_email         );
                gsdb.txt_panel.add(new Label("Source of score"));    gsdb.inp_panel.add(gsdb.w_source        );
                gsdb.txt_panel.add(new Label("Tournament"));         gsdb.inp_panel.add(gsdb.w_tournament    );
                //gsdb.txt_panel.add(new Label("Date of gameplay"));   gsdb.inp_panel.add(gsdb.w_date          );
                gsdb.txt_panel.add(new Label("Date of gameplay Y/M/D"));   gsdb.inp_panel.add(date_panel           );
                gsdb.txt_panel.add(new Label("Round"));              gsdb.inp_panel.add(gsdb.w_round         );
                gsdb.txt_panel.add(new Label("Venue"));              gsdb.inp_panel.add(gsdb.w_venue         );
                gsdb.txt_panel.add(new Label("Country"));            gsdb.inp_panel.add(gsdb.w_country       );
                gsdb.txt_panel.add(new Label("Prof./Amat. game"));   gsdb.inp_panel.add(gsdb.w_proam         );
                gsdb.txt_panel.add(new Label("Black player Name"));  gsdb.inp_panel.add(gsdb.w_black_player  );
                gsdb.txt_panel.add(new Label("Black player Grade")); gsdb.inp_panel.add(gsdb.w_black_grade   );
                gsdb.txt_panel.add(new Label("Black player ELO"));   gsdb.inp_panel.add(gsdb.w_black_elo     );
                gsdb.txt_panel.add(new Label("White player Name"));  gsdb.inp_panel.add(gsdb.w_white_player  );
                gsdb.txt_panel.add(new Label("White player Grade")); gsdb.inp_panel.add(gsdb.w_white_grade   );
                gsdb.txt_panel.add(new Label("White player ELO"));   gsdb.inp_panel.add(gsdb.w_white_elo     );
                gsdb.txt_panel.add(new Label("Result (1-0, 0-1)"));  gsdb.inp_panel.add(gsdb.w_result        );
                gsdb.txt_panel.add(new Label("Comment"));            gsdb.inp_panel.add(gsdb.w_comment       );

		add("West" ,gsdb.txt_panel);
                add("East" ,gsdb.inp_panel);
		add("South",gsdb.w_hide);

		pack();
	}

	public boolean action(Event e, Object arg)
        {
		if (e.target==gsdb.w_hide || e.id==Event.WINDOW_DESTROY)
		{
			hide();
		}
		return true;
	}

	public void clearData()
	{
                gsdb.w_name          .setText("");
                gsdb.w_email         .setText("");
                gsdb.w_country       .setText("");
                gsdb.w_black_player  .setText("");
                gsdb.w_white_player  .setText("");
                gsdb.w_black_grade   .select("Grade");
                gsdb.w_white_grade   .select("Grade");
                gsdb.w_black_elo     .setText("");
                gsdb.w_white_elo     .setText("");
                gsdb.w_result        .setText("");
                gsdb.w_comment       .setText("");
                gsdb.w_source        .setText("");
                gsdb.w_tournament    .setText("");
                gsdb.w_date          .setText("");
                gsdb.w_round         .setText("");
                gsdb.w_venue         .setText("");
                gsdb.w_proam         .select("Professional");
	}

	public void setDate(String date)
	{
		//System.out.println("setDate : "+date);
		century.select(date.substring(0,2));
		year   .select(date.substring(2,4));
		month  .select(date.substring(4,6));
		day    .select(date.substring(6));
	}

	public String getDate()
	{
		return ( century.getSelectedItem()+year.getSelectedItem()+month.getSelectedItem()+day.getSelectedItem() );
	}
}




