package com.example.kubstu_app.data.local.dao

import androidx.room.*
import com.example.kubstu_app.data.local.entities.*

@Dao
interface AppDao {

    // Сохранить данные студента после логина / Обновить данные студента
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)

    // Получить данные текущего студента
    @Query("SELECT * FROM student WHERE id = :studentId LIMIT 1")
    suspend fun getStudentById(studentId: Int): StudentEntity?

    // Удалить данные студента (при выходе из аккаунта)
    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    // Сохранить все оценки и зачёты, полученные с сервера
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllControlResults(results: List<GradesEntity>)

    // Получить все оценки и зачёты для определенного студента
    @Query("SELECT * FROM grades WHERE student_id = :studentId")
    suspend fun getControlResultsForStudent(studentId: Int): List<GradesEntity>

    // Удалить все старые оценки и зачёты для студента (перед загрузкой новых)
    @Query("DELETE FROM grades WHERE student_id = :studentId")
    suspend fun deleteControlResultsForStudent(studentId: Int)

    // Сохранить все формы обучения
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFormaOb(list: List<FormaObEntity>)

    // Получить весь список форм обучения
    @Query("SELECT * FROM formaob ORDER BY name ASC")
    suspend fun getAllFormaOb(): List<FormaObEntity>

    // Получить одну форму обучения по ID
    @Query("SELECT * FROM formaob WHERE id = :id LIMIT 1")
    suspend fun getFormaObById(id: Int): FormaObEntity?

    // Сохранить все институты
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInstitutes(list: List<InstituteEntity>)

    // Получить весь список институтов
    @Query("SELECT * FROM inst ORDER BY name ASC")
    suspend fun getAllInstitutes(): List<InstituteEntity>

    // Получить один институт по ID
    @Query("SELECT * FROM inst WHERE id = :id LIMIT 1")
    suspend fun getInstituteById(id: Int): InstituteEntity?

    // Сохранить все направления подготовки
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSpecializations(list: List<SpecsEntity>)

    // Получить весь список направлений подготовки
    @Query("SELECT * FROM spec ORDER BY name ASC")
    suspend fun getAllSpecializations(): List<SpecsEntity>

    // Получить одно направление подготовки по ID
    @Query("SELECT * FROM spec WHERE id = :id LIMIT 1")
    suspend fun getSpecializationById(id: Int): SpecsEntity?

    // Сохранить все группы, полученные с сервера
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGroups(list: List<GroupEntity>)

    // Получить группы по фильтрам: текстовое совпадение группы, институт, форма обучения, курс
    @Query("""
    SELECT * FROM groups
    WHERE (:nameQuery IS NULL OR name LIKE '%' || :nameQuery || '%')
    AND (:instId IS NULL OR inst_id = :instId)
    AND (:formaobId IS NULL OR formaob_id = :formaobId)
    AND (:kurs IS NULL OR kurs = :kurs)
    ORDER BY name ASC
""")
    suspend fun getGroups(
        nameQuery: String?,
        instId: Int?,
        formaobId: Int?,
        kurs: Int?
    ): List<GroupEntity>

    // Получить одну группу по ID
    @Query("SELECT * FROM groups WHERE id = :id LIMIT 1")
    suspend fun getGroupById(id: Int): GroupEntity?

    // Сохранить все элементы расписания экзаменов для определенной группы
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExamSchedules(schedules: List<ExamScheduleEntity>)

    // Получить расписание экзаменов для определенной группы
    @Query("SELECT * FROM exam_schedule ORDER BY date_sd ASC, time_sd ASC")
    suspend fun getExamScheduleForGroup(): List<ExamScheduleEntity>

    // Удалить старое расписание экзаменов для группы (перед загрузкой нового)
    @Query("DELETE FROM exam_schedule")
    suspend fun deleteExamScheduleForGroup()

    // Сохранить все загруженные работы студента
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUploadedWorks(works: List<WorkEntity>)

    // Получить все загруженные работы для определенного студента
    @Query("SELECT * FROM works WHERE student_id = :studentId ORDER BY ugod DESC, semestr DESC, work_name ASC")
    suspend fun getUploadedWorksForStudent(studentId: Int): List<WorkEntity>

    // Удалить старые загруженные работы для студента (перед загрузкой новых)
    @Query("DELETE FROM works WHERE student_id = :studentId")
    suspend fun deleteUploadedWorksForStudent(studentId: Int)

    // Сохранить/обновить заметку для конкретной дисциплины.
    // Если заметки ещё нет, создать её. Если есть – обновить.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(note: StudentNoteEntity)

    // Получить заметку для конкретной дисциплины
    // (чтобы показать её или отредактировать)
    @Query("SELECT * FROM notes WHERE disc_id = :disciplineId LIMIT 1")
    suspend fun getNoteForDiscipline(disciplineId: Int): StudentNoteEntity?

    // Удалить заметку для дисциплины
    @Delete
    suspend fun deleteNote(note: StudentNoteEntity)

    // Сохранить все дисциплины, полученные с сервера
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDiscs(discs: List<DiscEntity>)

    // Получить дисциплину по ID
    @Query("SELECT * FROM disc WHERE id = :id LIMIT 1")
    suspend fun getDiscById(id: Int): DiscEntity?

    // Получить дисциплину по названию
    @Query("SELECT * FROM disc WHERE disc_name = :name LIMIT 1")
    suspend fun getDiscByName(name: String): DiscEntity?

    // Получить все дисциплины
    @Query("SELECT * FROM disc ORDER BY disc_name ASC")
    suspend fun getAllDiscs(): List<DiscEntity>



}