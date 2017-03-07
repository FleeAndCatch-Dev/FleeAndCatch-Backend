package flee_and_catch.backend.controller;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;

import flee_and_catch.backend.communication.Client;
import flee_and_catch.backend.communication.Server;
import flee_and_catch.backend.communication.command.CommandType;
import flee_and_catch.backend.communication.command.ControlCommand;
import flee_and_catch.backend.communication.command.ControlCommandType;
import flee_and_catch.backend.communication.command.SzenarioCommand;
import flee_and_catch.backend.communication.command.SzenarioCommandType;
import flee_and_catch.backend.communication.command.device.app.App;
import flee_and_catch.backend.communication.command.device.robot.Robot;
import flee_and_catch.backend.communication.command.device.robot.Steering;
import flee_and_catch.backend.communication.command.identification.IdentificationType;
import flee_and_catch.backend.communication.command.szenario.Szenario;
import flee_and_catch.backend.view.ViewController;

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
		
		ViewController.setNumberOfScenarios(szenarios.size());
		return pSzenario;
	}	
	public static void close(Client pClient, Szenario pSzenario){
		pSzenario.setCommand(SzenarioCommandType.Undefined.toString());
		
		//Set devices active -> false
		for(int i=0;i<pSzenario.getRobots().size();i++){
			RobotController.changeActive(pSzenario.getRobots().get(i), false);
		}
		for(int i=0;i<pSzenario.getApps().size();i++){
			AppController.changeActive(pSzenario.getApps().get(i), false);
		}	
			
		//Set the szenario to the clients of null
		for(int i=0;i<Server.getClients().size();i++){
			for(int j=0;j<pSzenario.getApps().size();j++){
				if(Server.getClients().get(i).getDevice() instanceof App){
					if(((App)Server.getClients().get(i).getDevice()).getIdentification().getId() == pSzenario.getApps().get(j).getIdentification().getId())
						Server.getClients().get(i).setSzenario(null);
				}
			}
		}
		for(int i=0;i<Server.getClients().size();i++){
			for(int j=0;j<pSzenario.getRobots().size();j++){
				if(Server.getClients().get(i).getDevice() instanceof Robot){
					if(((Robot)Server.getClients().get(i).getDevice()).getIdentification().getId() == pSzenario.getRobots().get(j).getIdentification().getId())
						Server.getClients().get(i).setSzenario(null);
				}
			}
		}
		
		//Remove szenario
		Szenario szenario = null;
		for(int i=0;i<SzenarioController.getSzenarios().size();i++){
			if(SzenarioController.getSzenarios().get(i).getId() == pSzenario.getId())
				szenario = SzenarioController.getSzenarios().get(i);
		}
		if(szenario != null){
			SzenarioController.remove(szenario);		
			
			IdentificationType type = IdentificationType.valueOf(pClient.getIdentification().getType());
			if(type != IdentificationType.Undefined){
				//Send control end to robots of the szenario
				for(int i=0;i<Server.getClients().size();i++){
					for(int j=0;j<pSzenario.getRobots().size();j++){
						if(pSzenario.getRobots().get(j).getIdentification().getId() == Server.getClients().get(i).getIdentification().getId()){
							//Send end command to all robots in the szenario
							Gson gson = new Gson();
							ControlCommand cmd = new ControlCommand(ControlCommandType.Control.toString(), ControlCommandType.End.toString(), pClient.getIdentification(), pSzenario.getRobots().get(j), new Steering());							
							Server.sendCmd(Server.getClients().get(i), gson.toJson(cmd));
						}
					}
				}
				
				//Send szenario end to apps of the szenario
				for(int i=0;i<Server.getClients().size();i++){
					for(int j=0;j<pSzenario.getApps().size();j++){
						if(pSzenario.getApps().get(j).getIdentification().getId() == Server.getClients().get(i).getIdentification().getId()){
							//Send end command to all apps in the szenario
							Gson gson = new Gson();
							SzenarioCommand cmd = new SzenarioCommand(CommandType.Szenario.toString(), SzenarioCommandType.End.toString(), pClient.getIdentification(), pSzenario);
							//SzenarioCommand cmd = new ControlCommand(ControlCommandType.Control.toString(), ControlCommandType.End.toString(), pClient.getIdentification(), pSzenario.getRobots().get(j), new Steering());							
							Server.sendCmd(Server.getClients().get(i), gson.toJson(cmd));
						}
					}
				}
				return;
			}
			System.out.println("124 " + "Wrong component");
			return;
		}
		System.out.println("124 " + "This senario doesn't exist");
	}
	
	public static void remove(Szenario pSzenario){
		szenariosLock.lock();
		szenarios.remove(pSzenario);
		szenariosLock.unlock();
		ViewController.setNumberOfScenarios(szenarios.size());
	}
	
	public static Szenario getSzenarioOfDevice(int pId, IdentificationType pType){
		int szenarioId = -1;
		
		for(int i=0; i<szenarios.size();i++){
			if(pType == IdentificationType.App){
				for(int j=0;j<szenarios.get(i).getApps().size();j++){
					if(szenarios.get(i).getApps().get(j).getIdentification().getId() == pId)
						szenarioId = szenarios.get(i).getId();
				}
			}
			else if (pType == IdentificationType.Robot) {
				for(int j=0;j<szenarios.get(i).getRobots().size();j++){
					if(szenarios.get(i).getRobots().get(j).getIdentification().getId() == pId)
						szenarioId = szenarios.get(i).getId();
				}
			}
			else
				return null;
			if(szenarioId != -1)
				return getSzenarioById(szenarioId);
		}		
		return null;
	}
	public static Szenario getSzenarioById(int pId){
		for(int i=0;i<szenarios.size();i++){
			if(szenarios.get(i).getId() == pId){
				return szenarios.get(i);
			}
		}
		return null;
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
