
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

////////////////////////////////////////////////////////////////
//////         varFrame class                           ////////
////////////////////////////////////////////////////////////////

public class varFrame extends Frame
{
	public Gsdb gsdb;
	public Scrollbar sbv,sbh;
	public varCanvas canv;
	public Button expand, collaps;
	public MenuBar mbar;
	public Menu file, tree, move, help;

	public static String MENU_SEPERATOR     = new String("-");

	public static String MENU_FILE          = new String("File");
	public static String MENU_FILE_UPDATE   = new String("Update screen");
	public static String MENU_FILE_CLOSE    = new String("Close");

	public static String MENU_TREE          = new String("Tree");
	public static String MENU_TREE_EXPAND   = new String("Expand all");
	public static String MENU_TREE_EXP      = new String("Expand");
	public static String MENU_TREE_COL      = new String("Collapse");
	public static String MENU_TREE_COLLAPSE = new String("Collapse all");
	public static String MENU_HELP          = new String("Help");
	public static String MENU_HELP_ABOUT    = new String("About");

	public static String MENU_MOVE          = new String("Move");
	public static String MENU_MOVE_GOTO     = new String("Goto this position");
	public static String MENU_MOVE_ENTER    = new String("Enter this variation");
	public static String MENU_MOVE_LEAVE    = new String("Leave this variation");

	public String b=new String();

	public varFrame(Gsdb g)
	{
		super("Variations tree");
		gsdb=g;

		GridBagLayout gridbag = new GridBagLayout();

		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(gridbag);
		setBackground(Const.COLOR_BACKGR);

		sbv=new Scrollbar(Scrollbar.VERTICAL,  0,0,0,100);
		sbh=new Scrollbar(Scrollbar.HORIZONTAL,0,0,0,100);
		canv=new varCanvas(gsdb);
		canv.setBackground(Const.COLOR_BACKGR);
		canv.setForeground(Const.COLOR_FOREGR);

		mbar=new MenuBar(); setMenuBar(mbar);

		file=new Menu(MENU_FILE); mbar.add(file);
			file.add(new MenuItem(MENU_FILE_UPDATE));
			file.add(new MenuItem(MENU_SEPERATOR  ));
			file.add(new MenuItem(MENU_FILE_CLOSE ));

		tree=new Menu(MENU_TREE); mbar.add(tree);
			tree.add(new MenuItem(MENU_TREE_EXPAND  ));
			tree.add(new MenuItem(MENU_TREE_EXP     ));
			tree.add(new MenuItem(MENU_TREE_COL     ));
			tree.add(new MenuItem(MENU_TREE_COLLAPSE));
/*
		move=new Menu(MENU_MOVE); mbar.add(move);
			move.add(new MenuItem(MENU_MOVE         ));
			move.add(new MenuItem(MENU_MOVE_GOTO    ));
			move.add(new MenuItem(MENU_MOVE_ENTER   ));
			move.add(new MenuItem(MENU_MOVE_LEAVE   ));
*/
		help=new Menu(MENU_HELP); mbar.add(help);
			help.add(new MenuItem(MENU_HELP_ABOUT));
		mbar.setHelpMenu(help);
		

		// Canvas
		buildConstraints(constraints, 0, 0, 1, 1, 95, 90);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; 
		gridbag.setConstraints(canv, constraints);
		add(canv);

		// Vertical scrollbar
		buildConstraints(constraints, 1, 0, 1, 1, 5, 10); 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; 
		gridbag.setConstraints(sbv, constraints);
		add(sbv);

		// Horizontal scrollbar
		buildConstraints(constraints, 0, 1, 2, 1, 100, 10); 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.EAST; 
		gridbag.setConstraints(sbh, constraints);
		add(sbh);

		resize(200,400);
	}

	public void init()
	{
		repaint();
	}

