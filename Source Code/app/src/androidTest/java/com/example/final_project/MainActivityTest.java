package com.example.final_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.longClick;
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


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

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
                onView(withId(R.id.profileButton)).perform(click());
                break;
            } catch (Exception e) {
                onView(withId(R.id.signInButton)).perform(click());
                continue;
            }
        }

        activityRuleProfile = new ActivityTestRule<>(Profile.class);


        onView(withId(R.id.addressField2)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.phoneNumField)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.nameField)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());


        onView(withId(R.id.addressField2)).perform(typeText("100 Bank Street"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());
        onView(withId(R.id.phoneNumField)).perform(typeText("(905)-806-2222"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());
        onView(withId(R.id.nameField)).perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());

    }
}