package com.sliideusers

import org.junit.After
import org.junit.Before
import org.mockito.MockitoAnnotations

abstract class BaseTest {
    private lateinit var closeable: AutoCloseable

    @Before
    fun openMocks() {
        closeable = MockitoAnnotations.openMocks(this)
    }

    @After
    fun releaseMocks() {
        closeable.close()
    }
}