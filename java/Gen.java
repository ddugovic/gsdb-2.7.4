
//
// GNU SHOGI DATABASE PROJECT
// GN                      CT
// GN       G.S.D.B.       CT
// GN                      CT
// GNU SHOGI DATABASE PROJECT
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
//////         Gen class                                ////////
////////////////////////////////////////////////////////////////

class Gen
{
	public int From[];
	public int To[];
	public int Piece[];
	public int Taken[];
	public boolean Promotes[];
	public int Moves[];
	public int start[];
	public boolean checking;
	public int kingpos[];
	public int x,i,j,u,v,z,f,g,a,b,c,d,r,to;
	public int oldfrom,oldto,oldpiece,oldtaken,oldmoves;
	public boolean oldpromotes;
	public long value[];
	public int begin[];
	public int dummy;
	public long positions;
	public int piece;
	public String buffer;
	public StringBuffer sbuffer;
	public boolean result;
	public Move bmove;
	public int pos;
	public int lastmove;
	public boolean no_checking;
	
        public int moveptr;
        public Gsdb gsdb;
	public int real_depth;
	public Move best_move;
	public String buf;

	public Move best_path[][];

	long piece_value[] = { 0, 1000, 3000, 4000, 6000, 7000, 11000, 12000, 0, 4000, 5000, 5500, 6500, 12000, 14000,-1000,-3000,-4000,-6000,-7000,-11000,-12000, 0,-4000,-5000,-5500,-6500,-12000,-14000 } ;

        public Gen(Gsdb g)
        {
                moveptr=0;
                int max=Const.MAXMOVES * Const.MAXPLIES;

		From=new int[max];
		To=new int[max];
		Piece=new int[max];
		Taken=new int[max];
		Promotes=new boolean[max];
		Moves=new int[max];

		start=new int[Const.MAXPLIES];

		buffer=new String();
		sbuffer=new StringBuffer();

		best_path=new Move[Const.MAXPLIES][Const.MAXPLIES];
		for (i=0;i<Const.MAXPLIES;i++) for (j=0;j<Const.MAXPLIES;j++) best_path[i][j]=new Move();
		real_depth=0;
		best_move=new Move();
		buf=new String();
		kingpos=new int[2];
		value=new long[Const.MAXPLIES];
		begin=new int[Const.MAXPLIES];

		checking=false;
		no_checking=false;

		bmove=new Move();

                gsdb=g;
        }

	public boolean isBlack(int piece)
	{
		if (piece<15) return true; else return false;
	}

	public boolean isWhite(int piece)
	{
		if (piece>14) return true; else return false;
	}

