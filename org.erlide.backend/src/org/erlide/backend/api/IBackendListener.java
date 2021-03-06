/*******************************************************************************
 * Copyright (c) 2004 Vlad Dumitrescu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Vlad Dumitrescu
 *******************************************************************************/
package org.erlide.backend.api;

import org.eclipse.core.resources.IProject;

public interface IBackendListener {

    void runtimeAdded(IBackend backend);

    void runtimeRemoved(IBackend backend);

    void moduleLoaded(IBackend aBackend, IProject project, String moduleName);

}
