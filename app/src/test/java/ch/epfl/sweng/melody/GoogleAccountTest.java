package ch.epfl.sweng.melody;

import org.junit.Test;

import ch.epfl.sweng.melody.account.GoogleAccount;

import static org.junit.Assert.assertTrue;

public class GoogleAccountTest {

    @Test
    public void classCanInstantiate() {
        GoogleAccount googleAccount = new GoogleAccount();
        assertTrue(googleAccount != null);
    }
}
