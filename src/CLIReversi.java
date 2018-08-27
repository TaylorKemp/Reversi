import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.awt.Graphics;

@SuppressWarnings("serial")
class InvalidMoveException extends Exception {}

public class CLIReversi {
    @SuppressWarnings("resource")
    public static void main(String args[]) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Graphic g = new Graphic();
        g.init();
      
        String player = "";
        do {
            player = g.num_players();
            TimeUnit.MICROSECONDS.sleep(100);
            continue;
        } while (!player.equals("1") && !player.equals("2"));
        String computer = (player.equals("1")) ? "2": "1";
        
        // Specify who play first
        Boolean human_first = null; // note: nullable boolean
        g.listen.x = 0;
        g.listen.y = 0;
        g.repaint();
        
        human_first = (player.equals("1")) ? true: false;
        
        // Init board
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
        State curr_state = new State(board);
        boolean opponent_cant_move = false;
        g.listen.x = 0;
        g.listen.y = 0;
        if (!human_first) {
            curr_state = getOptimalMove(curr_state, computer.charAt(0));
            g.updateBoard(curr_state);
            g.repaint();
        }
     
        do {
            // Human's turn
            if (curr_state.isTerminal()) break;
            if (curr_state.getSuccessors(player.charAt(0)).length == 0 ) {
                System.out.println(String.format("Player %s can't move", player));
                if(opponent_cant_move) break;
            } else {
            	opponent_cant_move = false;
                String move;
                do {
                    move = "";
                    boolean isEmpty = false;
                    TimeUnit.MICROSECONDS.sleep(100);
                    move = g.get_move();
                    try {
                        isEmpty = isPosEmpty(move, curr_state);
                        if (isEmpty) {
                            int move_pos = Integer.parseInt(move) - 1; 
                            State temp = updateState(curr_state, move_pos, player.charAt(0));
                            if (temp == null) throw new Exception();
                            else {
                                curr_state = temp;
                                g.updateBoard(curr_state);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        continue;
                    }
                } while (true);
            }
            
            // Computer's turn
            if (curr_state.isTerminal()) break;
            if (curr_state.getSuccessors(computer.charAt(0)).length == 0 ) {
                System.out.println(String.format("Player %s can't move", computer));
                opponent_cant_move = true;
            } else {
                curr_state = getOptimalMove(curr_state, computer.charAt(0));
                g.updateBoard(curr_state);
            }
        } while (true);
        TimeUnit.SECONDS.sleep(2);
        int final_score = curr_state.getScore();
        if (final_score == 0) System.out.println("Tied Game!");
        else if (final_score == 1) g.playerOneWins = true;
        else if (final_score == -1) g.playerTwoWins = true;
        else throw new Exception();
        g.repaint();
        
        scanner.close(); // close scanner
    }
    
    private static State updateState (State curr_state, int move_pos, char player) {
        for (State s : curr_state.getSuccessors(player)) {
            if (s.getBoard().charAt(move_pos) != '0') return s;
        }
        return null;
    }
    
    private static boolean isPosEmpty (
            String move, 
            State curr_state) throws InvalidMoveException {
        String board = curr_state.getBoard();
        try {
            int move_in_int = Integer.parseInt(move);
            if (move_in_int < 1 || move_in_int > 16) return false;
            char char_at_pos = board.charAt(move_in_int - 1);
            if (char_at_pos != '0') return false;
            else return true;
        } catch (Exception e) {
            throw new InvalidMoveException();
        }  
    }
    
    private static State getOptimalMove (
            State curr_state, 
            char player) {

        if (curr_state.isTerminal()) return null;
        
        State[] successors = curr_state.getSuccessors(player);
        if (successors.length == 0) return null;
        else {
            State opt_state = null;
            int opt_val;
            if (player == '1') opt_val = Integer.MIN_VALUE;
            else opt_val = Integer.MAX_VALUE;
            for (State s : curr_state.getSuccessors(player)) {
                if (player == '1') {
                    int curr_val = 0; // default - harmless
                    curr_val = Minimax.run_with_pruning(s, '2');
                    
                    if (curr_val > opt_val) {
                        opt_val = curr_val;
                        opt_state = new State(s.getBoard().toCharArray());
                    }
                } else {
                    int curr_val = 0;
                    curr_val = Minimax.run_with_pruning(s, '1');
                  
                    if (curr_val < opt_val) { 
                        opt_val = curr_val;
                        opt_state = new State(s.getBoard().toCharArray());
                    }
                }
            }   
            return opt_state;
        }
    }
}
