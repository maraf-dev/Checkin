package com.example.checkin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class PersistentDataTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);
    /**
     * Test method for persistent profile settings in UserProfileFragment.
     */
    @Test
    public void testUserInfoPersistence() {
        onView(withId(R.id.attendeebtn)).perform(click());
        onView(withId(R.id.profile)).perform(click());
        //set name
        String testName = "Persistent_Test_1";
        onView(withId(R.id.nameEdit)).perform(ViewActions.replaceText(testName));
        //set email
        String testEmail = "me@ualberta.ca";
        onView(withId(R.id.emailEdit)).perform(ViewActions.replaceText(testEmail));
        //set homepage
        String testHomepage = "https://test.com";
        onView(withId(R.id.homeEdit)).perform(ViewActions.replaceText(testHomepage));
        onView(withId(R.id.saveButton)).perform(click());

        onView(withId(R.id.home2)).perform(click());

        onView(withId(R.id.profile)).perform(click());
        onView(withId(R.id.nameEdit)).check(matches((withText("Persistent_Test_1"))));
        onView(withId(R.id.emailEdit)).check(matches((withText("me@ualberta.ca"))));
        onView(withId(R.id.homeEdit)).check(matches((withText("https://test.com"))));
    }
}
