package AA2024.uadadsidelgaumalu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import AA2024.uadadsidelgaumalu.Motor.Estado;
import AA2024.uadadsidelgaumalu.Motor.Motor;
import AA2024.uadadsidelgaumalu.Motor.Mundo;
import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class Practica1_RL_Agente_01_Entorno extends AbstractPlayer {
	private Mundo m;
	private Motor cerebro;
	private boolean entrenar = false;
	
	public Practica1_RL_Agente_01_Entorno(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) throws IOException {
	     m=new Mundo(stateObs);
	     if(entrenar)
	        cerebro = Motor.devolverInstancia(m, new File("./src/AA2024//uadadsidelgaumalu/Tabla_Estados.txt"), entrenar);
	     else 
	    	 cerebro = Motor.devolverInstancia(m, new File("./src/AA2024//uadadsidelgaumalu/Tabla_Estados_Jugar.txt"), entrenar);
	}

	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		Estado e=m.analizaMundo(stateObs);	
		if(!entrenar) return cerebro.Jugar(m, e, entrenar);
		else return cerebro.Entrenar(m, e); 
	}

}
