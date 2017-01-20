package flee_and_catch.backend.controller;

import java.util.ArrayList;

import flee_and_catch.backend.communication.command.szenario.Szenario;

public final class SzenarioController {
	private static ArrayList<Szenario> szenarios = new ArrayList<Szenario>();

	public static ArrayList<Szenario> getSzenarios() {
		return szenarios;
	}
}