        public int generateMoves()
        {
		start[real_depth]=moveptr;

                for (pos=0;pos<81;pos++)
		{
			piece=gsdb.board[pos];
			if (piece!=Const.EMPTY)
			{
				if (gsdb.side==Const.BLACK)
				{
					switch (gsdb.board[pos])
					{
						case Const.BLACK_PAWN    : genBlackPawn(); break;
						case Const.BLACK_LANCE   : genBlackLance(); break;
						case Const.BLACK_KNIGHT  : genBlackKnight(); break;
						case Const.BLACK_SILVER  : genBlackSilver(); break;
						case Const.BLACK_GOLD    : genBlackGold(); break;
						case Const.BLACK_BISHOP  : genBlackBishop(); break;
						case Const.BLACK_ROOK    : genBlackRook(); break;
						case Const.BLACK_KING    : genBlackKing(); break;
						case Const.BLACK_PPAWN   : genBlackPPawn(); break;
						case Const.BLACK_PLANCE  : genBlackPLance(); break;
						case Const.BLACK_PKNIGHT : genBlackPKnight(); break;
						case Const.BLACK_PSILVER : genBlackPSilver(); break;
						case Const.BLACK_PBISHOP : genBlackPBishop(); break;
						case Const.BLACK_PROOK   : genBlackPRook(); break;
						default: break;
					}
				}
				else
				{
					switch (gsdb.board[pos])
					{
						case Const.WHITE_PAWN    : genWhitePawn(); break;
						case Const.WHITE_LANCE   : genWhiteLance(); break;
						case Const.WHITE_KNIGHT  : genWhiteKnight(); break;
						case Const.WHITE_SILVER  : genWhiteSilver(); break;
						case Const.WHITE_GOLD    : genWhiteGold(); break;
						case Const.WHITE_BISHOP  : genWhiteBishop(); break;
						case Const.WHITE_ROOK    : genWhiteRook(); break;
						case Const.WHITE_KING    : genWhiteKing(); break;
						case Const.WHITE_PPAWN   : genWhitePPawn(); break;
						case Const.WHITE_PLANCE  : genWhitePLance(); break;
						case Const.WHITE_PKNIGHT : genWhitePKnight(); break;
						case Const.WHITE_PSILVER : genWhitePSilver(); break;
						case Const.WHITE_PBISHOP : genWhitePBishop(); break;
						case Const.WHITE_PROOK   : genWhitePRook(); break;

						default: break;
					}
				}
			}
		}
		for (j=0;j<81;j++) if (gsdb.board[j]==Const.EMPTY) for (i=Const.PAWN;i<Const.KING;i++) // The Const.DROPS !
		{
			//System.out.println("i="+i+" j="+j);
			if (gsdb.side==Const.BLACK)
			{
				if (gsdb.pieces_in_hand[Const.BLACK][i]>0)
				{
					switch( i )
					{
					case Const.PAWN    : if (j> 8) if (canBlackDropPawn(j)) setMove(Const.DROP, j, Const.PAWN, Const.EMPTY, false); break;
					case Const.LANCE   : if (j> 8) setMove(Const.DROP, j, Const.LANCE, Const.EMPTY, false); break;
					case Const.KNIGHT  : if (j>17) setMove(Const.DROP, j, Const.KNIGHT, Const.EMPTY, false); break;
					case Const.SILVER  : setMove(Const.DROP, j, Const.SILVER, Const.EMPTY, false); break;
					case Const.GOLD    : setMove(Const.DROP, j, Const.GOLD  , Const.EMPTY, false); break; 
					case Const.BISHOP  : setMove(Const.DROP, j, Const.BISHOP, Const.EMPTY, false); break; 
					case Const.ROOK    : setMove(Const.DROP, j, Const.ROOK  , Const.EMPTY, false); break; 
					default : break;
					}
				}
			}
			else
			{
				if (gsdb.pieces_in_hand[Const.WHITE][i]>0)
				{
					switch( i )
					{
					case Const.PAWN    : if (j<72) if (canWhiteDropPawn(j)) setMove(Const.DROP, j, Const.PAWN, Const.EMPTY, false); break;
					case Const.LANCE   : if (j<72) setMove(Const.DROP, j, Const.LANCE, Const.EMPTY, false); break;
					case Const.KNIGHT  : if (j<63) setMove(Const.DROP, j, Const.KNIGHT, Const.EMPTY, false); break;
					case Const.SILVER  : setMove(Const.DROP, j, Const.SILVER, Const.EMPTY, false); break;
					case Const.GOLD    : setMove(Const.DROP, j, Const.GOLD  , Const.EMPTY, false); break; 
					case Const.BISHOP  : setMove(Const.DROP, j, Const.BISHOP, Const.EMPTY, false); break; 
					case Const.ROOK    : setMove(Const.DROP, j, Const.ROOK  , Const.EMPTY, false); break; 
					default : break;
					}
				}
			}
		}
		//System.out.println("depth="+real_depth+" generateMoves() done");
		return (moveptr-start[real_depth]);
        }

	public boolean canBlackDropPawn(int ps)
	{
		c=ps%9;

		result=true;
		for (z=0;z<9;z++)
		{
			if ( gsdb.board[ c + z*9 ]==Const.BLACK_PAWN )
			{
				result=false;
				break;
			}
		}

		return result;
	}

	public boolean canWhiteDropPawn(int ps)
	{
		c=ps%9;

		result=true;
		for (z=0;z<9;z++)
		{
			if ( gsdb.board[ c + z*9 ]==Const.WHITE_PAWN )
			{
				result=false;
				break;
			}
		}

		return result;
	}

	public boolean checkMove(int from, int to)
	{
		if (from<0 || from>80 || to<0 || to>80) return false;
		return true;
	}

	public boolean canBlackMove(int to)
	{
		if (to<0 || to>80) return false;
		if (gsdb.board[to]==Const.EMPTY) return true;
		if (isWhite(gsdb.board[to])) return true;
		return false;
	}

	public boolean canWhiteMove(int to)
	{
		if (to<0 || to>80) return false;
		if (gsdb.board[to]==Const.EMPTY) return true;
		if (isBlack(gsdb.board[to])) return true;
		return false;
	}

	public boolean canBlackPromote(int from, int to)
	{
		if (from<27 || to<27) return true; else return false;
	}

	public boolean canWhitePromote(int from, int to)
	{
		if (from>53 || to>53) return true; else return false;
	}

	public int col(int ps)
	{
		return(ps % 9);
	}

	public int row(int ps)
	{
		return(ps / 9);
	}

