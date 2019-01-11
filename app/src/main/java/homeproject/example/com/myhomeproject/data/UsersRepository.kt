package homeproject.example.com.myhomeproject.data

import com.google.android.gms.tasks.Task
import homeproject.example.com.myhomeproject.model.User

interface UsersRepository {
    fun isUserExistsForEmail(email: String): Task<Boolean>
    fun createUser(user: User, password: String): Task<Unit>
}