/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.iti10

import org.apache.camel.Exchange;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AuditUtils
import org.openehealth.ipf.platform.camel.ihe.mllp.core.QueryAuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

/**
 * Groovy audit strategy util for ITI-10 (PIX Update Notification).
 * @author Dmytro Rud
 */
class Iti10AuditStrategyUtils  {


    static void enrichAuditDatasetFromRequest(QueryAuditDataset auditDataset, MessageAdapter msg, Exchange exchange) {
        auditDataset.patientIds = AuditUtils.pidList(msg.PID[3])
    }

        
}
