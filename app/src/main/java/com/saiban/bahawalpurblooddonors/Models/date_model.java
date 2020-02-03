package com.saiban.bahawalpurblooddonors.Models;

/**
 * Created by maliksaif232 on 7/25/2017.
 */

public class date_model {
    String LastDonationDate;
    String NextDonationDate;

    public date_model() {
    }

    public String getLastDonationDate() {
        return LastDonationDate;
    }

    public void setLastDonationDate(String lastDonationDate) {
        LastDonationDate = lastDonationDate;
    }

    public String getNextDonationDate() {
        return NextDonationDate;
    }

    public void setNextDonationDate(String nextDonationDate) {
        NextDonationDate = nextDonationDate;
    }
}
