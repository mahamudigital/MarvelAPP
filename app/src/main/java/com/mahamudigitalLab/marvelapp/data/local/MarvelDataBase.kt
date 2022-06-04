package com.mahamudigitalLab.marvelapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mahamudigitalLab.marvelapp.data.model.character.CharacterModel


@Database(entities = [CharacterModel::class], version = 1, exportSchema = false)
@TypeConverters(MarvelConverters::class)
abstract class MarvelDataBase : RoomDatabase(){
    abstract fun marvelDao(): MarvelDao
}