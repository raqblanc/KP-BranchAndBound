package branchAndBound;

public class Solucion {
	private int solMejor[];
	private float benefMejor;
	private float nodosExpandidos;
	public Solucion(int[] solMejor, float benefMejor, float nodosExpandidos) {

		this.solMejor= solMejor;

		this.benefMejor = benefMejor;
		this.nodosExpandidos = nodosExpandidos;
	}
	public int[] getSolMejor() {
		return solMejor;
	}
	public void setSolMejor(int[] solMejor) {
		this.solMejor = solMejor;
	}
	public float getBenefMejor() {
		return benefMejor;
	}
	public void setBenefMejor(float benefMejor) {
		this.benefMejor = benefMejor;
	}
	public float getNodosExpandidos() {
		return nodosExpandidos;
	}
	public void addNodoExpandido() {
		this.nodosExpandidos++;
	}


}
