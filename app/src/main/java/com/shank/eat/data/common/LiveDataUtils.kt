package com.shank.eat.data.common

import android.arch.lifecycle.*

//функция расширения, которая сконвертирует LiveData<Snapshot> в LiveData<Pair<User , List<User>>>
// , для любого типа А , функция(f) которая принимает А и вовращает B
fun <A,B> LiveData<A>.map(f:(A) -> B): LiveData<B> =
        Transformations.map(this,f)