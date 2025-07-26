package AA2024.uadadsidelgaumalu.Motor;

import java.util.Arrays;


import java.util.Random;

public class Estado {

	public boolean[] atributos;
	public int numAtributos = 5;
	public double[] acciones;
	public int numAcciones = 4;

	public Estado() {
	    atributos = new boolean[numAtributos];
	    acciones = new double[numAcciones];
	}

	public Estado(boolean[] e) {
	    if (e.length != numAtributos) {
	        throw new IllegalArgumentException("El tamaño del array de atributos tieen q ser: " + numAtributos);
	    }
	    atributos = Arrays.copyOf(e, numAtributos);
	    acciones = new double[numAcciones];
	    Random r = new Random(System.currentTimeMillis());
	    for (int i = 0; i < acciones.length; i++) {
	        acciones[i] = r.nextDouble() * 0.5;
	    }
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;

	    Estado estado = (Estado) obj;
	    return Arrays.equals(atributos, estado.atributos);
	}

	@Override
	public int hashCode() {
	    return Arrays.hashCode(atributos);
	}

	@Override
	public String toString() {
	    return "Estado{" +
	           "atributos=" + Arrays.toString(atributos) +
	           ", acciones=" + Arrays.toString(acciones) +
	           '}';
	}

}
