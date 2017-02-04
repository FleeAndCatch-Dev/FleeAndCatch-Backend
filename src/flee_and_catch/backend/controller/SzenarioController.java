package flee_and_catch.backend.controller;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import flee_and_catch.backend.communication.command.szenario.Szenario;

public final class SzenarioController {
	private static ArrayList<Szenario> szenarios = new ArrayList<Szenario>();
	private static Lock szenariosLock = new ReentrantLock();

	public static void addNew(Szenario pSzenario){
		szenariosLock.lock();
		szenarios.add(pSzenario);
		szenariosLock.unlock();
	}
	
	public static void remove(Szenario pSzenario){
		szenariosLock.lock();
		szenarios.remove(pSzenario);
		szenariosLock.unlock();
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
