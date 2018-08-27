import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Graphic extends Frame {
	public Listen listen;
	public boolean chooseTurnOrder = false;
	public boolean openingScreen = true;
	public boolean playerOneWins = false;
	public boolean playerTwoWins = false;
	public String text = "Reversi";
	private State curr_state;
	private final int width = 400;
	private final int margin = 50;
	private final int mini_margin = 5;
	private final int top_margin = 100;
	{
		char[] board = new char[16];
	      for (int i = 0; i < 16; i++) {
	          if (i == 5 || i == 10) {
	              board[i] = '2';
	          } else if (i == 6 || i == 9) {
	              board[i] = '1';
	          } else {
	              board[i] = '0';
	          }
	      }
	      this.curr_state = new State(board);		
	}
	
	public void updateBoard(State new_state) {
		curr_state = new_state;
		this.repaint();
	}
	
	public String get_move() {
		int a = (int) Math.ceil(4*(listen.x - margin) / (1.0*width));
		int b = (int) Math.ceil(4*(listen.y - top_margin) / (1.0*width));
		if(a < 1 || a > 4) {
			return "";
		} else if (b < 1 || b > 4) {
			return "";
		}
		return "" + (a + (b-1)*4);
	}
	
	public String num_players() {
		int a = listen.x;
		int b = listen.y;
		int min_x = (width + 2 * margin) * 1 / 4-75;
	    int min_y = (width + top_margin + margin) / 2 - 100;
	    if(b >= min_y && b <= min_y + 200) {
	    	if(a >= min_x && a <= min_x + 150) {
	    		chooseTurnOrder = true;
	    		openingScreen = false;
	    		return "1";
	    	}else if(a >= (min_x + 75)*3 - 75 && a <= (min_x + 75)*3 + 75) {
	    		chooseTurnOrder = true;
	    		openingScreen = false;
	    		return "2";
	    	}
	    }
		return "";
	}
	
	public String turnOrder() {
		int a = listen.x;
		int b = listen.y;
		int min_x = (width + 2 * margin) * 1 / 4-75;
	    int min_y = (width + top_margin + margin) / 2 - 100;
	    if(b >= min_y && b <= min_y + 200) {
	    	if(a >= min_x && a <= min_x + 150) {
	    		chooseTurnOrder = false;
	    		return "y";
	    	}else if(a >= (min_x + 75)*3 - 75 && a <= (min_x + 75)*3 + 75) {
	    		chooseTurnOrder = false;
	    		return "n";
	    	}
	    }
		return "";
	}
       
   public Graphic(){
      super("Reversi");
      prepareGUI();
   }
   
   public void init() {
		this.setVisible(true);
       this.text = "Welcome to Reversi";
       this.repaint();
	}

   private void prepareGUI(){
      setSize(width + margin * 2,width + top_margin +margin);
      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      }); 
      listen = new Listen();
      addMouseListener(listen);
   }  
   
   public void drawButton(Graphics g, int x, int y, int width, int height, 
		   String text, FontMetrics metrics) {
	   g.setColor(java.awt.Color.LIGHT_GRAY);
	   g.fillRect(x, y, width, height);
	   g.setColor(java.awt.Color.BLACK);
	   x = x + (width - metrics.stringWidth(text)) / 2;
	   y = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
	   g.drawString(text, x, y);
   }
   
   public void openingScreen(Graphics g, FontMetrics metrics) {
	   drawButton(g, (width + 2 * margin) * 1 / 4-75, 
			   (width + top_margin + margin) / 2 - 100, 150, 200, 
			   "Play First", metrics);
	   drawButton(g, (width + 2 * margin) * 3 / 4-75, 
			   (width + top_margin + margin) / 2 - 100, 150, 200, 
			   "Play Second", metrics);
	   return;
   }
   
   public void victoryScreen(Graphics g, String message, FontMetrics metrics) {
	   drawButton(g, margin, 100 + top_margin, width, 200, 
			   message, metrics);
   }
   
   public void mainScreen(Graphics g) {
	   int root = (int) Math.sqrt(this.curr_state.board.length);
	      for(int i = 0; i < root; i++) {
	    	  for(int j = 0; j < root; j++) {
	    		  g.clearRect(margin + j*width/root, top_margin+i*width/root, 
			    		  width/root, width/root);
		    	  g.drawRect(margin + j*width/root, top_margin+i*width/root, 
			    		  width/root, width/root);
	    		  switch(this.curr_state.board[i*root+j]) {
	    			  case '1':
	    				  g.setColor(java.awt.Color.BLACK);
	    				  break;
	    			  case '2':
	    				  g.setColor(java.awt.Color.GRAY);
	    				  break;
	    		      default:
	    		    	  continue;
	    		  }
	    		  g.fillRect(mini_margin + margin + j*width/root, 
	    				  mini_margin + top_margin+i*width/root, 
			    		  width/root-mini_margin*2, width/root-mini_margin*2);
		    	  continue;
	    	  }
	      }
   }

   @Override
   public void paint(Graphics g) {
	   g.setColor(Color.GRAY);
	      Font font = new Font("Serif", Font.PLAIN, 24);
	      FontMetrics metrics = g.getFontMetrics(font);
	      g.setFont(font);
	      g.drawString(this.text, 200, (int) top_margin*3/4);
	   if(openingScreen) openingScreen(g, metrics);
	   else if(playerOneWins) victoryScreen(g, "Player One Wins!!!", metrics);
	   else if(playerTwoWins) victoryScreen(g, "Player Two Wins!!!", metrics);
	   else mainScreen(g);
   }
}