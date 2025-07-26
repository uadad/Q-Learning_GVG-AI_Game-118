package AA2024.uadadsidelgaumalu;

import java.io.IOException;
import java.util.Random;

import AA2024.uadadsidelgaumalu.Motor.Motor;
import tools.Utils;
import tracks.ArcadeMachine;

public class Practica_XX_exe {

	public static void main(String[] args) throws IOException {

		String p0 = "AA2024.uadadsidelgaumalu.Practica1_RL_Agente_01_Entorno";

		// Load available games
		String spGamesCollection = "examples/all_games_sp.csv";
		String[][] games = Utils.readGames(spGamesCollection);

		// Game settings
		boolean visuals = true;
		int gameIdx = 118;

		String gameName = games[gameIdx][1];
		String game = games[gameIdx][0];
		String level1;
		
		double[]  res;
		int haGanado = 0;
	   visuals = true;
	   Motor cerebro = Motor.devolverInstancia(null, null, false);
	   
	   int j=0,k=0;
	   for (int i = 0; i < 5; i++) {
	        int seed1 = new Random().nextInt();
	       level1 = game.replace(gameName, gameName + "_lvl" + k);
	       res = ArcadeMachine.runOneGame(game, level1, visuals, p0, null, seed1, 0);
	        if(res[0] != 0.0) {
	        	haGanado++;
	        }
	        j++;
	        if(j==20) {
	        	cerebro.añadirGanados(haGanado);
	        	cerebro.añadirExploracion();
	        	haGanado=0;
	        	j=0;
	        }
	     cerebro.decExp();
	     k++;
	     if(k>4) k=0;
	    }
	   cerebro.guardarGrafica("winRatesVsExplorationFija2.csv");
	   cerebro.guardarHistograma("Estados.csv");
       cerebro.guardarEstados();
       
       
      System.exit(0);
	}
}