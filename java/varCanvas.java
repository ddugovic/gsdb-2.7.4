//
// GNU SHOGI DATABASE PROJECT
// GN                      CT
// GN       G.S.D.B.       CT
// GN                      CT
// GNU SHOGI DATABASE PROJECT
//
// Matt Casters : 24/11/1996 until now
//
//   This source falls under the    
// 
//              GNU GENERAL PUBLIC LICENSE
//
//   please check the file COPYING in this directory
//
//

import java.awt.*;
import java.util.Vector;

////////////////////////////////////////////////////////////////
//////         varCanvas class                          ////////
////////////////////////////////////////////////////////////////

public class varCanvas extends Canvas
{
	public Gsdb gsdb;
	public Graphics gg;
	public Font fnt;

	public static int LINE_SIZE =20;
	public static int INDENT_X  =30;

	public int dx,dy;
	public int xpos,ypos;

	public String b=new String(); // string buffer

	public int move_number;
	public int total_lines;

	public int exes[]={ 0, 0, 0, 0, 0 };
	public int whys[]={ 0, 0, 0, 0, 0 };
	public int pts=exes.length;

	public Vector vmoves;
	public Vector vlevel;
	public int mvar;
	public int var_level;

	public int last_clicked;
	public int last_clicked_level;
	public Move last_clicked_move;

	public varCanvas(Gsdb g)
	{
		super();
		gsdb=g;

		fnt=new Font("Courier",Font.BOLD,12);
		setFont(fnt);

		vmoves  =new Vector(5,5);
		vlevel  =new Vector(5,5);

		last_clicked=0;
		last_clicked_level=0;
		last_clicked_move=null;
	}

	public void paint(Graphics g)
	{
		g.setColor(Const.COLOR_BACKGR);
		g.fillRect(0,0,size().width,size().height);
		g.setColor(Const.COLOR_FOREGR);
		drawMoves(g);
	}

	public void drawMoves(Graphics g)
	{
		int i,t;
		int v=gsdb.var_frame.sbv.getValue();
		int m=gsdb.game.size();
		m=(m>total_lines)?m:total_lines;

		total_lines=0;

		dx=50-gsdb.var_frame.sbh.getValue()*10;
		dy= 0-((v*m*LINE_SIZE)/100);

		xpos=dx;
		ypos=dy;

		vmoves  .removeAllElements();
		vlevel  .removeAllElements();

		for (i=0;gsdb.game.root.variation[i].piece!=0;i++)
		{
			mvar=i;
			var_level=0;
			move_number=i+1;
			ypos+=LINE_SIZE;
			total_lines++;
			drawMove (g, move_number, ". "+gsdb.game.root.variation[i].ASCII_move(b));
			vmoves.addElement(gsdb.game.root.variation[i]);

			if (gsdb.game.root.variation[i].variation!=null)
			{
				if (!gsdb.game.root.variation[i].expanded)
				{
					drawPlus(g);
				}
				else
				{
					drawMinus(g);
					int vars=gsdb.var_frame.countVariations(gsdb.game.root.variation[i]);
					//System.out.println("Counted "+vars+" variations.");
					drawVariations(g, vars, gsdb.game.root.variation[i]);
				}
			}
		}

		Color c=g.getColor();
		g.setColor(Const.COLOR_HEADER);
		if (last_clicked!=0)
		{
			g.drawRect(5,dy+(last_clicked-1)*LINE_SIZE+5,size().width,LINE_SIZE);
		}
		g.setColor(c);
	}

	public void drawVariations(Graphics g, int vars, Move m)
	{
		// for vars==5 you get following getVariations() results :
		//
		// gsdb.var_frame.getVaration(m,1) --> m 
		// gsdb.var_frame.getVaration(m,2) --> m.variation[0]
		// gsdb.var_frame.getVaration(m,3) --> m.variation[0].variation[0]
		// gsdb.var_frame.getVaration(m,4) --> m.variation[0].variation[0].variation[0]
		// gsdb.var_frame.getVaration(m,5) --> null
		// 

		int v,i,mnr;
		Move look=null;
		xpos+=INDENT_X;
		var_level++;

		for (v=2;v<=vars;v++)
		{
			look=gsdb.var_frame.getVariation(m, v);	
			ypos+=LINE_SIZE; total_lines++;
			drawMove (g,move_number,". "+look.variation[0].ASCII_move(b));
			vmoves.addElement(look.variation[0]);
			if (vars>2) xpos+=INDENT_X;
			for (i=1;i<look.moves;i++)
			{
				ypos+=LINE_SIZE; total_lines++;
				drawMove (g, move_number+i, ". "+look.variation[i].ASCII_move(b));
				vmoves.addElement(look.variation[i]);
				if (look.variation[i].variation!=null)
				{
					if (!look.variation[i].expanded)
					{
						drawPlus(g);
					}
					else
					{
						drawMinus(g);
						mnr=move_number;
						move_number=move_number+i;
						int vrs=gsdb.var_frame.countVariations(look.variation[i]);
						drawVariations(g, vrs, look.variation[i]);
						move_number=mnr;
					}
				}
			}
			if (vars>2) xpos-=INDENT_X;
		}
		var_level--;
		xpos-=INDENT_X;
	}

