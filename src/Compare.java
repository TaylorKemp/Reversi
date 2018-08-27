import java.util.function.BiPredicate;

public class Compare {
	private BiPredicate<Integer, Integer> function;
	
	public Compare(BiPredicate<Integer, Integer> function) {
		this.function = function;
	}
	
	public boolean test(Integer arg0, Integer arg1) {
		return this.function.test(arg0, arg1);
	}
}
