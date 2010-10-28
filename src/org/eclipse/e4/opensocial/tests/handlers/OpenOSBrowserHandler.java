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
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
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
	 * @author kartben
	 * 
	 */
	private final class OpenSocialDialog extends Dialog {
		private final Module m;

		/**
		 * @param parentShell
		 * @param m
		 */
		private OpenSocialDialog(Shell parentShell, Module m) {
			super(parentShell);
			this.m = m;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			b = new OpenSocialBrowser(parent, SWT.NONE, m);
			b.getBrowser().addTitleListener(new TitleListener() {
				@Override
				public void changed(TitleEvent event) {
					OpenSocialDialog.this.getShell().setText(event.title);
				}
			});
			GridDataFactory.fillDefaults().hint(500, 500).grab(true, true)
					.applyTo(b);
			return b;
		}

		@Override
		protected boolean isResizable() {
			return true;
		}
	}

	private OpenSocialBrowser b;

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
				.loadModule("file:///e:/Temp/gadget-contacts.xml");

		// final Module m = OpenSocialUtil
		// .loadModule("http://opensocial-resources.googlecode.com/svn/tests/trunk/suites/0.7/gadgets/core/prefs.xml");

		// final Module m = OpenSocialUtil
		// .loadModule("http://nakuada/~sebz/dashboard/gadget.xml");

		final Dialog d = new OpenSocialDialog(window.getShell(), m);

		d.setBlockOnOpen(false);
		d.open();

		return null;
	}
}
