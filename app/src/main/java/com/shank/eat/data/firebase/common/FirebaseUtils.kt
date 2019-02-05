package com.shank.eat.data.firebase.common

import android.arch.lifecycle.LiveData
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


//получаем экземпляр класса. Который поможет нам с авторизацией пользователей
val auth: FirebaseAuth = FirebaseAuth.getInstance()
//ссылка на базу данных. Данный класс поможет нам работать с базой данных
val database: DatabaseReference = FirebaseDatabase.getInstance().reference
//ссылка на хранилище. Данный класс поможет нам работать с хранилищем
val storage: StorageReference = FirebaseStorage.getInstance().reference


//возвращает нам liveDATA
fun DatabaseReference.liveData(): LiveData<DataSnapshot> = FirebaseLiveData(this)

// Configure Google Sign In
val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken("847404471501-9o80te7fpo8uttrdcat5di5ib5m31k10.apps.googleusercontent.com")
    .requestEmail()
    .build()

//create google sigin client
fun googleSignInClient(context: Context) = GoogleSignIn.getClient(context, gso)
