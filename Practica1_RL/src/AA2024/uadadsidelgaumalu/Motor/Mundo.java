package AA2024.uadadsidelgaumalu.Motor;

import java.util.ArrayList;
import core.game.Observation;
import core.game.StateObservation;
import tools.Vector2d;

public class Mundo {
	public ArrayList<Observation>[][] entorno;
	public int nf, nc, bloque, posX = 0, posY = 0, posAntX = 0, posAntY = 0, numAtributos = 5;
	public Vector2d avatarPos, meta, mon_Cercana;
	public int[][] mapa;
	public StateObservation stateObs = null;
	public int numM = 0;
	public boolean suelo = false, metaIzq = false, metaDer = false, obsDetras = false, obsDelante = false;

	public Mundo(StateObservation stateObs) {
		this.stateObs = stateObs;
		entorno = stateObs.getObservationGrid();
		nf = entorno[0].length;
		nc = entorno.length;
		bloque = stateObs.getBlockSize();
	}

	public Estado analizaMundo(StateObservation stateObs) {

		this.stateObs = stateObs;

		suelo = false;
		metaIzq = false;
		metaDer = false;
		obsDelante = false;
		obsDetras = false;
		mapa = new int[nc][nf];
		avatarPos = stateObs.getAvatarPosition();

		ArrayList<Observation>[][] entorno = stateObs.getObservationGrid();
		ArrayList<Vector2d> monedas = new ArrayList<>();
		for (int i = 0; i < nf; i++) {
			for (int j = 0; j < nc; j++) {
				ArrayList<Observation> contenido = entorno[j][i];
				if (!contenido.isEmpty()) {
					Observation prop = contenido.get(0);
					int itype = prop.itype;
					mapa[j][i] = itype;
					if (itype == 0)
						mapa[j][i] = 2;
					else if (itype == 10)
						meta = prop.position;
					else if (itype == 1) {
						posAntX = posX;
						posAntY = posY;
						posX = j;
						posY = i;
					} else if (itype == 12)
						monedas.add(prop.position);
				}
			}
		}

		if (posY + 1 < nf && (mapa[posX][posY + 1] == 5 || mapa[posX][posY + 1] == 2)) {
			suelo = true;
		} else {
			suelo = false;
		}

		if (posX + 1 < nc) {
			if ((mapa[posX + 1][posY] == 5 || mapa[posX + 1][posY] == 2)
					|| (mapa[posX + 1][posY] == 8 || mapa[posX + 1][posY] == 9)
					|| (posX + 2 < nc && (mapa[posX + 2][posY] == 8 || mapa[posX + 2][posY] == 9))) {
				obsDelante = true;
			} else {
				obsDelante = false;
			}
		}

		if (posX - 1 > 0) {
			if ((mapa[posX - 1][posY] == 5 || mapa[posX - 1][posY] == 2)
					|| (mapa[posX - 1][posY] == 8 || mapa[posX - 1][posY] == 9)
					|| (posX - 2 > 0 && (mapa[posX - 2][posY] == 8 || mapa[posX - 2][posY] == 9))) {
				obsDetras = true;
			} else {
				obsDetras = false;
			}
		}

		if (suelo) {
			if (posX + 1 < nc && posY + 2 < nf && mapa[posX + 1][posY + 2] != 5 && mapa[posX + 1][posY + 2] != 2
					&& mapa[posX + 1][posY + 2] != 4) {
				obsDelante = true;
			}
			if (posX - 1 > 0 && posY + 2 < nf && mapa[posX - 1][posY + 1] != 5 && mapa[posX - 1][posY + 1] != 2
					&& mapa[posX + 1][posY + 2] != 4) {
				obsDetras = true;
			}
		}
		// Sysout.out.println("Obs: "+ObsDetras);
		if (meta.x < avatarPos.x)
			metaIzq = true;
		else if (meta.x > avatarPos.x)
			metaDer = true;
      // Sysout.out.println("MetDer: "+metaDer);
		boolean[] est = new boolean[numAtributos];
		est[0] = suelo;
		est[1] = metaDer;
		est[2] = metaIzq;
		est[3] = obsDelante;
		est[4] = obsDetras;
		return new Estado(est);
	}

	public void PintarMundo(StateObservation stateObs) {
		entorno = stateObs.getObservationGrid();
		for (int y = 0; y < nf; y++) {
			for (int x = 0; x < nc; x++) {
				ArrayList<Observation> contenido = entorno[x][y];
				Observation prop;
				if (x == 0) {
					System.out.print(" ");
				}
				if (contenido.size() > 0) {
					prop = contenido.get(0);
					System.out.print(prop.itype);
				} else {
					System.out.print(" ");
				}
			}
			System.out.println(" ");
		}
		System.out.println(" ");
		System.out.println(" ");
	}

}
