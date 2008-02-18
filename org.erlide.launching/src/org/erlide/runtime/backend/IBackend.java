/*******************************************************************************
 * Copyright (c) 2006 Vlad Dumitrescu and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Vlad Dumitrescu
 *******************************************************************************/
package org.erlide.runtime.backend;

import java.io.IOException;
import java.util.List;

import org.eclipse.debug.core.IStreamListener;
import org.erlide.jinterface.rpc.ConversionError;
import org.erlide.runtime.backend.console.BackendShellManager;
import org.erlide.runtime.backend.exceptions.BackendException;
import org.erlide.runtime.backend.exceptions.ErlangRpcException;

import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;

public interface IBackend {

	public static final String ERL_BACKEND = "erlide_backend";

	/**
	 * Method dispose
	 */
	void dispose();

	/**
	 * @throws ErlangRpcException
	 * 
	 * @param m
	 * @param f
	 * @param signature
	 *            TODO
	 * @param a
	 * @return OtpErlangObject
	 * @throws ConversionError
	 */
	RpcResult rpc(String m, String f, String signature, Object... a)
			throws ErlangRpcException, ConversionError;

	/**
	 * @throws ErlangRpcException
	 * 
	 * @param m
	 * @param f
	 * @param timeout
	 * @param signature
	 *            TODO
	 * @param a
	 * @return OtpErlangObject
	 * @throws ConversionError
	 */
	RpcResult rpct(String m, String f, int timeout, String signature,
			Object... a) throws ErlangRpcException, ConversionError;

	/**
	 * @throws ErlangRpcException,
	 *             BackendException
	 * 
	 * @param m
	 * @param f
	 * @param signature
	 *            TODO
	 * @param a
	 * @return OtpErlangObject
	 * @throws ConversionError
	 */
	OtpErlangObject rpcx(String m, String f, String signature, Object... a)
			throws ErlangRpcException, BackendException, ConversionError;

	/**
	 * @throws ErlangRpcException,
	 *             BackendException
	 * 
	 * @param m
	 * @param f
	 * @param timeout
	 * @param signature
	 *            TODO
	 * @param a
	 * @return OtpErlangObject
	 * @throws ConversionError
	 */
	OtpErlangObject rpcxt(String m, String f, int timeout, String signature,
			Object... a) throws ErlangRpcException, BackendException,
			ConversionError;

	/**
	 * 
	 * @param pid
	 * @param msg
	 */
	void send(OtpErlangPid pid, Object msg);

	void send(String name, Object msg);

	/**
	 * Method addEventListener
	 * 
	 * @param event
	 *            String
	 * @param l
	 *            IBackendEventListener
	 */
	void addEventListener(String event, IBackendEventListener l);

	/**
	 * Method removeEventListener
	 * 
	 * @param event
	 *            String
	 * @param l
	 *            IBackendEventListener
	 */
	void removeEventListener(String event, IBackendEventListener l);

	ICodeManager getCodeManager();

	OtpErlangPid getEventPid();

	OtpErlangPid getRpcPid();

	String getCurrentVersion();

	OtpErlangObject receive(int i) throws OtpErlangExit,
			OtpErlangDecodeException;

	OtpErlangObject receiveEvent() throws OtpErlangExit,
			OtpErlangDecodeException;

	OtpErlangObject receiveRpc(long timeout) throws OtpErlangExit,
			OtpErlangDecodeException;

	OtpErlangObject execute(String fun, OtpErlangObject... args)
			throws Exception;

	BackendShellManager getShellManager();

	void sendToDefaultShell(String msg) throws IOException;

	void sendToShell(String str);

	void addStdListener(IStreamListener dsp);

	String getLabel();

	boolean ping();

	List<IBackendEventListener> getEventListeners(String event);
}
