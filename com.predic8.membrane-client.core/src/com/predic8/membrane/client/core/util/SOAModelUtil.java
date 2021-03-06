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

package com.predic8.membrane.client.core.util;

import groovy.xml.MarkupBuilder;

import java.io.File;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.predic8.schema.creator.AbstractSchemaCreator;
import com.predic8.schema.restriction.BaseRestriction;
import com.predic8.schema.restriction.facet.Facet;
import com.predic8.schema.restriction.facet.MaxLengthFacet;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wsdl.WSDLParserContext;
import com.predic8.wstool.creator.RequestCreator;
import com.predic8.wstool.creator.SOARequestCreator;

public class SOAModelUtil {
	
	public static String getSOARequestBody(BindingOperation bOperation, Map<String, String> result) {
		return createRequestBody(bOperation, new RequestCreator(), result);
	}
	
	public static String getPortTypeName(BindingOperation bOperation) {
		return bOperation.getBinding().getPortType().getName();
	}

	public static Definitions getDefinitions(String url) {
		WSDLParserContext ctx = new WSDLParserContext();
		ctx.setInput(url);
		return new WSDLParser().parse(ctx);
	}
	
	public static Definitions getDefinitions(File file) {
		WSDLParserContext ctx = new WSDLParserContext();
		ctx.setInput(file);
		return new WSDLParser().parse(ctx);
	}

	public static String getHost(String url) {
		if ("".equals(url))
			return url;
		
		if (url.startsWith("http://"))
			url = url.substring(7);
		else if (url.startsWith("https://"))
			url = url.substring(8);
		
		return url.split("/")[0];
	}
	
	public static String getPathAndQueryString(String dest) throws MalformedURLException {
		URL url = new URL(dest);
		
		String uri = url.getPath();
		if (url.getQuery() != null) {
			return uri + "?" + url.getQuery();
		}
		return uri;
	}
		
	private static String createRequestBody(BindingOperation bOperation, @SuppressWarnings("rawtypes") AbstractSchemaCreator schemaCreator, Map<String, String> formParams) {
		StringWriter writer = new StringWriter();
		
		SOARequestCreator creator = new SOARequestCreator(bOperation.getDefinitions(), schemaCreator, new MarkupBuilder(writer));

		
		if (formParams != null)
			creator.setFormParams(formParams);
		
		try {
			creator.createRequest(getPortTypeName(bOperation), bOperation.getName(), bOperation.getBinding().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return writer.toString();
	}
	
	public static MaxLengthFacet getMaxLengthFacet(BaseRestriction rest) {
		for (Facet facet : rest.getFacets()) {
			if (facet instanceof MaxLengthFacet) 
				return (MaxLengthFacet)facet;
		}
		return null;
	}
}
