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
package eu.quanticol.jsstl.space;

import java.util.Set;

public interface SpaceModel<L, E> {
	// TODO: Add the methods.

	/**
	 * This method returns the set of points in the model. The returned set can
	 * be modified.
	 * 
	 * @return the set of points in the model.
	 */
	public Set<L> getPoints();

	public Set<E> getEdges();

	public Set<L> getExternalBorder(Set<L> set);

	// /**
	// * Computes the closure of <code>set</code>.
	// * @param set a set
	// * @return the closure of <code>set</code>
	// */
	// public Set<S> closure( Set<S> set );
	//
	// /**
	// * Computes the external border of <code>set</code>.
	// *
	// * @param sets a set of points
	// *
	// * @return the closure of <code>set</code>.
	// */
	//
	// public Set<S> pre( S s );
	//
	// public Set<S> post( S s );
	//
	// public Set<S> pre( Set<S> set );
	//
	// public Set<S> post( Set<S> set );
	//
	// public Set<S> getSet( String name );
}