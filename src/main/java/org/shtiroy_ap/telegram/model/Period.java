package org.shtiroy_ap.telegram.model;

import java.util.List;

public class Period {
    private EnquiryPeriod enquiryPeriod;
    private TenderPeriod tenderPeriod;
    private List<Enquiry> enquiries;

    public Period() {
    }

    public Period(EnquiryPeriod enquiryPeriod, TenderPeriod tenderPeriod, List<Enquiry> enquiries) {
        this.enquiryPeriod = enquiryPeriod;
        this.tenderPeriod = tenderPeriod;
        this.enquiries = enquiries;
    }

    public EnquiryPeriod getEnquiryPeriod() {
        return enquiryPeriod;
    }

    public void setEnquiryPeriod(EnquiryPeriod enquiryPeriod) {
        this.enquiryPeriod = enquiryPeriod;
    }

    public TenderPeriod getTenderPeriod() {
        return tenderPeriod;
    }

    public void setTenderPeriod(TenderPeriod tenderPeriod) {
        this.tenderPeriod = tenderPeriod;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public void setEnquiries(List<Enquiry> enquiries) {
        this.enquiries = enquiries;
    }
}
