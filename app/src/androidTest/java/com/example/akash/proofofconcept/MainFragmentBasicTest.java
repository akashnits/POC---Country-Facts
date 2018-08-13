package com.example.akash.proofofconcept;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.akash.proofofconcept.networkUtils.ApiClient;
import com.example.akash.proofofconcept.view.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.akash.proofofconcept.TestUtils.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class MainFragmentBasicTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule= new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainScreenLoads(){

        IdlingResource resource= ApiClient.getmResource();

        IdlingRegistry.getInstance().register(resource);

        onView(withRecyclerView(R.id.list_country).atPosition(1)).check(matches(isDisplayed()));

        onView(withRecyclerView(R.id.list_country)
                .atPositionOnView(0, R.id.label_title))
                .check(matches(withText("Beavers")));

        IdlingRegistry.getInstance().unregister(resource);
        }
}
