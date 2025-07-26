package AA2024.uadadsidelgaumalu.Motor;

import java.io.*;
import java.util.*;

class estJugar {
	public boolean[] atr;
	public int va;

	public estJugar() {
		super();
		atr = new boolean[5];
	}

	public estJugar(Boolean atr0, Boolean atr1, Boolean atr2, Boolean atr3, Boolean atr4, int va) {
		super();
		this.atr[0] = atr0;
		this.atr[1] = atr1;
		this.atr[2] = atr2;
		this.atr[3] = atr3;
		this.atr[4] = atr4;
		this.va = va;
	}

	public void setAtr1(int i, Boolean atr1) {
		this.atr[i] = atr1;
	}

	public boolean getAtr(int i) {
		return this.atr[i];
	}

	public int getVa() {
		return va;
	}

	public void setVa(int va) {
		this.va = va;
	}

}

public class Fichero {

	private File f;
	private int numAtributos = 5, numAcciones = 4;
	private Map<Integer, Estado> conjEstadosEnt;
	private Map<Integer, estJugar> conjEstadosJugar;

	public Fichero(File f, boolean ent) {
		this.f = f;
		this.conjEstadosEnt = new HashMap<>();
		conjEstadosJugar = new HashMap<>();
		if (ent) {
			cargarEstadosDesdeFichero();
		} else {
			cargarEstadosDesdeFicheroJugar();
		}
	}

