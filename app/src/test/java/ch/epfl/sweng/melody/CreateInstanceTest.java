package ch.epfl.sweng.melody;


import org.junit.Test;

import ch.epfl.sweng.melody.notification.NotificationHandler;
import ch.epfl.sweng.melody.util.FetchingUtils;
import ch.epfl.sweng.melody.util.NavigationHandler;
import ch.epfl.sweng.melody.util.PermissionUtils;
import ch.epfl.sweng.melody.util.UserPreferences;

public class CreateInstanceTest {

    @Test
    public void canCreateInstance() {
        UserPreferences userPreferences = new UserPreferences();
        PermissionUtils permissionUtils = new PermissionUtils();
        NavigationHandler navigationHandler = new NavigationHandler();
        FetchingUtils fetchingUtils = new FetchingUtils();
        NotificationHandler notificationHandler = new NotificationHandler();
    }
}
