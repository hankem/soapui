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

package com.eviware.soapui.impl.wsdl.actions.iface;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.support.AbstractSoapUIAction;

/**
 * Sorts the operations of a {@link WsdlInterface}.
 */
public class SortOperationsAction extends AbstractSoapUIAction<WsdlInterface> {
    public static final String SOAPUI_ACTION_ID = "SortOperationsAction";

    public SortOperationsAction() {
        super("Sort Operations", "Sorts the operations defined within this interface");
    }

    public void perform(WsdlInterface iface, Object param) {
        iface.sortOperationsByName();
        UISupport.showInfoMessage("Sorted " + iface.getOperationCount() + " operations by name.", "Sort Operations");
    }
}
