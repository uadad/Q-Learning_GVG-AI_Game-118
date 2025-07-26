package AA2024.uadadsidelgaumalu.Motor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import ontology.Types.ACTIONS;

public class Motor {
	private Fichero fich;
	private Estado estAnterior, estActual;
	private int ant = 0, act = 0, acc = -1;
	public double exp = 0.3;
	private Random r = new Random();
	public static Motor Instancia = null;
	private boolean b;
	private ArrayList<Integer> Ganados;
	private ArrayList<Double> conjExploraciones;
	private ArrayList<String[]> estados;

	public Motor(Mundo mundo, File f, boolean b) throws IOException {
		this.b = b;
		fich = new Fichero(f, b);
		Ganados = new ArrayList<Integer>();
		conjExploraciones = new ArrayList<Double>();
		estados = new ArrayList<String[]>();
	}

	public ACTIONS Jugar(Mundo m, Estado e, boolean b) {
		int i = fich.encontrarEst(e, b);
		this.b = b;
		if (i != -1)
			acc = fich.devolverAccionJugar(i);

		else {
			acc = r.nextInt(3);
		}
		switch (acc) {
		case 0:
			return ACTIONS.ACTION_RIGHT;
		case 1:
			return ACTIONS.ACTION_LEFT;
		case 2:
			return ACTIONS.ACTION_USE;
		default:
			return ACTIONS.ACTION_NIL;
		}
	}

	public ACTIONS Entrenar(Mundo m, Estado e) {
		ant = act;
		b = true;
		int i = fich.encontrarEst(e, b);
		if (i == -1)
			i = fich.anadirEstado(e);
		act = i;
		estActual = fich.devolverEstado(act);
		if (ant != 0) {
			float alfa = 0.1f;
			float gamba = 0.5f;

			estAnterior = fich.devolverEstado(ant);

			estAnterior.acciones[acc] = estAnterior.acciones[acc] + alfa * (calculaRecompensa(m, estActual, estAnterior)
					+ (gamba * obtenerMejorValor(estActual)) - estAnterior.acciones[acc]);

			fich.actualizarEstado(ant, estAnterior);
		}
		estados.add(new String[] { String.valueOf(estActual.atributos[0]), String.valueOf(estActual.atributos[1]),
				String.valueOf(estActual.atributos[2]), String.valueOf(estActual.atributos[3]),
				String.valueOf(estActual.atributos[4]) });
		Random r = new Random();
		double x = r.nextDouble();
		if (x < exp) {
			acc = r.nextInt(3);
		} else {
			// System.out.println("S13");
			acc = obtenerMejorAccion(estActual);
		}
		// System.out.println("S14");
		switch (acc) {
		case 0:
			return ACTIONS.ACTION_RIGHT;
		case 1:
			return ACTIONS.ACTION_LEFT;
		case 2:
			return ACTIONS.ACTION_USE;
		default:
			return ACTIONS.ACTION_NIL;
		}
	}

	private int obtenerMejorAccion(Estado estado) {
		int j = 0;
		double max = estado.acciones[0];
		for (int i = 0; i < estado.acciones.length; i++) {
			if (estado.acciones[i] > max) {
				max = estado.acciones[i];
				j = i;
			}
		}
		// System.out.println("M: "+max + " "+ j);
		return j;
	}

	private double obtenerMejorValor(Estado estado) {
		double max = estado.acciones[0];
		for (int i = 1; i < estado.acciones.length; i++) {
			if (estado.acciones[i] > max) {
				max = estado.acciones[i];
			}
		}
		// System.out.println(" "+ max);
		return max;
	}

	public double calculaRecompensa(Mundo m, Estado Act, Estado Ant) {

		if (Ant.atributos[3]) {
			if (Ant.atributos[4] && !Act.atributos[4])
				return 5.0f;
			else if (!Ant.atributos[4] && !Act.atributos[0])
				return -4.0f;
			else if (!Ant.atributos[4] && m.posAntX < m.posX)
				return 5.0f;
			else if (Ant.atributos[4] && m.posAntX < m.posX)
				return -4.0f;
			else if (Ant.atributos[4] && !Act.atributos[0])
				return 5.0f;
			else
				return -4.0f;
		}

		if (Ant.atributos[1]) {
			if (Ant.atributos[3] && !Act.atributos[3])
				return 5.0f;
			else if (!Ant.atributos[3] && !Act.atributos[0])
				return -4.0f;
			else if (!Ant.atributos[3] && m.posAntX < m.posX)
				return 5.0f;
			else if (Ant.atributos[3] && m.posAntX > m.posX)
				return -4.0f;
			else if (Ant.atributos[3] && !Act.atributos[0])
				return 5.0f;
			else
				return -4.0f;
		}

		if (Ant.atributos[2]) {
			if (Ant.atributos[4] && !Act.atributos[4])
				return 5.0f;
			else if (!Ant.atributos[4] && !Act.atributos[0])
				return -4.0f; // (!Ant.atributos[6] || !Ant.atributos[5])
			else if (!Ant.atributos[4] && m.posAntX > m.posX)
				return 5.0f;
			else if (Ant.atributos[4] && m.posAntX < m.posX)
				return -4.0f;
			else if (Ant.atributos[4] && !Act.atributos[0])
				return 5.0f;
			else
				return -4.0f;

		}

		return -4.0f;

	}

	public static Motor devolverInstancia(Mundo mundo, File fich, boolean entrenar) throws IOException {
		// TODO Auto-generated method stub
		if (Instancia == null) {
			Instancia = new Motor(mundo, fich, entrenar);
		}
		return Instancia;
	}

	public void guardarEstados() {
		this.fich.guardarEstadosEnFichero(b);
	}

	public void guardarGrafica(String filename) {

		try (PrintWriter writer = new PrintWriter(new File(filename))) {
			StringBuilder sb = new StringBuilder();
			sb.append("Iteraciones,Ganados,Exploracion\n");
			for (int i = 0; i < Ganados.size(); i++) {
				sb.append((i + 1) * 20).append(",").append(Ganados.get(i)).append(",").append(conjExploraciones.get(i))
						.append("\n");
			}
			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void guardarHistograma(String filename) {
       
		try (PrintWriter writer = new PrintWriter(new File(filename))) {
			StringBuilder sb = new StringBuilder();
			sb.append("Estados\n");
			for (int i = 0; i < this.estados.size(); i++) {
				sb.append(String.join(",", estados.get(i))).append("\n");
			}
			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void decExp() {
		// TODO Auto-generated method stub
		if (exp > 0.00075)
			exp -= 0.00075;
	}

	public void añadirGanados(int i) {
		// TODO Auto-generated method stub
		Ganados.add(i);
	}

	public void añadirExploracion() {
		conjExploraciones.add(exp);
	}
}
