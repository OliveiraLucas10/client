/* Copyright 2010 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.plugin.membrane_client.creator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.predic8.membrane.client.core.SOAPConstants;
import com.predic8.membrane.client.core.configuration.URL;
import com.predic8.membrane.client.core.configuration.WSDL;
import com.predic8.membrane.client.core.util.SOAModelUtil;
import com.predic8.membrane_client.tests.SWTTestCase;
import com.predic8.membrane_client.tests.util.GUITestUtil;
import com.predic8.schema.Schema;
import com.predic8.schema.SchemaParser;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Port;
import com.predic8.wsdl.Service;
import com.predic8.xml.util.ClasspathResolver;
import com.predic8.xml.util.ResourceResolver;

public class CompositeCreatorTest extends SWTTestCase {

	private CompositeCreator creator;
	
	private Schema schema;
	
	protected void setUp() throws Exception {
		super.setUp();
		Composite base = new Composite(shell, SWT.NONE);
		base.setLayout(new FillLayout());
		
		creator = new CompositeCreator(base);
		
		ResourceResolver resolver =  new ClasspathResolver();
		
		SchemaParser parser = new SchemaParser();
		parser.setResourceResolver(resolver);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("input", "/person-a.xsd");
		
	    schema = (Schema)parser.parse(params);
		
	}
	
	public void testEmployeeSchema() throws Exception {
		creator.createElement(schema.getElement("employee"), new CompositeCreatorContext());
		
		Control[] children = creator.getRoot().getChildren();
		
		assertTrue(children.length > 0);
		
		List<Text> texts = GUITestUtil.getTextControls(children);
		assertTrue(texts.size() == 3); 
		
		List<String> pathes = new ArrayList<String>(); 
		for (Text text : texts) {
			Object data = text.getData(SOAPConstants.PATH);
			assertTrue(data != null);
			pathes.add(data.toString());
		}
		
		assertTrue(pathes.contains("xpath:/employee/firstName"));
		assertTrue(pathes.contains("xpath:/employee/lastName"));
		assertTrue(pathes.contains("xpath:/employee/department"));
		
		
		
	}
	
	public void testEmployeeSchemaWithFormParams() throws Exception {
		CompositeCreatorContext ctx = new CompositeCreatorContext();
		ctx.setFormParams(getEmployeeParams());		
		creator.createElement(schema.getElement("employee"), ctx);
		
		Control[] children = creator.getRoot().getChildren();
		
		assertTrue(children.length > 0);
		
		List<Text> texts = GUITestUtil.getTextControls(children);
		assertTrue(texts.size() == 3); 
		
		Map<String, String> pathes = new HashMap<String, String>(); 
		for (Text text : texts) {
			Object data = text.getData(SOAPConstants.PATH);
			assertTrue(data != null);
			pathes.put(data.toString(), text.getText());
		}
		
		assertTrue(pathes.containsKey("xpath:/employee/firstName"));
		assertEquals("Muster", pathes.get("xpath:/employee/firstName"));
		
		assertTrue(pathes.containsKey("xpath:/employee/lastName"));
		assertEquals("Mustermann", pathes.get("xpath:/employee/lastName"));
		
		assertTrue(pathes.containsKey("xpath:/employee/department"));
		assertEquals("IT", pathes.get("xpath:/employee/department"));
	}
	
	private Map<String, String> getEmployeeParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("xpath:/employee/firstName", "Muster");
		params.put("xpath:/employee/lastName", "Mustermann");
		params.put("xpath:/employee/department", "IT");
		return params;
	}
	
	public void testBLZService() throws Exception {
		
		initCreator("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");

		Control[] children = creator.getRoot().getChildren();
		
		assertTrue(children.length > 0);
		
		List<Text> texts = GUITestUtil.getTextControls(children);
		assertTrue(texts.size() == 1); 
		
		List<String> pathes = new ArrayList<String>(); 
		for (Text text : texts) {
			Object data = text.getData(SOAPConstants.PATH);
			assertTrue(data != null);
			pathes.add(data.toString());
		}
		assertTrue(pathes.contains("xpath:/getBank/blz"));
	}
	
	
	public void testFlightHistoryService() throws Exception {
		
		initCreator("http://www.pathfinder-xml.com/development/WSDL/FlightHistoryService.wsdl");

		Control[] children = creator.getRoot().getChildren();
		
		assertTrue(children.length > 0);
		
		List<Text> texts = GUITestUtil.getTextControls(children);
		assertTrue(texts.size() == 16); 
		
		List<String> pathes = new ArrayList<String>(); 
		for (Text text : texts) {
			Object data = text.getData(SOAPConstants.PATH);
			System.out.println(data);
			assertTrue(data != null);
			pathes.add(data.toString());
		}
		
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/Login/@AccountId"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/Login/@Guid"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/Login/@Password"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/Login/@UserId"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/@Id"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/SpecificationFlight/@FlightNumber"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/SpecificationFlight/@TailNumber"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/SpecificationFlight/Airline/@AirlineCode"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/SpecificationFlight/Airline/@IATACode"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/SpecificationFlight/Airline/@ICAOCode"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/SpecificationFlight/Airline/@Name"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/SpecificationDateRange/@ArrivalDateTimeMax"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/SpecificationDateRange/@ArrivalDateTimeMin"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/SpecificationDateRange/@DepartureDateTimeMax"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/SpecificationDateRange/@DepartureDateTimeMin"));
		assertTrue(pathes.contains("xpath:/FlightHistoryGetHistoryRequest/FlightHistoryGetHistoryInfo/FlightHistoryGetHistoryRequestedData/@MaxPositionalUpdates"));
	}
	
	
	private WSDL getWSDL(String value) {
		URL url = new URL();
		url.setValue(value);

		WSDL wsdl = new WSDL();
		wsdl.setUrl(url);
		return wsdl;
	}
	
	private void initCreator(String value) {
		WSDL wsdl = getWSDL(value);
		
		Definitions definitions = SOAModelUtil.getDefinitions(wsdl.getUrl().getValue());
		creator.setDefinitions(definitions);
		
		Service service = definitions.getServices().get(0);
		Port port = service.getPorts().get(0);
		Object operation = port.getBinding().getOperations().get(0);
		
		BindingOperation bOp = (BindingOperation)operation;
		
		creator.buildComposite(SOAModelUtil.getPortTypeName(bOp), bOp.getName(), bOp.getBinding().getName());
	}
	
}
