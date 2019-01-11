package homeproject.example.com.myhomeproject.common

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

//переконвертировали Task любого типа  в Task<Unit>
fun Task<*>.toUnit(): Task<Unit> = onSuccessTask { Tasks.forResult(Unit) }