package org.eclipse.e4.opensocial.tests.views;

import org.eclipse.e4.opensocial.container.internal.ui.OpenSocialBrowser;
import org.eclipse.e4.opensocial.container.internal.util.OpenSocialUtil;
import org.eclipse.e4.opensocial.model.Module;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class SampleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.eclipse.e4.opensocial.tests.views.SampleView";

	private OpenSocialBrowser osBrowser;

	/**
	 * The constructor.
	 */
	public SampleView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@SuppressWarnings("restriction")
	public void createPartControl(Composite parent) {

		final Module m = OpenSocialUtil
				.loadModule("file:///e:/Temp/gadget-contacts.xml");

		osBrowser = new OpenSocialBrowser(parent, SWT.NONE, m);
		osBrowser.getBrowser().addTitleListener(new TitleListener() {
			@Override
			public void changed(TitleEvent event) {
				SampleView.this.setPartName(event.title);
			}
		});
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