	public int promote(int pce)
	{
		if (pce<15)
		{
			if (pce<Const.BLACK_GOLD) return pce+8;
			if (pce<Const.BLACK_KING) return pce+7;
			return pce;
		}
		else
		{
			if (pce<Const.WHITE_GOLD) return pce+8;
			if (pce<Const.WHITE_KING) return pce+7;
			return pce;
		}
	}

	public void setMove(int f, int t, int pce, int tkn, boolean pro)
	{
		From    [moveptr]=f;
		To      [moveptr]=t;
		Piece   [moveptr]=pro?promote(pce):pce;
		Taken   [moveptr]=tkn;
		Promotes[moveptr]=pro;

		if (no_checking)
		{
			moveptr++;
			return;
		}

		if (gsdb.side==Const.BLACK)
		{
			if (Taken[moveptr]==Const.WHITE_KING)
			{
				moveptr++;
				return;
			}
			boolean check=false;
			doMove(moveptr);
			check=isWhiteCheck();
			undoMove(moveptr);
			if (check)
			{
				moveptr++;
			}
		}
		else
		{
			boolean check=false;
			doMove(moveptr);
			check=isWhiteCheck();
			undoMove(moveptr);
			if (!check)
			{
				moveptr++;
			}
		}
	}

	public void genBlackPawn()
	{
		r=row(pos);
		to=pos-9;

		if (!canBlackMove(to)) return;
		if (r==1)
		{
			setMove(pos,to,gsdb.board[pos],gsdb.board[to],true); // piece must promote !
		}
		else
		if (r<=3)
		{
			setMove(pos,to,gsdb.board[pos],gsdb.board[to],false); // piece can promote !
			setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
		}
		else
		{
			setMove(pos,to,gsdb.board[pos],gsdb.board[to],false); // piece can't promote !
		}
	}

