/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Michele Loreti
 *  	Laura Nenzi
 *  	Luca Bortolussi
 *******************************************************************************/
/**
 * 
 */
package eu.quanticol.jsstl.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import eu.quanticol.jsstl.space.GraphModel;

/**
 * @author loreti
 *
 */
public class TraGraphModelReader implements GraphModelReader {

	public static final String BEGIN_LOCATIONS_SECTION = "LOCATIONS";
	public static final String BEGIN_EDGES_SECTION = "EDGES";

	@Override
	public GraphModel read(File file) throws IOException, SyntaxErrorExpection {
		return read(new FileReader(file));
	}

	private void populateEdges(ArrayList<Command> commands, int traSection,
			GraphModel model) throws SyntaxErrorExpection {
		for (int i = traSection; i < commands.size(); i++) {
			Command cmd = commands.get(i);
			String[] element = cmd.code.split(" ");
			if (element.length != 3) {
				throw new SyntaxErrorExpection(cmd.line,
						"<int> <int> <double>", cmd.code);
			}
			try {
				int src = Integer.parseInt(element[0]);
				int trg = Integer.parseInt(element[1]);
				double weight = Double.parseDouble(element[2]);
				model.addEdge(src, trg, weight);
			} catch (NumberFormatException e) {
				throw new SyntaxErrorExpection(cmd.line,
						"<int> <int> <double>", cmd.code);
			}
		}
	}

	private int loadLocations(ArrayList<Command> commands, GraphModel model)
			throws SyntaxErrorExpection {
		Command cmd = commands.get(0);
		if (!cmd.isBeginningOfLocationsSection()) {
			throw new SyntaxErrorExpection(cmd.line, BEGIN_LOCATIONS_SECTION,
					cmd.code);
		}
		for (int counter = 1; counter < commands.size(); counter++) {
			cmd = commands.get(counter);
			if (cmd.isBeginningOfEdgesSection()) {
				return counter + 1;
			}
			model.addLoc(cmd.code, counter-1);
		}
		return commands.size();
	}

	private ArrayList<Command> readCommands(Reader reader) throws IOException {
		ArrayList<Command> toReturn = new ArrayList<>();
		BufferedReader br = new BufferedReader(reader);

		String str = br.readLine();
		int counter = 1;
		while (str != null) {
			if (!str.trim().isEmpty()) {
				toReturn.add(new Command(counter, str));
			}
			counter++;
			str = br.readLine();
		}

		br.close();

		return toReturn;
	}

	@Override
	public GraphModel read(String filename) throws IOException,
			SyntaxErrorExpection {
		return read(new File(filename));
	}

	private class Command {

		int line;
		String code;

		public Command(int line, String code) {
			this.line = line;
			this.code = code;
		}

		public boolean isBeginningOfEdgesSection() {
			return code.equals(BEGIN_EDGES_SECTION);
		}

		public boolean isBeginningOfLocationsSection() {
			return code.equals(BEGIN_LOCATIONS_SECTION);
		}

	}

	@Override
	public GraphModel read(Reader reader) throws IOException,
			SyntaxErrorExpection {
		ArrayList<Command> commands = readCommands(reader);
		GraphModel model = new GraphModel();
		int traSection = loadLocations(commands, model);
		populateEdges(commands, traSection, model);
		return model;
	}

}