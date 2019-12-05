package com.example.final_project;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.longClick;  // ADDED THIS
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.object.HasToString.hasToString;

public class ServicesTest {

    private Administrator admin;

    @Rule
    public ActivityTestRule<MainActivity> activityRuleMain
            = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<Profile> activityRuleProfile;


    @Test
    public void changeText_sameActivity() {

        onView(withId(R.id.SignInButton)).perform(click());
        onView(ViewMatchers.withId(R.id.passwordField2))
                .perform(typeText("EspressoUser123"), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.usernameField2))
                .perform(typeText("EspressoUser123"), closeSoftKeyboard());
        onView(withId(R.id.signInButton)).perform(click());
        while (true) {
            try {
                onView(withId(R.id.servicesButton)).perform(click());
                break;
            } catch (Exception e) {
                onView(withId(R.id.signInButton)).perform(click());
                continue;
            }
        }

        //goes into service picks the first service then deletes
        //goes into services again, picks the second service then deletes

        onView(withId(R.id.servicesBtn)).perform(click());
        try {
            onData(anything()).inAdapterView(withId(R.id.serviceList)).atPosition(0).perform(longClick());
            onData(anything()).inAdapterView(withId(R.id.serviceList)).atPosition(0).perform(longClick());
        }catch (Exception e){
            System.out.println("No services available");
        }
        onView(withId(R.id.backBtn)).perform(click());


    }
}