package org.eclipse.e4.opensocial.tests.views;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.opensocial.container.internal.ui.OpenSocialBrowser;
import org.eclipse.e4.opensocial.container.internal.util.OpenSocialUtil;
import org.eclipse.e4.opensocial.model.Module;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class OpenSocialView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.eclipse.e4.opensocial.tests.views.OpenSocialView";

	private OpenSocialBrowser osBrowser;

	private String _moduleUrl;

	/**
	 * The constructor.
	 */
	public OpenSocialView() {
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		if (site.getSecondaryId() != null) {
			_moduleUrl = site.getSecondaryId().replace("%3A", ":");
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@SuppressWarnings("restriction")
	public void createPartControl(Composite parent) {

		final String moduleUri = (_moduleUrl != null) ? _moduleUrl
				: "file:///e:/Temp/gadget-contacts.xml";
		final Module m = OpenSocialUtil.loadModule(moduleUri);

		osBrowser = new OpenSocialBrowser(parent, SWT.NONE, m);
		osBrowser.getBrowser().addTitleListener(new TitleListener() {
			@Override
			public void changed(TitleEvent event) {
				OpenSocialView.this.setPartName(event.title);
			}
		});
		Job job = new Job("Retrieve Icon for " + m.getModulePrefs().getTitle()) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO if there is an Icon in ModulePrefs, use it instead...
				URI uri;
				try {
					uri = new URI(moduleUri);
				} catch (URISyntaxException e1) {
					return Status.CANCEL_STATUS;
				}
				String host = uri.getHost();
				try {
					URL favicon = new URL("http://" + host + "/favicon.ico");
					InputStream is = favicon.openConnection().getInputStream();
					Display display = getSite().getWorkbenchWindow().getShell()
							.getDisplay();
					final Image icon = new Image(display, is);
					display.asyncExec(new Runnable() {

						public void run() {
							setTitleImage(icon);
						}
					});
				} catch (MalformedURLException e) {
					return Status.CANCEL_STATUS;
				} catch (IOException e) {
					return Status.CANCEL_STATUS;
				} catch (SWTException e) {
					// the Image might be malformed or not readable by
					// SWT
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
		GridLayoutFactory.fillDefaults().applyTo(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(osBrowser);

		makeActions();
	}

	private void makeActions() {
	}

	private void showMessage(String message) {

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		osBrowser.getBrowser().setFocus();
	}
}