	public void genBlackLance()
	{
		for (i=1;i<9;i++)
		{
			to=pos-9*i;
			if (canBlackMove(to)) // Ok, we can move the lance
			{
				r=row(to);

				if (r==0)
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true); // piece must promote !
				}
				else
				if (r<3)
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false); // piece can promote !
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				}
				else
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false); // piece can't promote !
				}
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else
				break; // stop the loop, we can't jump over pieces
		}
	}

	public void genBlackKnight()
	{
		r=row(pos);
		c=col(pos);

		if (c<8) // watch it, where on the left edge
		{
			to=pos-17;
			if (canBlackMove(to))
			{
				if (r<=3) // we must promote 
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
				}
				else
				if (r==4) // we can promote
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				}
				else
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				}
			}
		}
		if (c>0)
		{
			to=pos-19;
			if (canBlackMove(to))
			{
				if (r<=3) // we must promote 
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
				}
				else
				if (r==4) // we can promote
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				}
				else
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				}
			}
		}
	}

	public void genBlackSilver()
	{
		r=row(pos);
		c=col(pos);
		
		if (c>0)
		{
			to=pos-10;
			if (canBlackMove(to))
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
			}
			to=pos+8;
			if (canBlackMove(to))
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
			}
		}
		if (c<8)
		{
			to=pos-8;
			if (canBlackMove(to))
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
			}
			to=pos+10;
			if (canBlackMove(to))
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
			}
		}
		to=pos-9;
		if (canBlackMove(to))
		{
			setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
		}
	}

	public void genBlackGold()
	{
		r=row(pos);
		c=col(pos);
		
		if (c<8)
		{
			to=pos+ 1; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos- 8; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		if (c>0)
		{
			to=pos-10; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos- 1; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		to=pos- 9; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		to=pos+ 9; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
	}

	public void genBlackBishop()
	{
		r=row(pos);
		c=col(pos);

		for (i=1;i<=c;i++)
		{
			to=pos-10*i;
			if (canBlackMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos-8*i;
			if (canBlackMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<=c;i++)
		{
			to=pos+8*i;
			if (canBlackMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos+10*i;
			if (canBlackMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
	}

	public void genBlackRook()
	{
		r=row(pos);
		c=col(pos);

		for (i=1;i<=r;i++)
		{
			to=pos-9*i;
			if (canBlackMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-r;i++)
		{
			to=pos+9*i;
			if (canBlackMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<=c;i++)
		{
			to=pos-i;
			if (canBlackMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos+i;
			if (canBlackMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canBlackPromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
	}

	public void genBlackKing()
	{
		r=row(pos);
		c=col(pos);
		
		if (c>0)
		{
			to=pos-10; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos- 1; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos+ 8; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		if (c<8)
		{
			to=pos+10; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos+ 1; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos- 8; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		to=pos- 9; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		to=pos+ 9; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
	}

	public void genBlackPPawn()
	{
		genBlackGold();
	}

	public void genBlackPLance()
	{
		genBlackGold();
	}

	public void genBlackPKnight()
	{
		genBlackGold();
	}

	public void genBlackPSilver()
	{
		genBlackGold();
	}

	public void genBlackPBishop()
	{
		r=row(pos);
		c=col(pos);
		
		if (c>0)
		{
			to=pos-1; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		if (c<8)
		{
			to=pos+1; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		to=pos+9; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		to=pos-9; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);

		for (i=1;i<=c;i++)
		{
			to=pos-10*i;
			if (canBlackMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos-8*i;
			if (canBlackMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<=c;i++)
		{
			to=pos+8*i;
			if (canBlackMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos+10*i;
			if (canBlackMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}

	}

	public void genBlackPRook()
	{
		r=row(pos);
		c=col(pos);
		
		if (c>0)
		{
			to=pos-10; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos+ 8; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		if (c<8)
		{
			to=pos- 8; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos+10; if (canBlackMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}

		for (i=1;i<=r;i++)
		{
			to=pos-9*i;
			if (canBlackMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-r;i++)
		{
			to=pos+9*i;
			if (canBlackMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<=c;i++)
		{
			to=pos-i;
			if (canBlackMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos+i;
			if (canBlackMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
	}








	public void genWhitePawn()
	{
		r=row(pos);
		to=pos+9;

		if (!canWhiteMove(to)) return;
		if (r==7)
		{
			setMove(pos,to,gsdb.board[pos],gsdb.board[to],true); // piece must promote !
		}
		else
		if (r>=5)
		{
			setMove(pos,to,gsdb.board[pos],gsdb.board[to],false); // piece can promote !
			setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
		}
		else
		{
			setMove(pos,to,gsdb.board[pos],gsdb.board[to],false); // piece can't promote !
		}
	}

	public void genWhiteLance()
	{
		for (i=1;i<9;i++)
		{
			to=pos+9*i;
			if (canWhiteMove(to)) // Ok, we can move the lance
			{
				r=row(to);

				if (r==8)
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true); // piece must promote !
				}
				else
				if (r>5 || row(pos)>5)
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false); // piece can promote !
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				}
				else
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false); // piece can't promote !
				}
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else
				break; // stop the loop, we can't jump over pieces
		}
	}

	public void genWhiteKnight()
	{
		r=row(pos);
		c=col(pos);

		if (c<8) // watch it, where on the left edge
		{
			to=pos+19;
			if (canWhiteMove(to))
			{
				if (r>=5) // we must promote 
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
				}
				else
				if (r==4) // we can promote
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				}
				else
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				}
			}
		}
		if (c>0)
		{
			to=pos+17;
			if (canWhiteMove(to))
			{
				if (r>=5) // we must promote 
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
				}
				else
				if (r==4) // we can promote
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				}
				else
				{
					setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				}
			}
		}
	}

	public void genWhiteSilver()
	{
		r=row(pos);
		c=col(pos);
		
		if (c>0)
		{
			to=pos-10;
			if (canWhiteMove(to))
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
			}
			to=pos+8;
			if (canWhiteMove(to))
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
			}
		}
		if (c<8)
		{
			to=pos-8;
			if (canWhiteMove(to))
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
			}
			to=pos+10;
			if (canWhiteMove(to))
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
			}
		}
		to=pos+9;
		if (canWhiteMove(to))
		{
			setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true);
		}
	}

	public void genWhiteGold()
	{
		r=row(pos);
		c=col(pos);
		
		if (c<8)
		{
			to=pos+10; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos+ 1; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		if (c>0)
		{
			to=pos+ 8; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos- 1; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		to=pos- 9; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		to=pos+ 9; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
	}

	public void genWhiteBishop()
	{
		r=row(pos);
		c=col(pos);

		for (i=1;i<=c;i++)
		{
			to=pos-10*i;
			if (canWhiteMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos-8*i;
			if (canWhiteMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<=c;i++)
		{
			to=pos+8*i;
			if (canWhiteMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos+10*i;
			if (canWhiteMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
	}

	public void genWhiteRook()
	{
		r=row(pos);
		c=col(pos);
		
		for (i=1;i<=r;i++)
		{
			to=pos-9*i;
			if (canWhiteMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-r;i++)
		{
			to=pos+9*i;
			if (canWhiteMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<=c;i++)
		{
			to=pos-i;
			if (canWhiteMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos+i;
			if (canWhiteMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (canWhitePromote(pos,to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],true );
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
	}

	public void genWhiteKing()
	{
		r=row(pos);
		c=col(pos);
		
		if (c>0)
		{
			to=pos+ 8; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos- 1; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos-10; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		if (c<8)
		{
			to=pos- 8; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos+ 1; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos+10; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		to=pos- 9; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		to=pos+ 9; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
	}

	public void genWhitePPawn()
	{
		genWhiteGold();
	}

	public void genWhitePLance()
	{
		genWhiteGold();
	}

	public void genWhitePKnight()
	{
		genWhiteGold();
	}

	public void genWhitePSilver()
	{
		genWhiteGold();
	}

	public void genWhitePBishop()
	{
		r=row(pos);
		c=col(pos);
		
		if (c>0)
		{
			to=pos-1; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		if (c<8)
		{
			to=pos+1; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		to=pos+9; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		to=pos-9; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);

		for (i=1;i<=c;i++)
		{
			to=pos-10*i;
			if (canWhiteMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos-8*i;
			if (canWhiteMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<=c;i++)
		{
			to=pos+8*i;
			if (canWhiteMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos+10*i;
			if (canWhiteMove(to)) // Ok, we can move the bishop
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
	}

	public void genWhitePRook()
	{
		r=row(pos);
		c=col(pos);
		
		if (c>0)
		{
			to=pos-10; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos+ 8; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}
		if (c<8)
		{
			to=pos- 8; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
			to=pos+10; if (canWhiteMove(to)) setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
		}

		for (i=1;i<=r;i++)
		{
			to=pos-9*i;
			if (canWhiteMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-r;i++)
		{
			to=pos+9*i;
			if (canWhiteMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<=c;i++)
		{
			to=pos-i;
			if (canWhiteMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
		for (i=1;i<9-c;i++)
		{
			to=pos+i;
			if (canWhiteMove(to)) // Ok, we can move the rook
			{
				setMove(pos,to,gsdb.board[pos],gsdb.board[to],false);
				if (gsdb.board[to]!=Const.EMPTY) break; // stop the loop, we can't jump over pieces
			}
			else break; // stop the loop, we can't jump over pieces
		}
	}

	public void doMove( int i )
	{
		bmove.setMove(From[i],To[i],Piece[i],Taken[i],Promotes[i]);
		gsdb.doMove( bmove );
		if (Piece[i]==Const.BLACK_KING) kingpos[Const.BLACK]=To[i]; else if (Piece[i]==Const.WHITE_KING) kingpos[Const.WHITE]=To[i];
	}

	public void undoMove( int i )
	{
		bmove.setMove(From[i],To[i],Piece[i],Taken[i],Promotes[i]);
		gsdb.undoMove( bmove );
		if (Piece[i]==Const.BLACK_KING) kingpos[Const.BLACK]=From[i]; else if (Piece[i]==Const.WHITE_KING) kingpos[Const.WHITE]=From[i];
	}

	/*
		int i;
		for (i=0;i<81;i++)
		{
			retval+=piece_value[ gsdb.board[i] ];
		}
		for (i=PAWN;i<KING;i++)
		{
			retval+=piece_value[i]*gsdb.pieces_in_hand[Const.BLACK][i];
			retval-=piece_value[i]*gsdb.pieces_in_hand[Const.WHITE][i];
		}
	*/

	public long evaluatePosition()
	{
		value[real_depth]=0;

		positions++;
		if (gsdb.side==Const.WHITE)
		{
			dummy=moveptr;
			if (generateMoves()==0)
			{
				if (From[lastmove]==Const.DROP && Piece[lastmove]==Const.PAWN)
				{
					// This move is illegal : you can't drop a pawn to give mate
					value[real_depth]=190000;
				}
				else
				{
					value[real_depth]=-90000+real_depth;
				}
			}
			// White has no legal moves left : checkmate.
			moveptr=dummy;
		}
		else
		{
			dummy=moveptr;
			generateMoves();
			for (x=dummy;x<moveptr;x++)
			{
				if (Taken[x]==Const.WHITE_KING)
				{
					value[real_depth]=90000-real_depth;
					break;
				}
			}
			moveptr=dummy;
		}

		return value[real_depth];
	}

	public long calc(long alfa, long beta, int depth)
	{
		if (depth==0) return evaluatePosition();

		value[real_depth]=alfa;
		begin[real_depth]=moveptr;
		generateMoves();
		if (real_depth==0)
		{
			sortMoves(begin[0],moveptr);
			if (Moves[0]==0 && Piece[0]!=Const.PAWN && From[0]!=Const.DROP)
			{
				best_path[0][0].setMove(From[0],To[0],Piece[0],Taken[0],Promotes[0]);
				best_move.setMove(From[0],To[0],Piece[0],Taken[0],Promotes[0]);
				return 95000;
			}
		}

		best_path[real_depth][real_depth  ].clearMove();
		best_path[real_depth][real_depth+1].clearMove();
		
		if (gsdb.side==Const.WHITE && begin[real_depth]==moveptr)
		{
			return -99999+real_depth;
		}

		for (int m=begin[real_depth];m<moveptr;m++)
		{
			if (real_depth<1)
			{
				// Something on the screen is nice ...
				gsdb.setMessage("["+depth+"p  "+ASCII_move(m)+" "+(m+1)+"/"+moveptr+" ("+value[0]+")] Best="+printBestPath(buffer));
			}
			if (Taken[m]==Const.BLACK_KING || Taken[m]==Const.WHITE_KING)
			{
				best_path[real_depth][real_depth  ].setMove(From[m],To[m],Piece[m],Taken[m],Promotes[m]);
				moveptr=begin[real_depth]; // clean up
				return 99999-real_depth;
			}

			doMove( m );
			lastmove=m;
			gsdb.side=gsdb.side^1;
			real_depth++;

			long v=-calc(-beta,-value[real_depth-1],depth-1);

			real_depth--;
			gsdb.side=gsdb.side^1;

			undoMove( m );

			if (v>value[real_depth])
			{
				value[real_depth]=v;
				best_path[real_depth][real_depth].setMove(From[m],To[m],Piece[m],Taken[m],Promotes[m]);
				for (x=real_depth+1;x<Const.MAXPLIES;x++) best_path[real_depth][x].copy( best_path[real_depth+1][x] );
				if (real_depth==0)
				{
					best_move.setMove(From[m],To[m],Piece[m],Taken[m],Promotes[m]);
				}
			}
			if (value[real_depth]>=beta)
			{
				break;
			}
		}

		moveptr=begin[real_depth]; // cleanup on this level !

		return value[real_depth];
	}

	public void sortMoves(int start, int stop)
	{
		int backup=moveptr;
		for (f=start;f<stop;f++)
		{
			doMove(f);
			gsdb.side=gsdb.side^1;
			Moves[f]=generateMoves(); moveptr=backup;
			gsdb.side=gsdb.side^1;
			undoMove(f);
		}

		for (f=start;f<stop;f++)
		{
			for (g=start;g<stop-1;g++)
			{
				if (Moves[g]>Moves[g+1])
				{
					oldfrom    =From    [g]; oldto      =To      [g];
					oldpiece   =Piece   [g]; oldtaken   =Taken   [g];
					oldpromotes=Promotes[g]; oldmoves   =Moves   [g];

					From[g]=From[g+1]; To[g]=To[g+1]; Piece[g]=Piece[g+1];
					Taken[g]=Taken[g+1]; Promotes[g]=Promotes[g+1]; Moves[g]=Moves[g+1];
					From[g+1]=oldfrom; To[g+1]=oldto; Piece[g+1]=oldpiece;
					Taken[g+1]=oldtaken; Promotes[g+1]=oldpromotes; Moves[g+1]=oldmoves;

				}
			}
		}
	}

	public String ASCII_move(int i)
	{
		bmove.setMove(From[i],To[i],Piece[i],Taken[i],Promotes[i]);
		return bmove.ASCII_move(buffer);
	}

	public String printBestPath(String s)
	{
		d=0;
		sbuffer.setLength(0);
		sbuffer.append("<");

		while (best_path[0][d].piece>0)
		{
			sbuffer.append(best_path[0][d].ASCII_move(buffer));
			sbuffer.append(" ");
			d++;
		}
		sbuffer.setCharAt(sbuffer.length()-1,'>');
		s=sbuffer.toString();

		return s;
	}

	public void getKings()
	{
		if (gsdb.board[kingpos[Const.WHITE]]!=Const.WHITE_KING)
		{
			for (int a=0;a<81;a++) if ( gsdb.board[a]==Const.WHITE_KING ) { kingpos[Const.WHITE]=a; break; }
		}
		if (gsdb.board[kingpos[Const.BLACK]]!=Const.BLACK_KING)
		{
			for (int a=0;a<81;a++) if ( gsdb.board[a]==Const.BLACK_KING ) { kingpos[Const.BLACK]=a; break; }
		}
	}

	public boolean isWhiteCheck()
	{
		int p;
		int end;

		getKings();
		int pos=kingpos[Const.WHITE];
		// CHECK DOWN
		if (pos<72)
		{
			p=gsdb.board[pos + 9];
			if ( p==Const.BLACK_PAWN || p==Const.BLACK_LANCE || p==Const.BLACK_SILVER || p==Const.BLACK_GOLD || p==Const.BLACK_ROOK || p==Const.BLACK_KING || p==Const.BLACK_PPAWN || p==Const.BLACK_PLANCE || p==Const.BLACK_PKNIGHT || p==Const.BLACK_PSILVER || p==Const.BLACK_PBISHOP|| p==Const.BLACK_PROOK ) return true;
		}
		// CHECK UP
		if (pos>8)
		{
			p=gsdb.board[pos - 9];
			if ( p==Const.BLACK_GOLD || p==Const.BLACK_ROOK || p==Const.BLACK_KING || p==Const.BLACK_PPAWN || p==Const.BLACK_PLANCE || p==Const.BLACK_PKNIGHT || p==Const.BLACK_PSILVER || p==Const.BLACK_PBISHOP|| p==Const.BLACK_PROOK ) return true;
		}
		// CHECK LEFT
		if (col(pos)>0)
		{
			p=gsdb.board[pos - 1];
			if ( p==Const.BLACK_GOLD || p==Const.BLACK_ROOK || p==Const.BLACK_KING || p==Const.BLACK_PPAWN || p==Const.BLACK_PLANCE || p==Const.BLACK_PKNIGHT || p==Const.BLACK_PSILVER || p==Const.BLACK_PBISHOP|| p==Const.BLACK_PROOK ) return true;
		}
		// CHECK RIGHT
		if (col(pos)<8)
		{
			p=gsdb.board[pos + 1];
			if ( p==Const.BLACK_GOLD || p==Const.BLACK_ROOK || p==Const.BLACK_KING || p==Const.BLACK_PPAWN || p==Const.BLACK_PLANCE || p==Const.BLACK_PKNIGHT || p==Const.BLACK_PSILVER || p==Const.BLACK_PBISHOP|| p==Const.BLACK_PROOK ) return true;
		}
		// CHECK LEFT UP
		if (pos>8 && col(pos)>0)
		{
			p=gsdb.board[pos - 10];
			if (p==Const.BLACK_SILVER || p==Const.BLACK_BISHOP || p==Const.BLACK_KING || p==Const.BLACK_PBISHOP|| p==Const.BLACK_PROOK ) return true;
		}
		// CHECK RIGHT UP
		if (pos>8 && col(pos)<8)
		{
			p=gsdb.board[pos - 8];
			if (p==Const.BLACK_SILVER || p==Const.BLACK_BISHOP || p==Const.BLACK_KING || p==Const.BLACK_PBISHOP|| p==Const.BLACK_PROOK ) return true;
		}
		
		// CHECK LEFT DOWN
		if (pos<72 && col(pos)>0)
		{
			p=gsdb.board[pos +8];
			if ( p==Const.BLACK_SILVER || p==Const.BLACK_GOLD || p==Const.BLACK_BISHOP || p==Const.BLACK_KING || p==Const.BLACK_PPAWN || p==Const.BLACK_PLANCE || p==Const.BLACK_PKNIGHT || p==Const.BLACK_PSILVER || p==Const.BLACK_PBISHOP|| p==Const.BLACK_PROOK ) return true;
		}
		// CHECK RIGHT DOWN
		if (pos<72 && col(pos)<8)
		{
			p=gsdb.board[pos +10];
			if ( p==Const.BLACK_SILVER || p==Const.BLACK_GOLD || p==Const.BLACK_BISHOP || p==Const.BLACK_KING || p==Const.BLACK_PPAWN || p==Const.BLACK_PLANCE || p==Const.BLACK_PKNIGHT || p==Const.BLACK_PSILVER || p==Const.BLACK_PBISHOP|| p==Const.BLACK_PROOK ) return true;
		}
		// CHECK LEFT Const.KNIGHT
		if (pos<63 && col(pos)>0)
		{
			p=gsdb.board[pos +17];
			if ( p==Const.BLACK_KNIGHT ) return true;
		}
		// CHECK RIGHT Const.KNIGHT
		if (pos<63 && col(pos)<8)
		{
			p=gsdb.board[pos +19];
			if ( p==Const.BLACK_KNIGHT ) return true;
		}

		//
		// 
		// Now we need to check Lance, Bishop & Rook
		//
		//

		// BACKWARD : Lance & Rook
		for (u=1;u<9;u++)
		{
			end=pos+u*9;
			if (end<81)
			{
				p=gsdb.board[end];
				if (p==Const.BLACK_LANCE || p==Const.BLACK_ROOK || p==Const.BLACK_PROOK) return true;
				if (p!=Const.EMPTY) break;
			}
		}
		// UPWARD : Rook
		for (u=1;u<9;u++)
		{
			end=pos-u*9;
			if (end>0)
			{
				p=gsdb.board[end];
				if (p==Const.BLACK_ROOK || p==Const.BLACK_PROOK) return true;
				if (p!=Const.EMPTY) break;
			}
		}
		// LEFT : Rook
		for (u=1;col(pos)-u>=0;u++)
		{
			end=pos-u;
			p=gsdb.board[end];
			if (p==Const.BLACK_ROOK || p==Const.BLACK_PROOK) return true;
			if (p!=Const.EMPTY) break;
		}
		// RIGHT : Rook
		for (u=1;col(pos)+u<9;u++)
		{
			end=pos+u;
			p=gsdb.board[end];
			if (p==Const.BLACK_ROOK || p==Const.BLACK_PROOK) return true;
			if (p!=Const.EMPTY) break;
		}

		// Const.BISHOP : Right up
		for (u=1;col(pos)+u<9;u++)
		{
			end=pos-u*8;
			if (end<0) break;
			p=gsdb.board[end];
			if (p==Const.BLACK_BISHOP || p==Const.BLACK_PBISHOP) return true;
			if (p!=Const.EMPTY) break;
		}
		// Const.BISHOP : Right down
		for (u=1;col(pos)+u<9;u++)
		{
			end=pos+u*10;
			if (end>80) break;
			p=gsdb.board[end];
			if (p==Const.BLACK_BISHOP || p==Const.BLACK_PBISHOP) return true;
			if (p!=Const.EMPTY) break;
		}
		// Const.BISHOP : Left up
		for (u=1;col(pos)-u>=0;u++)
		{
			end=pos-u*10;
			if (end<0) break;
			p=gsdb.board[end];
			if (p==Const.BLACK_BISHOP || p==Const.BLACK_PBISHOP) return true;
			if (p!=Const.EMPTY) break;
		}
		// Const.BISHOP : Left down
		for (u=1;col(pos)-u>=0;u++)
		{
			end=pos+u*8;
			if (end>80) break;
			p=gsdb.board[end];
			if (p==Const.BLACK_BISHOP || p==Const.BLACK_PBISHOP) return true;
			if (p!=Const.EMPTY) break;
		}

		return false;
	}

	public String PSN(String b, Move m)
	{
		int matches;
		boolean canpromote=false;
		int mn=moveptr;
		boolean nc=no_checking;
		no_checking=true;
		generateMoves();
		no_checking=nc;
		String retval=null;

		matches=0;
		for (int i=mn;i<moveptr;i++)
		{
			if (m.piece==Piece[i] && m.to==To[i] && m.promotes==Promotes[i] && From[i]!=Const.DROP)
			{
				matches++;
			}
			if (m.from==From[i] && m.to==To[i] && Promotes[i])
			{
				canpromote=true;
			}
		}
		moveptr=mn;

		if (matches>1 || matches==0 || m.from==Const.DROP)
		{
			if (!gsdb.dec_frame.cmi_encesn.getState())
			{
				retval=m.ASCII_move(b);
			}
			else
			{
				retval=m.ASCII_move(b);
				int io;
				if (m.taken==Const.EMPTY) io=retval.indexOf('-'); else io=retval.indexOf('x');
				if (io>=0) retval=retval.substring(0,io)+retval.substring(io+1);
			}
		}
		else
		{
			//
			// Here we make a better 'PSN' move (Portable Shogi Notation)
			// P7g-7f  or  +B3c-2b  or +P2cx2b
			// P7f     or  +B2b     pr +Px2b
			//
			String s=new String(m.ASCII_move(b));
			String last,first;

			if (m.taken==Const.EMPTY)
			{
				last=s.substring( s.indexOf('-')+1, s.indexOf('-')+3 ); // 7f
			}
			else
			{
				last=s.substring( s.indexOf('x'), s.indexOf('x')+3 ); // x2b
			}
			if (gsdb.dec_frame.cmi_encesn.getState() && m.taken!=Const.EMPTY)
			{
				last=last.substring(1);
			}
			if (s.charAt(0)=='+')
				first=s.substring(0,2);
			else
				first=s.substring(0,1);
			retval=(first+last+(m.promotes?"+":(canpromote?"=":"")));
		}
		if (m.from==Const.DROP) retval=retval+" ";
		else
		if (matches==1 && retval.charAt(0)!='+' && !m.promotes ) retval=retval+"  ";
		else
		if (matches==1 && (retval.charAt(0)=='+' || m.promotes)) retval=retval+" ";

		return retval;
	}
}


