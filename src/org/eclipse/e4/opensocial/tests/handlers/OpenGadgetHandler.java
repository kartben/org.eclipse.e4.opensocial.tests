/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.opensocial.tests.handlers;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;

public class OpenGadgetHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow wwin = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		String selectedText = "";
		if (selection != null) {
			Method getText;
			if (selection instanceof IStructuredSelection) {
				Object o = ((IStructuredSelection) selection).getFirstElement();
				if (o instanceof IFile) {
					selectedText = ((IFile) o).getLocationURI().toString();
				} else if (o instanceof IAdaptable) {
					IFile ifile = (IFile) ((IAdaptable) o)
							.getAdapter(IFile.class);
					selectedText = ifile.getLocationURI().toString();
				}
			}
			try {
				getText = selection.getClass().getMethod("getText");
				selectedText = (String) getText.invoke(selection);
			} catch (Exception e) {
			}
		}
		InputDialog inputDialog = new InputDialog(wwin.getShell(),
				"Open Gadget", "Please enter the gadget URL.", selectedText,
				new IInputValidator() {
					public String isValid(String newText) {
						try {
							new URL(newText);
						} catch (MalformedURLException ex) {
							return "Invalid URL";
						}
						return null;
					}
				});
		String url = null;
		if (inputDialog.open() == Dialog.OK) {
			try {
				url = inputDialog.getValue();
				url = url.replace(":", "%3A");
				wwin.getActivePage().showView(
						"org.eclipse.e4.opensocial.tests.views.OpenSocialView",
						url, IWorkbenchPage.VIEW_ACTIVATE);
			} catch (PartInitException e) {
				StatusManager.getManager().handle(
						new Status(IStatus.ERROR, "e4.opensocial",
								"Could not open gadget", e));
			}
		}
		return url;
	}
}
