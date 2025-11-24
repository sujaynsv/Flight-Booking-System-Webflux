package com.flightapp.dto;

public class PnrResponse {

    private String pnr;

    public PnrResponse() {
    }

    public PnrResponse(String pnr) {
        this.pnr = pnr;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }
}
