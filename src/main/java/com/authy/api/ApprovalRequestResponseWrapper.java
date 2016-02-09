package com.authy.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="hash")
public class ApprovalRequestResponseWrapper extends HashWrapper {
    private ApprovalRequestResponse approvalRequest;

    @XmlElement(name="approval-request")
    public ApprovalRequestResponse getApprovalRequest() {
        return approvalRequest;
    }

    public void setApprovalRequest(ApprovalRequestResponse approvalRequest) {
        this.approvalRequest = approvalRequest;
    }
}
