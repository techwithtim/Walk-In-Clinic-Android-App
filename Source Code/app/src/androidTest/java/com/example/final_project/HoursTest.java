package com.example.final_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


@RunWith(AndroidJUnit4.class)

public class HoursTest {

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
                onView(withId(R.id.hoursButton)).perform(click());
                break;
            } catch (Exception e) {
                onView(withId(R.id.signInButton)).perform(click());
                continue;
            }
        }

        onView(withId(R.id.sundayOpening)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.sundayClosing)).perform(replaceText(""), closeSoftKeyboard());

        onView(withId(R.id.sundayClosing)).perform(typeText("17:00"), closeSoftKeyboard());
        onView(withId(R.id.sundayOpening)).perform(typeText("26:04"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());

        onView(withId(R.id.sundayOpening)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.sundayOpening)).perform(typeText("12:77"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());

        onView(withId(R.id.sundayOpening)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.sundayOpening)).perform(typeText("6000"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());

        onView(withId(R.id.sundayOpening)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.sundayOpening)).perform(typeText("27"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());

        onView(withId(R.id.sundayOpening)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.sundayOpening)).perform(typeText("23:"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());

        onView(withId(R.id.sundayOpening)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.sundayOpening)).perform(typeText("8:30"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());
    }
}