import java.util.*;
import java.lang.Math;

class State {
//Contains information for the current board setup
//Controls updates to board
    char[] board;
    int score = -1000;

    public State(char[] arr) {
        this.board = Arrays.copyOf(arr, arr.length);
    }

    public int getScore() {
    // Returns current value of board
    	int score = 0;
    	for(int i = 0; i < board.length; i++) {
    		if(board[i] == '1') {
    			score++;
    		} else if(board[i] == '2') {
    			score--;
    		}
    	}
    	if(score < 0) return -1;
    	return Math.min(1, score);
    }
    
    public boolean isTerminal() {  	
    // Checks if board state has any potential moves left
    	for(int i = 0; i < this.board.length; i++) {
    		if(this.board[i] == '0') return false;
    	}

        return true;
    }
    
    private boolean updateValues(State newBoard, Compare condition, 
    		char[] board, int start, int index, int inc, int i, char player) {
    // Updates board in a provided direction ie flips tiles above current move
    	boolean someUpdate = false;
    	boolean isChanged = false;
    	
    	for(int j = start; condition.test(j, index); j+=inc) {
    		if(newBoard.board[j] == '0') {
    			index = i;
    		} else if(newBoard.board[j] == player) {
    			if(someUpdate) {
    				isChanged = true;
    			}
    			index = j;
    		} else {
    			if(j != index) someUpdate = true;
    			else someUpdate = false;
    		}
    	}
    	if(someUpdate) {
    		for(int j = i;condition.test(j, index); j+=inc) {
    			newBoard.board[j] = player;
    		}
    		someUpdate = false;
    	}
    	
    	return isChanged;  
    	
    }
  
    private State getNewBoard(char[] board, int i, char opponent, char player) {
    //Gets a new board based on a move made
    	State newBoard = new State(board);
    	int index = -1;
    	boolean isChanged = false;
    	Compare less_than_equal = new Compare(
    			(Integer arg0, Integer arg1)->{return arg0 <= arg1;}
    			);
    	Compare more_than_equal = new Compare(
    			(Integer arg0, Integer arg1)->{return arg0 >= arg1;}
    			);
    	
    	//to left
    	isChanged = updateValues(
    			newBoard, more_than_equal, board,i-1, i&-4, -1, i, player) ? 
    					true:isChanged;
    	//to right
    	isChanged = updateValues(
    			newBoard,less_than_equal, board,i+1, (i&-4)+3, 1, i, player) ? 
    					true:isChanged;
    	//below
    	isChanged = updateValues(
    			newBoard, less_than_equal, board,i+4, (i&3)+12, 4, i, player) ?
    					true:isChanged;
    	//above
    	isChanged = updateValues(
    			newBoard, more_than_equal, board,i-4, (i&3), -4, i, player) ?
    					true:isChanged;
    	//upper left diagonal
    	index = i - 5 * Math.min((i >> 2), (i & 3));
    	isChanged = updateValues(
    			newBoard, more_than_equal, board,i-5, index, -5, i, player) ?
    					true:isChanged;
    	//bottom left diagonal
    	index = i - 3 * Math.min((i >> 2), 3 - (i & 3));//top left diag end
    	isChanged = updateValues(
    			newBoard, more_than_equal, board,i-3, index, -3, i, player) ? 
    					true:isChanged;
    	//upper right diagonal
    	index = i + 5 * Math.min(3 - (i >> 2), 3 - (i & 3));//bot right diag
    	isChanged = updateValues(
    			newBoard, less_than_equal, board,i+5, index, 5, i, player) ?
    					true:isChanged;
    	//bottom right diagonal
    	index = i + 3 * Math.min(3 - (i >> 2), (i & 3));//bot left diag
    	isChanged = updateValues(
    			newBoard, less_than_equal, board,i+3, index, 3, i, player) ? 
    					true:isChanged;
    	
    	if(isChanged) return newBoard;
    	return null;
    }
    
    public State[] getSuccessors(char player) {
    // Gets all possible moves from current board position
    	char opponent = '1';
    	State[] successors = new State[16];
    	State successor = null;
    	int index = 0;
    	
    	if(player == '1') opponent = '2';

    	for(int i = 0; i < this.board.length; i++) {
    		if(this.board[i] == '0') {
    			successor = getNewBoard(this.board, i, opponent, player);
    			if(successor != null) successors[index++] = successor;
    		}
    	}
    	
    	return Arrays.copyOf(successors, index);
    }
 
    public String getBoard() {
    //Gets a string version of the board
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            builder.append(this.board[i]);
        }
        return builder.toString().trim();
    }
}
