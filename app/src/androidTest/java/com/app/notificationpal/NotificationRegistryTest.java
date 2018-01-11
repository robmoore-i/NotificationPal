package com.app.notificationpal;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

public class NotificationRegistryTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, true);

    private Context appContext = InstrumentationRegistry.getTargetContext();
    private NotificationManager notificationManager = (NotificationManager) appContext.getSystemService(NOTIFICATION_SERVICE);

    @Before
    public void beforeEach() {
        notificationManager.cancelAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Test
    public void canRegisterNotification() {
        onView(withId(R.id.notificationName)).perform(replaceText("Train Time")).perform(closeSoftKeyboard());
        onView(withId(R.id.submitNotification)).perform(click());

        assertEquals("Train Time", notificationManager.getActiveNotifications()[0].getNotification().extras.getString(Notification.EXTRA_TITLE));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Test
    public void registersNotificationOnlyOnce() {
        onView(withId(R.id.notificationName)).perform(replaceText("Train Time")).perform(closeSoftKeyboard());
        onView(withId(R.id.submitNotification)).perform(click());
        onView(withId(R.id.submitNotification)).perform(click());

        assertEquals("Train Time", notificationManager.getActiveNotifications()[0].getNotification().extras.getString(Notification.EXTRA_TITLE));
        assertEquals(1, notificationManager.getActiveNotifications().length);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Test
    public void canChangeConstraintType() {
        String spinnerOptionText = appContext.getResources().getString(R.string.location);

        onView(withId(R.id.notificationName)).perform(replaceText("Train Time")).perform(closeSoftKeyboard());
        onView(withId(R.id.constraintTypeSpinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.constraintTypeSpinner)).check(matches(withSpinnerText(containsString(spinnerOptionText))));
        onView(withId(R.id.submitNotification)).perform(click());

        assertEquals("Train Time", notificationManager.getActiveNotifications()[0].getNotification().extras.getString(Notification.EXTRA_TITLE));
        assertEquals(1, notificationManager.getActiveNotifications().length);
    }
}
