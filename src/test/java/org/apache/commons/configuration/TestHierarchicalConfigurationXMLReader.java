/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.configuration;

import static org.junit.Assert.assertEquals;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Test class for HierarchicalConfigurationXMLReader.
 *
 * @version $Id: TestHierarchicalConfigurationXMLReader.java 1224644 2011-12-25 20:34:22Z oheger $
 */
public class TestHierarchicalConfigurationXMLReader
{
    private static final String TEST_FILE = ConfigurationAssert.getTestFile(
            "testHierarchicalXMLConfiguration.xml").getAbsolutePath();

    private HierarchicalConfigurationXMLReader parser;

    @Before
    public void setUp() throws Exception
    {
        XMLConfiguration config = new XMLConfiguration();
        config.setFileName(TEST_FILE);
        config.load();
        parser = new HierarchicalConfigurationXMLReader(config);
    }

    @Test
    public void testParse() throws Exception
    {
        SAXSource source = new SAXSource(parser, new InputSource());
        DOMResult result = new DOMResult();
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        trans.transform(source, result);
        Node root = ((Document) result.getNode()).getDocumentElement();
        JXPathContext ctx = JXPathContext.newContext(root);

        assertEquals("Wrong name of root element", "database", root.getNodeName());
        assertEquals("Wrong number of children of root", 1, ctx.selectNodes(
                "/*").size());
        assertEquals("Wrong number of tables", 2, ctx.selectNodes(
                "/tables/table").size());
        assertEquals("Wrong name of first table", "users", ctx
                .getValue("/tables/table[1]/name"));
        assertEquals("Wrong number of fields in first table", 5, ctx
                .selectNodes("/tables/table[1]/fields/field").size());
        assertEquals("Wrong attribute value", "system", ctx
                .getValue("/tables/table[1]/@tableType"));
    }
}
