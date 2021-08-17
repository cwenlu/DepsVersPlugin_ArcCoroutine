package com.cwl.arccoroutine.jectpack.paging.data.source.local

import androidx.paging.PagingSource
import androidx.room.*

@Entity
class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val updateTime: Long = System.currentTimeMillis()
)

@Dao
interface PersonDao {

    @Query("select * from PersonEntity order by updateTime desc")
    fun queryAllData(): PagingSource<Int, PersonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(personEntity: List<PersonEntity>)

    @Delete
    fun delete(personEntity: PersonEntity)
}