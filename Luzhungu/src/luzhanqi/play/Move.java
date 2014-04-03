package luzhanqi.play;

public class Move {

    /*
     * From position of the move
     */
	int fromIndex;
	/*
	 * To position of the move
	 */
	int toIndex;
	/*
	 * evaluation of the move as per weights
	 */
	double evalution;
	
	public Move(int fromIndex, int toIndex, double evalution) {
		super();
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
		this.evalution = evalution;
	}
	public int getFromIndex() {
		return fromIndex;
	}
	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}
	public int getToIndex() {
		return toIndex;
	}
	public void setToIndex(int toIndex) {
		this.toIndex = toIndex;
	}
	public double getEvalution() {
		return evalution;
	}
	public void setEvalution(double evalution) {
		this.evalution = evalution;
	}
	
	
}
