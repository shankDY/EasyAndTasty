package com.shank.eat.screens.common

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v4.util.AtomicFile
import com.shank.eat.screens.btm_navigation_screens.MainActivity
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.io.filefilter.RegexFileFilter
import org.jetbrains.anko.doAsync
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class PictureHelper(private val fragment: Fragment){

    var imageUri: Uri? = null

    //число по которому мы можем в ответе узнать, что результат выполнился для этой операции или нет
    val CAMERA_TAKE_PICTURE_CODE = 2
    val GET_PICTURE_CODE = 3


    //метод открывает камеру. и загружает полученную фотографию в бд
    fun takeCameraPicture() {

        //стандартное намерение, которое говорит о том, что приложение камера сделаем снимок
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)


        /** Если getPackage () не NULL, будут рассматриваться только компоненты активности в этом пакете приложения.
        Если нет действий, удовлетворяющих всем этим условиям, возвращается пустая строка.
        Если найдены несколько действий, удовлетворяющих цели, будет использоваться та, которая имеет наивысший
        приоритет. Если существует несколько действий с одинаковым приоритетом, система либо выберет наилучшее действие
        на основе предпочтений пользователя, либо перейдет к системному классу, который позволит пользователю выбрать
        действие и перейти оттуда.**/

        // PackageManager: менеджер пакетов, с помощью которого разрешается намерение.
        //Возвращает ComponentName Имя компонента, реализующего действие, которое может отображать намерение.
        // Т.e возращает на имя приложения, которое может обработать наше действие(намерение)
        if (intent.resolveActivity(fragment.context!!.packageManager) != null){
            val imageFile = createImageFile()

            //получаем путь к нашей фотке
            imageUri = FileProvider.getUriForFile(fragment.context!!, "com.shank.eat.fileprovider",
                imageFile)

            //данный код говорит, что положи выходной файл камеры в этот uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri)
            fragment.startActivityForResult(intent, CAMERA_TAKE_PICTURE_CODE)
        }
    }




// данный метод позволяет открыть галерею со всеми фотками
    fun getPicture(){


        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.type = "image/*"

        fragment.startActivityForResult(intent, GET_PICTURE_CODE)
    }



    //создаем файл
    private fun createImageFile(): File {
        //устанавливаем понятный формат даты
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        //  создаем папку в Android External Storage Directory(чтобы другие приложения смогли использовать фотографии)
        val storageDir = fragment.context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Save a file: path for use with ACTION_VIEW intents
        return File.createTempFile(
            "JPEG_${simpleDateFormat.format(Date())}",
            ".jpg",
            storageDir //директория
        )
    }



    // выполняем асинктаск метод для конвертации bitmap в файл
    fun convertBitmapToFile(bitmap: Bitmap): Uri? {

        val file = createImageFile()

        val atomicFile = AtomicFile(file)

        var fos: FileOutputStream? = null
        var oos: ObjectOutputStream? = null

        try {
            fos = atomicFile.startWrite()
            oos = ObjectOutputStream(fos)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,oos)
            oos.writeObject(bitmap)
            oos.flush()
            atomicFile.finishWrite(fos)
        }catch (e: IOException){
            atomicFile.failWrite(fos)
            throw e
        } finally {
            oos?.close()
        }

        return FileProvider.getUriForFile(fragment.context!!, "com.shank.eat.fileprovider", file)


    }


    // в фоне удаляем сделанное юзером фото

    fun fileDell() {
        doAsync {

            val file = File("/storage/emulated/0/Android/data/com.shank.eat/files/Pictures")


            val files = FileUtils.listFiles(file, RegexFileFilter("(.*\\.jpg)||(.*\\.png)"), DirectoryFileFilter.DIRECTORY)

            if(files.isNotEmpty()){

                for (i in files){
                    i.delete()
                }
            }
        }
    }


}


