package ru.krivonos.remindercalendar.general.dsl

import ru.krivonos.remindercalendar.general.model.UserModel
import ru.krivonos.remindercalendar.general.model.UserPermission
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class DslTest {

    @Test
    fun `test dsl`() {
        val user: UserModel = user {
            name {
                first = "Ivan"
                second = "Ivanovich"
                last = "Ivanov"
            }
            birth {
                date = LocalDate.parse("2000-01-01")
            }
            contacts {
                phone = "+7987654321"
                email = "ivanov@gmail.com"
            }
            permissions {
                add(UserPermission.READ)
                +UserPermission.WRITE
                add("update")

            }
        }

        assertEquals("Ivan", user.fname)
        assertEquals("Ivanovich", user.sname)
        assertEquals("Ivanov", user.lname)
        assertEquals("2000-01-01", user.dob.toString())
        assertEquals("ivanov@gmail.com", user.email.email)
        assertEquals("+7987654321", user.phone.phone)
        assertTrue(user.permissions.containsAll(
                setOf(
                        UserPermission.READ,
                        UserPermission.WRITE,
                        UserPermission.UPDATE
                )
        ))

    }
}