	public void drawMove(Graphics g, int movenr, String m)
	{
		g.drawString(""+movenr+m,xpos,ypos);
		drawColor(g, movenr);
		vlevel.addElement(new Integer(var_level));
	}

	public void drawPlus(Graphics g)
	{
		ypos+=3; Color c=g.getColor();
		
		g.setColor(Const.COLOR_PLMIN);
		g.fillRect(xpos-32,ypos-15,15,15);
		g.setColor(Const.COLOR_SHADOW);
		g.drawLine(xpos-30,ypos-16,xpos-16,ypos-16);
		g.drawLine(xpos-16,ypos-16,xpos-16,ypos- 2);
		g.setColor(Const.COLOR_HEADER);
		g.drawLine(xpos-29,ypos- 7,xpos-20,ypos- 7);
		g.drawLine(xpos-29,ypos- 8,xpos-20,ypos- 8);
		g.drawLine(xpos-24,ypos- 2,xpos-24,ypos-13);
		g.drawLine(xpos-25,ypos- 2,xpos-25,ypos-13);

		g.setColor(c); ypos-=3;
	}

	public void drawMinus(Graphics g)
	{
		ypos+=3; Color c=g.getColor();

		g.setColor(Const.COLOR_PLMIN);
		g.fillRect(xpos-32,ypos-15,15,15);
		g.setColor(Const.COLOR_SHADOW);
		g.drawLine(xpos-30,ypos-16,xpos-16,ypos-16);
		g.drawLine(xpos-16,ypos-16,xpos-16,ypos- 2);
		g.setColor(Const.COLOR_HEADER);
		g.drawLine(xpos-29,ypos- 7,xpos-20,ypos- 7);
		g.drawLine(xpos-29,ypos- 8,xpos-20,ypos- 8);

		g.setColor(c); ypos-=3;
	}

	public void drawColor(Graphics g, int mnr)
	{
		Color c=g.getColor();
		if ((mnr%2)==1) // black or white ??
			g.setColor(Color.black); else g.setColor(Color.white);

		exes[0]=xpos-14;  whys[0]=ypos;
		exes[1]=xpos-12;  whys[1]=ypos- 7;
		exes[2]=xpos- 8;  whys[2]=ypos-10;
		exes[3]=xpos- 4;  whys[3]=ypos- 7;
		exes[4]=xpos- 2;  whys[4]=ypos;

		g.fillPolygon(exes,whys,pts);
		g.setColor(c);
	}

	public boolean mouseDown(Event evt, int x, int y)
	{
		int movenr=(y-dy+20)/LINE_SIZE;
		String num;
		int xx;

		Graphics g=getGraphics();
		Color c=g.getColor();
		g.setColor(Const.COLOR_BACKGR);
		if (last_clicked!=0)
		{
			g.drawRect(5,dy+(last_clicked-1)*LINE_SIZE+5,size().width,LINE_SIZE);
		}

		if (movenr-1<vmoves.size())
		{
			//System.out.println("Line number="+movenr+" level="+vlevel.elementAt(movenr-1));
			g.setColor(Const.COLOR_HEADER);
			g.drawRect(5,dy+(movenr-1)*LINE_SIZE+5,size().width,LINE_SIZE);
		}
		g.setColor(c);

		if (movenr-1>=vmoves.size()) return false;

		last_clicked=movenr;
		last_clicked_level=((Integer)vlevel.elementAt(movenr-1)).intValue();
		last_clicked_move=searchMove((Move)vmoves.elementAt(movenr-1));
		//int level=x/
		if (last_clicked_move!=null)
		{
			if (last_clicked_move.variation!=null)
			{
				if (evt.clickCount==2) // double click expands / collapses
				last_clicked_move.expanded=!last_clicked_move.expanded;
				repaint();
			}
		}

		return true;
	}

	public Move searchMove(Move look)
	{
		gsdb.game.root.moves=gsdb.game.size();
		Move found=searchMove(look, gsdb.game.root);
		return found;
	}

	public Move searchMove(Move look, Move tree)
	{
		Move subtree;

		for (int i=0;i<tree.moves;i++)
		{
			if (tree.variation[i]==look) return look;
			if (tree.variation[i].variation!=null)
			{
				subtree=searchMove(look, tree.variation[i]);
				if (subtree!=null) return subtree;
			}
		}
		return null;
	}
}

