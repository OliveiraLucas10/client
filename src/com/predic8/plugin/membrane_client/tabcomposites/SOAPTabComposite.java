/* Copyright 2009 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.plugin.membrane_client.tabcomposites;

import java.io.ByteArrayInputStream;

import org.eclipse.swt.widgets.TabFolder;

import com.predic8.plugin.membrane_client.listeners.HighligtingLineStyleListner;
import com.predic8.plugin.membrane_client.ui.TextUtil;


public class SOAPTabComposite extends BodyTextTabComposite {

	public static final String TAB_TITLE = "SOAP";
	
	public SOAPTabComposite(TabFolder parent) {
		super(parent, TAB_TITLE);
		bodyText.addLineStyleListener(new HighligtingLineStyleListner());
	}

	@Override
	public String getBodyText() {
		return bodyText.getText();
	}

	public void setBodyTextEditable(boolean bool) {
		bodyText.setEditable(bool);
	}

	public void setTabTitle(String tabName) {
		tabItem.setText(tabName);
	}

	public void setBodyText(String string) {
		bodyText.setText(string);
	}
	
	public void beautify(byte[] content) {
		bodyText.setText(TextUtil.formatXML(new ByteArrayInputStream(content)));
		bodyText.redraw();
	}
	
	protected boolean isBeautifyBody() {
		return true;
	}
	
	@Override
	public boolean isFormatSupported() {
		return true;
	}
}
