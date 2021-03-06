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

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple MultiConfigurationTest.
 *
 * @version $Id: TestPatternSubtreeConfiguration.java 1224818 2011-12-26 21:20:17Z oheger $
 */
public class TestPatternSubtreeConfiguration
{
    private static String CONFIG_FILE = "target/test-classes/testPatternSubtreeConfig.xml";
    private static String PATTERN = "BusinessClient[@name='${sys:Id}']";
    private XMLConfiguration conf;

    @Before
    public void setUp() throws Exception
    {
        conf = new XMLConfiguration();
        conf.setFile(new File(CONFIG_FILE));
        conf.load();
    }

    /**
     * Rigourous Test :-)
     */
    @Test
    public void testMultiConfiguration()
    {
        //set up a reloading strategy
        FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
        strategy.setRefreshDelay(10000);

        PatternSubtreeConfigurationWrapper config = new PatternSubtreeConfigurationWrapper(this.conf, PATTERN);
        config.setReloadingStrategy(strategy);
        config.setExpressionEngine(new XPathExpressionEngine());

        System.setProperty("Id", "1001");
        assertTrue(config.getInt("rowsPerPage") == 15);

        System.setProperty("Id", "1002");
        assertTrue(config.getInt("rowsPerPage") == 25);

        System.setProperty("Id", "1003");
        assertTrue(config.getInt("rowsPerPage") == 35);
    }
}