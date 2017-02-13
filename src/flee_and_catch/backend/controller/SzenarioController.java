package flee_and_catch.backend.controller;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import flee_and_catch.backend.communication.command.szenario.Szenario;

public final class SzenarioController {
	private static ArrayList<Szenario> szenarios = new ArrayList<Szenario>();
	private static Lock szenariosLock = new ReentrantLock();

	public static Szenario addNew(Szenario pSzenario){
		szenariosLock.lock();
		//Generate id		
		int id = 0;
		ArrayList<Szenario> tmpszenario = szenarios;
		ArrayList<Szenario> sortszenarios = new ArrayList<Szenario>();
		
		for(int i=0; i<tmpszenario.size(); i++){
			for(int j=0; j<tmpszenario.size() - 1; j++){
				if(tmpszenario.get(j).getId() < tmpszenario.get(j + 1).getId())
					continue;
				sortszenarios.add(tmpszenario.get(j));
				tmpszenario.remove(j);
			}
		}
		
		if(sortszenarios.size() > 1)
			szenarios = sortszenarios;
		
		for(int i=0;i<szenarios.size();i++){
			if(szenarios.get(i).getId() == id)
				id++;
		}
		
		pSzenario.setId(id);
		
		szenarios.add(pSzenario);
		szenariosLock.unlock();
		
		return pSzenario;
	}	
	public static void remove(Szenario pSzenario){
		szenariosLock.lock();
		szenarios.remove(pSzenario);
		szenariosLock.unlock();
	}
	
	public static int generateId(){
		//generate new id for a szenario
		return 0;
	}
	
	public static ArrayList<Szenario> getSzenarios() {
		szenariosLock.lock();
		ArrayList<Szenario> szenarioList = new ArrayList<Szenario>(szenarios);
		szenariosLock.unlock();
		return szenarioList;
	}

	public static void setSzenarios(ArrayList<Szenario> szenarios) {
		szenariosLock.lock();
		SzenarioController.szenarios = szenarios;
		szenariosLock.unlock();
	}
	
}