        void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy)
        {
                gbc.gridx = gx;
                gbc.gridy = gy;
                gbc.gridwidth = gw;
                gbc.gridheight = gh;
                gbc.weightx = wx;
                gbc.weighty = wy;
        }

	public boolean handleEvent(Event evt)
	{
		if (evt.id== Event.WINDOW_DESTROY) { hide(); return true; } 
		if (evt.target == sbv || evt.target == sbh) { canv.repaint(); return true; } 
		return super.handleEvent(evt);
	}

	public boolean action(Event evt, Object arg)
	{
		if (evt.target instanceof MenuItem)
		{
			String label=(String)arg;

			if (label.equals(MENU_FILE_CLOSE   ))   hide();
			if (label.equals(MENU_FILE_UPDATE  ))   repaint();
			if (label.equals(MENU_TREE_EXPAND  ))   expand_all();
			if (label.equals(MENU_TREE_EXP     ))   expand_tree();
			if (label.equals(MENU_TREE_COL     ))   collapse_tree();
			if (label.equals(MENU_TREE_COLLAPSE))   collapse_all();
			if (label.equals(MENU_HELP_ABOUT   ))   help();
		/*
			if (label.equals(MENU_MOVE_GOTO    ))   gotoMove();
			if (label.equals(MENU_MOVE_ENTER   ))   gsdb.game.enterVariation();
			if (label.equals(MENU_MOVE_LEAVE   ))   gsdb.game.enterVariation();
		*/
		}
		return true;
	}

	public void expand_all()
	{
		gsdb.game.root.moves=gsdb.game.size();
		changeVariation(gsdb.game.root, true);
		canv.last_clicked=0;
		canv.repaint();
	}

	public void expand_tree()
	{
		if (canv.last_clicked_move!=null) 
			canv.last_clicked_move.expanded=true;
		canv.last_clicked=0;
		canv.repaint();
	}

	public void collapse_tree()
	{
		gsdb.game.root.moves=gsdb.game.size();
		if (canv.last_clicked_move!=null) 
			canv.last_clicked_move.expanded=false;
		canv.last_clicked=0;
		canv.repaint();
	}

	public void collapse_all()
	{
		gsdb.game.root.moves=gsdb.game.size();
		changeVariation(gsdb.game.root, false);
		canv.total_lines=gsdb.game.size();
		canv.last_clicked=0;
		canv.repaint();
	}

	public void help()
	{
		MyDialog dl=new MyDialog(gsdb, "GSDB Game Tree window, GNU Public Licence (c) 1998", MyDialog.OK, MyDialog.NOTHING);
		dl.show();
	}

        public void deleteVariation()
        {
        }

	public void setVariation(Move m)
	{
	}

        public void changeVariation(Move m, boolean expand)
        {
                int v;

                for (v=0;v<m.moves;v++)
                {
                        m.variation[v].expanded=expand;
                        if (m.variation[v].variation!=null)
                                changeVariation(m.variation[v], expand);
                }
        }

        public int countVariations(Move m)
        {
                int retval=0;
                Move look=m;

                while (look!=null) 
                {
                        retval++;
                        if (look.variation!=null) look=look.variation[0]; else look=null;
                }

                return retval;
        }

        public Move getVariation(Move m, int var)
        {
                int i;
                Move look=m;

                for (i=2;i<var;i++)
                {
                        if (look.variation==null) return null;
                        look=look.variation[0];
                }
                return look;
        }

        public void gotoMove()
        {
		if (canv.last_clicked_move!=null)
		{
			System.out.println("clicked last on move ["+canv.last_clicked_move.ASCII_move(b)+"] level="+canv.last_clicked_level);
			gotoMove(canv.last_clicked_move,canv.last_clicked_level);
		}
		else
		{
			MyDialog md=new MyDialog(gsdb,"Click on a move first !",MyDialog.OK,MyDialog.NOTHING);
			md.show();
		}
        }

	public void gotoMove(Move look, int level)
	{
		boolean found=false;

		gsdb.game.begin();
		while (gsdb.game.forward() && gsdb.game.prevMove()!=look);
	}
}

