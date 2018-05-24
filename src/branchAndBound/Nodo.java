package branchAndBound;

/**
 * Clase necesaria para la gestión del árbol en los algoritmos de ramificación y poda
 * @author raqblanc
 *
 */
public class Nodo {
	private int solucion[];
	private int k;
	private float peso;
	private float beneficio;
	private float beneficioOpt;
	
	public Nodo(int k, int n){
		this.k = k;
		this.peso = 0;
		this.beneficio = 0;
		this.beneficioOpt = 0;
		this.solucion = new int[n];
	}
	
	
	public void setSolucion(int[] sol) {
		//copia
		for (int i = 0; i < solucion.length; i++) {
			this.solucion[i] = sol[i];
		}
	}
	public void setPeso(float peso) {
		this.peso = peso;
	}
	public void setBeneficio(float beneficio) {
		this.beneficio = beneficio;
	}
	public void setBeneficioOpt(float beneficioOpt) {
		this.beneficioOpt = beneficioOpt;
	}
	public int[] getSolucion() { 
		//copia
		int tam = this.solucion.length;
		int sol[] = new int[tam];
		for (int i = 0; i < tam; i++) {
			sol[i] = this.solucion[i];
		}
		return sol;
	}
	public void setiEsimaSolucion(int i, int valor) {
		this.solucion[i] = valor;
	}
	public int getK() {
		return k;
	}
	public float getPeso() {
		return peso;
	}
	public float getBeneficio() {
		return beneficio;
	}
	public float getBeneficioOpt() {
		return beneficioOpt;
	}
}
