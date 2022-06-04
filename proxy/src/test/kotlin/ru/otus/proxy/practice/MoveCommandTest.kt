package ru.otus.proxy.practice

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

internal class MoveCommandTest {
    @Test
    fun MoveCommandShouldChangeCoordinateProperty() {
        val mock = mock(IMovableObject::class.java)
        `when`(mock.coordinate).thenReturn(10)
        `when`(mock.velocity).thenReturn(7)
        val move = MoveCommand(mock)

        move.execute()

        verify(mock).coordinate = 17
    }

}