package com.playlab.canaldoyoutuber.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.ui.adapter.MenuViewHolder
import com.playlab.canaldoyoutuber.ui.fragment.PlayListsFragment
import com.playlab.canaldoyoutuber.ui.fragment.SocialNetworksFragment
import org.junit.Rule
import org.junit.Test

class TestMainActivity {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun clickSocialNetworksMenuItem_showSocialNetworksFragment (){

        onView(withId(R.id.rv_menu)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MenuViewHolder>(
                1,
                click()
            )
        )
        activityScenarioRule.scenario.onActivity { activity ->
            val currentFragment = activity
                    .supportFragmentManager
                    .findFragmentByTag(SocialNetworksFragment.KEY)

            assertThat(currentFragment).isNotNull()
            assertThat(currentFragment?.isVisible).isTrue()
        }
    }

    @Test
    fun clickPlayListsMenuItem_showPlayListsFragment (){

        onView(withId(R.id.rv_menu)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MenuViewHolder>(
                2,
                click()
            )
        )
        activityScenarioRule.scenario.onActivity { activity ->
            val currentFragment = activity
                .supportFragmentManager
                .findFragmentByTag(PlayListsFragment.KEY)

            assertThat(currentFragment).isNotNull()
            assertThat(currentFragment?.isVisible).isTrue()
        }
    }
}