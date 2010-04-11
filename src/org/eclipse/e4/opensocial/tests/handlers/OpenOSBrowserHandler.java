package org.eclipse.e4.opensocial.tests.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.opensocial.container.internal.ui.OpenSocialBrowser;
import org.eclipse.e4.opensocial.container.internal.util.OpenSocialUtil;
import org.eclipse.e4.opensocial.model.Module;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class OpenOSBrowserHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public OpenOSBrowserHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);

		// final Module m = OpenSocialUtil
		// .loadModule("http://m2mdemo.anyware-tech.com/resources/gadget/Temperature.xml");
		final Module m = OpenSocialUtil
				.loadModule("file:///e:/Temp/testLog.xml");
		Dialog d = new Dialog(window.getShell()) {
			@Override
			protected Control createDialogArea(Composite parent) {
				Composite c = new OpenSocialBrowser(parent, SWT.NONE, m);
				GridDataFactory.fillDefaults().hint(300, 300).applyTo(c);
				return c;
			}

			@Override
			protected boolean isResizable() {
				return true;
			}
		};
		d.open();

		return null;
	}
}
