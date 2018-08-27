public class Minimax {
	public static int count = 0;
	
	private static int max_value_with_pruning(State curr_state, int alpha, int beta) {
	// Gets the max value possible at a node
		int score = 0;
		count++;
		if(curr_state.isTerminal()) {
			return curr_state.getScore();
		}
		State[] successors = curr_state.getSuccessors('1');
		if(successors.length == 0) {
			if(curr_state.getSuccessors('2').length != 0) {
				return min_value_with_pruning(curr_state, alpha, beta);
			} else {
				return curr_state.getScore();
			}
		}
		for(int i = 0; i < successors.length; i++) {
			score = min_value_with_pruning(successors[i], alpha, beta);
			alpha = Math.max(score, alpha);
			if(alpha >= beta) {
				return beta;
			}
		}
		
		return alpha;

	}
	
	private static int min_value_with_pruning(State curr_state, int alpha, int beta) {
	// Gets the min value possible at a node
		int score = 0;
		count++;
		if(curr_state.isTerminal()) {
			return curr_state.getScore();
		}
		State[] successors = curr_state.getSuccessors('2');
		if(successors.length == 0) {
			if(curr_state.getSuccessors('1').length != 0) {
				return max_value_with_pruning(curr_state, alpha, beta);
			} else {
				return curr_state.getScore();
			}
		}
		for(int i = 0; i < successors.length; i++) {
			score = max_value_with_pruning(successors[i], alpha, beta);
			beta = Math.min(score, beta);
			if(alpha >= beta) {
				return alpha;
			} 
		}
		
		return beta;

	}
	
	public static int run_with_pruning(State curr_state, char player) {
	// Finds the optimal move for a given board state and player
		if(player == '1') {
			return max_value_with_pruning(curr_state, -1000, 1000);
		} else {
			return min_value_with_pruning(curr_state, -1000, 1000);
		}
	}
}
