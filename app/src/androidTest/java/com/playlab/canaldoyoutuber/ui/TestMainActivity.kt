package com.playlab.canaldoyoutuber.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.playlab.canaldoyoutuber.R
import com.playlab.canaldoyoutuber.ui.adapter.MenuViewHolder
import com.playlab.canaldoyoutuber.ui.fragment.*
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

    @Test
    fun clickGroupsMenuItem_showGroupsFragment (){

        onView(withId(R.id.rv_menu)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MenuViewHolder>(
                3,
                click()
            )
        )
        activityScenarioRule.scenario.onActivity { activity ->
            val currentFragment = activity
                .supportFragmentManager
                .findFragmentByTag(GroupsFragment.KEY)

            assertThat(currentFragment).isNotNull()
            assertThat(currentFragment?.isVisible).isTrue()
        }
    }

    @Test
    fun clickAboutMenuItem_showAboutFragment (){

        onView(withId(R.id.rv_menu)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MenuViewHolder>(
                4,
                click()
            )
        )
        activityScenarioRule.scenario.onActivity { activity ->
            val currentFragment = activity
                .supportFragmentManager
                .findFragmentByTag(AboutChannelFragment.KEY)

            assertThat(currentFragment).isNotNull()
            assertThat(currentFragment?.isVisible).isTrue()
        }
    }

    @Test
    fun clickBooksMenuItem_showBooksFragment (){

        onView(withId(R.id.rv_menu)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MenuViewHolder>(
                5,
                click()
            )
        )
        activityScenarioRule.scenario.onActivity { activity ->
            val currentFragment = activity
                .supportFragmentManager
                .findFragmentByTag(BooksFragment.KEY)

            assertThat(currentFragment).isNotNull()
            assertThat(currentFragment?.isVisible).isTrue()
        }
    }
}