	private void cargarEstadosDesdeFicheroJugar() {
		f = new File("./src/AA2024/uadadsidelgaumalu/Tabla_Estados.txt");

		if (f.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				String linea;
				int id = 1;
				while ((linea = br.readLine()) != null) {
					String[] s = linea.trim().split(" ");
					estJugar e = new estJugar();
					for (int i = 0; i < numAtributos; i++) {
						e.setAtr1(i, Boolean.parseBoolean(s[i]));
					}
					int mejorAccion = 0;
					double max = Double.parseDouble(s[numAtributos]);
					for (int i = 1; i < numAcciones; i++) {
						if (Double.parseDouble(s[numAtributos + i]) > max) {
							max = Double.parseDouble(s[numAtributos + i]);
							mejorAccion = i;
						}
					}
					e.setVa(mejorAccion);
					conjEstadosJugar.put(id++, e);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (f.getParentFile() != null) {
					f.getParentFile().mkdirs();
				}
				if (f.createNewFile()) {
					System.out.println("Fichero creado: " + f.getAbsolutePath());
					try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
						bw.write("");
					}
				} else {
					System.out.println("No se pudo crear el fichero.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void cargarEstadosDesdeFichero() {
		if (f.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				String linea;
				int id = 1;
				while ((linea = br.readLine()) != null) {
					String[] s = linea.trim().split(" ");
					Estado estado = new Estado();
					for (int i = 0; i < estado.numAtributos; i++) {
						estado.atributos[i] = Boolean.parseBoolean(s[i]);
					}
					for (int i = 0; i < estado.numAcciones; i++) {
						estado.acciones[i] = Double.parseDouble(s[estado.numAtributos + i]);
					}
					conjEstadosEnt.put(id++, estado);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void guardarEstadosEnFichero(boolean b) {
		if (b)
			f = new File("./src/AA2024/uadadsidelgaumalu/Tabla_Estados.txt");
		else
			f = new File("./src/AA2024/uadadsidelgaumalu/Tabla_Estados_Jugar.txt");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
			if (b) {
				for (Map.Entry<Integer, Estado> entry : conjEstadosEnt.entrySet()) {
					Estado e = entry.getValue();
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < e.numAtributos; i++) {
						sb.append(e.atributos[i]).append(" ");
					}
					for (int i = 0; i < e.numAcciones; i++) {
						sb.append(e.acciones[i]).append(" ");
					}
					bw.write(sb.toString().trim());
					bw.newLine();
				}
			} else {
				for (Map.Entry<Integer, estJugar> entry : conjEstadosJugar.entrySet()) {
					estJugar e = entry.getValue();
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < numAtributos; i++) {
						sb.append(e.getAtr(i)).append(" ");
					}
					sb.append(e.getVa()).append(" ");
					bw.write(sb.toString().trim());
					bw.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int numEstados() {
		return conjEstadosEnt.size();
	}

	public Estado devolverEstado(int id) {
		return conjEstadosEnt.getOrDefault(id, new Estado());
	}

	public int devolverAccionJugar(int id) {
		estJugar e = conjEstadosJugar.getOrDefault(id, new estJugar());
		return e.getVa();
	}

	public int encontrarEst(Estado es, boolean b) {
		if (b) {
			for (Map.Entry<Integer, Estado> entry : conjEstadosEnt.entrySet()) {
				Estado e = entry.getValue();
				boolean encontrado = true;
				for (int i = 0; i < es.numAtributos; i++) {
					if (e.atributos[i] != es.atributos[i]) {
						encontrado = false;
						break;
					}
				}
				if (encontrado) {
					return entry.getKey();
				}
			}
		} else {
			for (Map.Entry<Integer, estJugar> entry : conjEstadosJugar.entrySet()) {
				estJugar e = entry.getValue();
				boolean encontrado = true;
				for (int i = 0; i < es.numAtributos; i++) {
					if (e.getAtr(i) != es.atributos[i]) {
						encontrado = false;
						break;
					}
				}
				if (encontrado) {
					return entry.getKey();
				}
			}
		}
		return -1;
	}

	public int anadirEstado(Estado e) {
		int id = conjEstadosEnt.size() + 1;
		conjEstadosEnt.put(id, e);
		return id;
	}

	public void actualizarEstado(int id, Estado e) {
		if (conjEstadosEnt.containsKey(id)) {
			conjEstadosEnt.put(id, e);
		}
	}

	public void generarFichero() {
		String fich_S = "./src/AA2024/uadadsidelgaumalu/Tabla_Estados_Jugar.txt";
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fich_S))) {
			for (Map.Entry<Integer, Estado> entry : conjEstadosEnt.entrySet()) {
				Estado e = entry.getValue();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < numAtributos; i++) {
					sb.append(e.atributos[i]).append(" ");
				}
				int mejorAccion = 0;
				double max = e.acciones[0];
				for (int i = 1; i < e.numAcciones; i++) {
					if (e.acciones[i] > max) {
						max = e.acciones[i];
						mejorAccion = i;
					}
				}
				sb.append(mejorAccion);
				bw.write(sb.toString());
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/*
 * import java.io.BufferedReader;
 * 
 * import java.io.BufferedWriter;
 * 
 * import java.io.File; import java.io.FileReader; import java.io.FileWriter;
 * import java.io.IOException; import java.util.Arrays; import
 * java.util.Scanner; import java.util.StringTokenizer;
 * 
 * import javax.sound.sampled.BooleanControl;
 * 
 * import java.lang.invoke.*;
 * 
 * public class Fichero {
 * 
 * private File f; private int x; int numAtributos = 5;
 * 
 * public Fichero(File f) { if (!f.exists()) { try { this.f = f; BufferedWriter
 * bw = new BufferedWriter(new FileWriter(this.f)); bw.flush(); bw.close(); x =
 * 0; } catch (IOException e) { e.printStackTrace(); } } else { this.f = f; x =
 * cuentaEstados(); }
 * 
 * }
 * 
 * private int cuentaEstados() { int i = 0; try (BufferedReader buff = new
 * BufferedReader(new FileReader(this.f))) { while (buff.readLine() != null) {
 * i++; } } catch (IOException e) { e.printStackTrace(); } return i; }
 * 
 * public void generarFichero() throws IOException { String fich_S =
 * "./src/AA2024/uadadsidelgaumalu/Tabla_Estados_Jugar.txt"; if (this.f == null
 * || !this.f.exists()) { System.err.println("el archivo no exiset."); return; }
 * 
 * try (BufferedReader br = new BufferedReader(new FileReader(this.f));
 * BufferedWriter bw = new BufferedWriter(new FileWriter(fich_S))) {
 * 
 * String linea; while ((linea = br.readLine()) != null) { String[] s =
 * linea.trim().split(" "); String[] atributos = Arrays.copyOfRange(s, 0,
 * numAtributos);
 * 
 * double max = Double.parseDouble(s[5]); int j = 0; for (int i = numAtributos +
 * 1; i < s.length; i++) { double acc = Double.parseDouble(s[i]);
 * //System.out.println(" M:  " + acc +" "+ i); if (acc >= max) { max = acc; j =
 * i - numAtributos; } } //System.out.println(" M:  " + j); StringBuilder aux =
 * new StringBuilder(); for (String atributo : atributos) {
 * aux.append(atributo).append(" "); }
 * aux.append(j).append(System.lineSeparator()); bw.write(aux.toString()); } }
 * catch (IOException e) { e.printStackTrace(); } }
 * 
 * public int devolverEstadoJugar(int i) { if (i < 1 || i > x) return -1;
 * 
 * try (BufferedReader br = new BufferedReader(new FileReader(this.f))) { int j
 * = 1; String linea; while ((linea = br.readLine()) != null) { if (j == i) {
 * String[] s = linea.trim().split(" "); if (s.length > numAtributos) { return
 * Integer.parseInt(s[numAtributos]); } } j++; } } catch (IOException e) {
 * e.printStackTrace(); } System.out.println(" -1"); return -1; }
 * 
 * public int encontrarEst(Estado es) { int i = -1, j = 1; try (BufferedReader
 * br = new BufferedReader(new FileReader(this.f))) { String linea; while
 * ((linea = br.readLine()) != null) { String[] s = linea.split(" "); boolean
 * encontrado = true; for (int k = 0; k < es.numAtributos; k++) { if
 * (es.atributos[k] != Boolean.parseBoolean(s[k])) { encontrado = false; break;
 * } } if (encontrado) { i = j; break; } j++; } } catch (IOException e) {
 * e.printStackTrace(); } return i; }
 * 
 * public Estado devolverEstado(int i) { Estado e = new Estado(); if (i < 1 || i
 * > x) return e;
 * 
 * try (BufferedReader br = new BufferedReader(new FileReader(this.f))) { int j
 * = 1; String linea; while ((linea = br.readLine()) != null) { if (j == i) {
 * String[] s = linea.trim().split(" "); for (int k = 0; k < (e.numAtributos +
 * e.numAcciones); k++) { if (k < e.numAtributos) { e.atributos[k] =
 * Boolean.parseBoolean(s[k]); } else { e.acciones[k - e.numAtributos] =
 * Double.parseDouble(s[k]); } } break; } j++; } } catch (IOException ex) {
 * ex.printStackTrace(); } return e; }
 * 
 * public int andirEstado(Estado e) { try (BufferedWriter bw = new
 * BufferedWriter(new FileWriter(this.f, true))) { StringBuilder estadoBuilder =
 * new StringBuilder(); for (int k = 0; k < e.numAtributos; k++) {
 * estadoBuilder.append(e.atributos[k]).append(" "); } for (int k = 0; k <
 * e.numAcciones; k++) { estadoBuilder.append(e.acciones[k]).append(" "); }
 * estadoBuilder.append(System.lineSeparator());
 * bw.write(estadoBuilder.toString()); x++; } catch (IOException ex) {
 * ex.printStackTrace(); } return x; }
 * 
 * public int numEstados() { return x; }
 * 
 * public void andirEstado(int i, Estado e) { try { BufferedReader br = new
 * BufferedReader(new FileReader(this.f)); StringBuilder sb = new
 * StringBuilder(); String linea; int j = 1; while ((linea = br.readLine()) !=
 * null) { if (j == i) { String[] s = linea.trim().split(" "); for (int k = 0; k
 * < e.numAcciones; k++) { s[numAtributos + k] = String.valueOf(e.acciones[k]);
 * } sb.append(String.join(" ", s)).append(System.lineSeparator()); } else {
 * sb.append(linea).append(System.lineSeparator()); } j++; } br.close();
 * BufferedWriter bw = new BufferedWriter(new FileWriter(this.f));
 * bw.write(sb.toString()); bw.close(); } catch (IOException ex) {
 * ex.printStackTrace(); } } }
 */

/*
 * public void generarFichero() throws IOException { String fich_S =
 * "./uadadsidelgaumalu/Tabla_Estados_Jugar.txt"; if (this.f == null ||
 * !this.f.exists()) { System.err.println("el archivo no exiset."); return; }
 * 
 * Scanner scanner = new Scanner(this.f); FileWriter w = new FileWriter(fich_S);
 * while (scanner.hasNextLine()) { String linea = scanner.nextLine().trim();
 * String[] s = linea.split(" ");
 * 
 * String[] atributos = new String[numAtributos]; System.arraycopy(s, 0,
 * atributos, 0, numAtributos); double max = Double.MIN_VALUE; int j = -1; for
 * (int i = numAtributos; i < s.length; i++) { double acc =
 * Double.parseDouble(s[i]); if (acc > max) { max = acc; j = i - numAtributos; }
 * } StringBuilder aux = new StringBuilder(); for (String atributo : atributos)
 * { aux.append(atributo).append(" "); }
 * aux.append(j).append(System.lineSeparator()); w.write(aux.toString()); } }
 * 
 * public int devolverEstadoJugar(int i) { try { if (this.f != null) { Scanner
 * scanner = new Scanner(this.f); int j = 1; // boolean[] atributos = new
 * boolean[e.numAtributos]; // double[] acciones = new double[e.numAcciones];
 * while (scanner.hasNextLine()) { String linea = scanner.nextLine().trim();
 * String[] s = linea.split(" "); if (j == i) { if (s.length > numAtributos) {
 * int acc = Integer.parseInt(s[numAtributos]); scanner.close(); return acc; }
 * // e.atributos=atributos; // e.acciones=acciones; break; } j++; }
 * scanner.close(); } } catch (Exception ex) { ex.printStackTrace(); } return
 * -1; }
 * 
 * public int encontrarEst(Estado es) { int i = -1, j = 1; try { if (this.f !=
 * null) { Scanner scanner = new Scanner(this.f); while (scanner.hasNextLine())
 * { String linea = scanner.nextLine(); String[] s = linea.split(" "); boolean
 * encontrado = true; for (int k = 0; k < es.numAtributos; k++) { if
 * (es.atributos[k] != Boolean.parseBoolean(s[k])) { encontrado = false; break;
 * } } if (encontrado) { i = j; break; } j++; } scanner.close(); } } catch
 * (Exception ex) { ex.printStackTrace(); } return i; }
 * 
 * public Estado devolverEstado(int i) { Estado e = new Estado();
 * 
 * try { if (this.f != null) { Scanner scanner = new Scanner(this.f); int j = 1;
 * // boolean[] atributos = new boolean[e.numAtributos]; // double[] acciones =
 * new double[e.numAcciones]; while (scanner.hasNextLine()) { String linea =
 * scanner.nextLine().trim(); String[] s = linea.split(" "); if (j == i) { int k
 * = 0; while (k < (e.numAtributos + e.numAcciones)) { if (k < e.numAtributos) {
 * e.atributos[k] = Boolean.parseBoolean(s[k]); } else { e.acciones[k -
 * e.numAtributos] = Double.parseDouble(s[k]); } k++; } //
 * e.atributos=atributos; // e.acciones=acciones; break; } j++; }
 * scanner.close(); } } catch (Exception ex) { ex.printStackTrace(); } return e;
 * }
 * 
 * public void andirEstado(int i, Estado e) { try (FileWriter fw = new
 * FileWriter(this.f, true); BufferedWriter bw = new BufferedWriter(fw)) {
 * StringBuilder estadoBuilder = new StringBuilder(); for (int k = 0; k <
 * e.numAtributos; k++) { estadoBuilder.append(e.atributos[k]).append(" "); }
 * for (int k = 0; k < e.numAcciones; k++) {
 * estadoBuilder.append(e.acciones[k]).append(" "); }
 * estadoBuilder.append(System.lineSeparator());
 * bw.write(estadoBuilder.toString()); x++; } catch (IOException ex) {
 * ex.printStackTrace(); } }
 * 
 * public int numEstados() { return x; }
 */
