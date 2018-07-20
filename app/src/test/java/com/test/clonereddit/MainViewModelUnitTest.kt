package com.test.clonereddit

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.view.View
import com.test.clonereddit.modules.newsfeed.model.Topic
import com.test.clonereddit.modules.newsfeed.viewmodel.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelUnitTest {
  @Rule
  @JvmField
  val rule = InstantTaskExecutorRule()

  val mainViewModel = MainViewModel()

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)
  }

  /***
   * Test single topic
   */
  @Test
  fun testAddSingleTopic() {
    mainViewModel.addTopic(Topic("test1", "time1"))

    assertEquals(View.VISIBLE, mainViewModel.contentVisibility.get())
    assertEquals(View.GONE, mainViewModel.emptyStateVisibility.get())

    val mockTopicList = mainViewModel.getTopicsLiveData()?.value
    assertTrue(mockTopicList?.isNotEmpty()!!)
    assertEquals(0, mockTopicList[0].voteCounts)
    assertEquals("test1", mockTopicList[0].title)
  }

  /***
   * Test multiple topics
   * Expected: The latest topic should be on top
   */
  @Test
  fun testAddMultipleTopic() {
    mainViewModel.addTopic(Topic("test1", "time1"))
    mainViewModel.addTopic(Topic("test2", "time2"))

    val mockTopicList = mainViewModel.getTopicsLiveData()?.value
    assertTrue(mockTopicList?.isNotEmpty()!!)
    assertEquals(0, mockTopicList[0].voteCounts)
    assertEquals("test2", mockTopicList[0].title)

    assertEquals(2, mockTopicList?.size)
    assertEquals("time1", mockTopicList[1].formattedTime)
  }

  /***
   * Test multiple topics with different votecounts
   * Expected: The highest voted topic should be at the top
   */
  @Test
  fun testVoteDescendingOrderTopic() {
    mainViewModel.addTopic(Topic("test1", "time1"))
    mainViewModel.addTopic(Topic("test2", "time2", voteCounts = 2))
    mainViewModel.addTopic(Topic("test3", "time3", voteCounts = 1))

    val mockTopicList = mainViewModel.getTopicsLiveData()?.value
    assertTrue(mockTopicList?.isNotEmpty()!!)
    assertEquals(3, mockTopicList?.size)

    assertEquals(2, mockTopicList[0].voteCounts)
    assertEquals("test2", mockTopicList[0].title)

    assertEquals(1, mockTopicList[1].voteCounts)
    assertEquals("test3", mockTopicList[1].title)
  }

  /***
   * Test upvote topic action
   * Expected: The votes for topic should increase
   */
  @Test
  fun testUpvoteTopic() {
    mainViewModel.addTopic(Topic("test1", "time1"))
    mainViewModel.addTopic(Topic("test2", "time2", voteCounts = 2))
    mainViewModel.addTopic(Topic("test3", "time3", voteCounts = 1))

    val mockTopicList = mainViewModel.getTopicsLiveData()?.value
    assertTrue(mockTopicList?.isNotEmpty()!!)
    assertEquals(3, mockTopicList?.size)

    assertEquals(2, mockTopicList[0].voteCounts)
    assertEquals("test2", mockTopicList[0].title)

    assertEquals(1, mockTopicList[1].voteCounts)
    assertEquals("test3", mockTopicList[1].title)

    // upvote first added topic
    mainViewModel?.upvoteTopic(topicId = 0)
    assertTrue(mockTopicList[2].isUpvoted)
    assertFalse(mockTopicList[2].isDownvoted)
    assertEquals(1, mockTopicList[2].voteCounts)
  }

  /***
   * Test downvote topic action
   * Expected: The votes for topic should decrease
   */
  @Test
  fun testDownvoteTopic() {
    mainViewModel.addTopic(Topic("test1", "time1"))
    mainViewModel.addTopic(Topic("test2", "time2", voteCounts = 2))
    mainViewModel.addTopic(Topic("test3", "time3", voteCounts = 1))

    val mockTopicList = mainViewModel.getTopicsLiveData()?.value
    assertTrue(mockTopicList?.isNotEmpty()!!)
    assertEquals(3, mockTopicList?.size)

    assertEquals(2, mockTopicList[0].voteCounts)
    assertEquals("test2", mockTopicList[0].title)

    assertEquals(1, mockTopicList[1].voteCounts)
    assertEquals("test3", mockTopicList[1].title)

    // downvote first added topic
    mainViewModel?.downvoteTopic(topicId = 0)
    assertTrue(mockTopicList[2].isDownvoted)
    assertFalse(mockTopicList[2].isUpvoted)
    assertEquals(-1, mockTopicList[2].voteCounts)
  }

}