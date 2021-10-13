
public class Treenode {
	private Point item;
	private Treenode l;
	private Treenode r;
	
	public Treenode() {}
	
	public Treenode(Point item) {
		this.item = item;
	}
	
	public Point getData() {
		return item;
	}
	
	public void setData(Point item) {
		this.item = item;
	}
	
	public Treenode getLeft() {
		return l;
	}
	
	public void setLeft(Treenode left) {
		this.l = left;
	}
	
	public Treenode getRight() {
		return r;
	}
	
	public void setRight(Treenode right) {
		this.r = right;
	}
}
