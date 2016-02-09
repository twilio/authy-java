package com.authy.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="hash")
public class ApprovalStatusResponseWrapper extends HashWrapper {
    private ApprovalStatusResponse approvalStatus;

    @XmlElement(name="approval-request")
    public ApprovalStatusResponse getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatusResponse approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
