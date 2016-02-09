package com.authy.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * <?xml version="1.0" encoding="UTF-8"?>
 * <hash>
 *   <approval-request>
 *     <uuid>4a10dbb0-6ac2-0133-7bd9-0e67b818e6fb</uuid>
 *   </approval-request>
 *   <success type="boolean">true</success>
 * </hash>
 */

@XmlRootElement(name="approval-request")
public class ApprovalRequestResponse {

    private String uuid;
    private boolean success;

    @XmlElement(name="uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
