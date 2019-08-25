/*
 * SoapUI, Copyright (C) 2004-2019 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon as they will be approved by the European Commission - subsequent 
 * versions of the EUPL (the "Licence"); 
 * You may not use this work except in compliance with the Licence. 
 * You may obtain a copy of the Licence at: 
 * 
 * http://ec.europa.eu/idabc/eupl 
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is 
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the Licence for the specific language governing permissions and limitations 
 * under the Licence. 
 */

package com.eviware.soapui.impl.wsdl;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.OperationConfig;
import com.eviware.soapui.config.WsdlInterfaceConfig;
import com.eviware.soapui.model.iface.InterfaceListener;
import org.junit.Before;
import org.junit.Test;

import static java.lang.String.join;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WsdlInterfaceTest {

    private WsdlProject project;
    private WsdlInterfaceConfig interfaceConfig;
    private WsdlInterface iface;

    @Before
    public void setUp() throws Exception {
        project = new WsdlProject();
        interfaceConfig = WsdlInterfaceConfig.Factory.newInstance();
        iface = new WsdlInterface(project, interfaceConfig);

        assertEquals(0, iface.getEndpoints().length);
    }

    @Test
    public void testAddEndpoints() throws Exception {
        iface.addEndpoint("testEndpoint");
        assertEquals(1, iface.getEndpoints().length);
        assertEquals("testEndpoint", iface.getEndpoints()[0]);

        iface.addEndpoint("testEndpoint");
        assertEquals(1, iface.getEndpoints().length);
        assertEquals("testEndpoint", iface.getEndpoints()[0]);

        iface.addEndpoint("testEndpoint2");
        assertEquals(2, iface.getEndpoints().length);
        assertEquals("testEndpoint", iface.getEndpoints()[0]);
        assertEquals("testEndpoint2", iface.getEndpoints()[1]);
    }

    @Test
    public void testRemoveEndpoints() throws Exception {
        iface.addEndpoint("testEndpoint");
        iface.addEndpoint("testEndpoint2");

        iface.removeEndpoint("testEndpoint");
        assertEquals(1, iface.getEndpoints().length);

        iface.removeEndpoint("testEndpoint2");
        assertEquals(0, iface.getEndpoints().length);
    }

    @Test
    public void testSortOperationsByName() {
        interfaceConfig = WsdlInterfaceConfig.Factory.newInstance();
        OperationConfig operation2 = interfaceConfig.addNewOperation();
        operation2.setName("op 2 (initially first)");
        OperationConfig operation1 = interfaceConfig.addNewOperation();
        operation1.setName("op 1 (initially second)");
        operation1.addNewCall().setName("call 1");
        operation1.addNewCall().setName("call 2");
        InterfaceListener interfaceListener = mock(InterfaceListener.class);
        SoapUI.getListenerRegistry().addSingletonListener(InterfaceListener.class, interfaceListener);
        iface = new WsdlInterface(project, interfaceConfig);

        iface.sortOperationsByName();

        assertEquals(join("\n",
                "<xml-fragment wsaVersion=\"NONE\" xmlns:con=\"http://eviware.com/soapui/config\">",
                "  <con:settings/>",
                "  <con:definitionCache/>",
                "  <con:endpoints/>",
                "  <con:operation name=\"op 1 (initially second)\" isOneWay=\"false\">",
                "    <con:settings/>",
                "    <con:call name=\"call 1\">",
                "      <con:settings/>",
                "      <con:encoding>UTF-8</con:encoding>",
                "    </con:call>",
                "    <con:call name=\"call 2\">",
                "      <con:settings/>",
                "      <con:encoding>UTF-8</con:encoding>",
                "    </con:call>",
                "  </con:operation>",
                "  <con:operation name=\"op 2 (initially first)\" isOneWay=\"false\">",
                "    <con:settings/>",
                "  </con:operation>",
                "</xml-fragment>"
        ), iface.getConfig().toString().replaceAll(" id=\"[0-9a-f-]*\"", ""));
        for (int i = 0; i < 2; i++) {
            WsdlOperation operation = (WsdlOperation) iface.getOperationList().get(i);
            assertEquals(operation.getConfig().toString(), iface.getConfig().getOperationArray(i).toString());
            verify(interfaceListener).operationAdded(operation);
        }
        verify(interfaceListener, times(2)).operationRemoved(any());
    }
}
