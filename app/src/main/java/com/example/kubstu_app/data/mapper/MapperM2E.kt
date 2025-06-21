package com.example.kubstu_app.data.mapper

import com.example.kubstu_app.data.local.entities.*
import com.example.kubstu_app.data.model.*

fun KindOfNagrModel.toEntity(): KindOfNagrEntity {
    return KindOfNagrEntity(
        id = this.kindofnagr_id,
        name = this.kindofnagr_name
    )
}

fun KindOfNagrEntity.toModel(): KindOfNagrModel {
    return KindOfNagrModel(
        kindofnagr_id = this.id,
        kindofnagr_name = this.name
    )
}

//fun List<KindOfNagrModel>.toEntity(): List<KindOfNagrEntity> {
//    return this.map { it.toEntity() }
//}

//fun List<KindOfNagrEntity>.toModel(): List<KindOfNagrModel> {
//    return this.map { it.toModel() }
//}

// --- Мапперы для оценок ---

fun ExamModel.toEntity(semesterTitle: String): GradesEntity {
    return GradesEntity(
        id = this.id, // ID экзамена становится ID записи в таблице оценок
        student_id = this.student_id,
        disc_name = this.disc_name,
        tipkontr_id = this.tipkontr_id,
        tipkontr_lname = this.tipkontr_lname,
        oc_f = this.oc_f,
        work_name = this.work_name,
        filename = this.filename,
        file_id = this.file_id,
        title = semesterTitle // Заголовок семестра из SemesterModel
    )
}

fun ZachetModel.toEntity(semesterTitle: String): GradesEntity {
    return GradesEntity(
        id = this.id, // ID зачета становится ID записи в таблице оценок
        student_id = this.student_id,
        disc_name = this.disc_name,
        tipkontr_id = this.tipkontr_id,
        tipkontr_lname = this.tipkontr_lname,
        oc_f = this.oc_f,
        work_name = this.work_name,
        filename = this.filename,
        file_id = this.file_id,
        title = semesterTitle // Заголовок семестра из SemesterModel
    )
}

fun SemesterModel.toGradesEntityList(): List<GradesEntity> {
    val semesterTitle = this.title // Получаем заголовок семестра

    // Маппим экзамены, добавляя title семестра
    val examGrades = this.e.map { examModel ->
        examModel.toEntity(semesterTitle)
    }

    // Маппим зачеты, добавляя title семестра
    val zachetGrades = this.z.map { zachetModel ->
        zachetModel.toEntity(semesterTitle)
    }

    // Объединяем оба списка в один
    return examGrades + zachetGrades
}


// --- Мапперы для Disc ---

fun DiscModel.toEntity(): DiscEntity {
    return DiscEntity(
        id = this.disc_id,
        name = this.disc_name
    )
}

fun DiscEntity.toModel(): DiscModel {
    return DiscModel(
        disc_id = this.id,
        disc_name = this.name
    )
}

//fun List<DiscModel>.toEntity(): List<DiscEntity> {
//    return this.map { it.toEntity() }
//}
//
//fun List<DiscEntity>.toModel(): List<DiscModel> {
//    return this.map { it.toModel() }
//}

// --- Мапперы для Lesson ---

// Важное примечание: LessonModel содержит вложенные KindOfNagrModel и DiscModel.
// При маппинге LessonModel в LessonEntity, нам нужны только ИДЕНТИФИКАТОРЫ
// этих вложенных объектов (kindofnagr_id и disc_id), так как LessonEntity
// хранит только их ID для внешних ключей.
// Мы предполагаем, что KindOfNagrEntity и DiscEntity уже были сохранены в БД
// или будут сохранены перед LessonEntity.

fun LessonModel.toEntity(): LessonEntity {
    return LessonEntity(
        date = this.date,
        pair = this.pair,
        classroom = this.classroom,
        teacher = this.teacher,
        kindofnagr_id = this.kindofnagr.kindofnagr_id,
        disc_id = this.disc.disc_id
    )
}

// Функция расширения для списка LessonModel в список LessonEntity
fun List<LessonModel>.toEntity(): List<LessonEntity> {
    return this.map { it.toEntity() }
}

// Функция расширения для списка LessonEntity в список LessonModel
// Это более сложный случай, как описано выше. Для простых случаев можно использовать так:
// fun List<LessonEntity>.toLessonModelList(): List<LessonModel> {
//     return this.map { it.toLessonModel() } // Требует, чтобы toLessonModel был определен без доп. параметров
// }

// Если тебе нужно получить полные модели уроков для UI, лучше использовать
// отдельную @Relation в Room для получения LessonWithDetails или подобной модели,
// которая уже будет содержать вложенные данные.
// Это выходит за рамки простого маппинга "Entity в Model" без дополнительных запросов к БД.

// Маппинг из LessonEntity в LessonModel более сложен,
// так как для создания LessonModel нам нужны полные KindOfNagrModel и DiscModel,
// а LessonEntity хранит только их ID.
// Для этого тебе, возможно, потребуется DAO или репозиторий для получения
// соответствующих KindOfNagrEntity и DiscEntity из базы данных.
// Это пример маппинга, который может потребовать дополнительной логики в репозитории/DAO.
// В ViewModel ты мог бы маппить LessonEntity в более простую ViewState-модель,
// которая включает эти данные напрямую.
// Например, в репозитории ты можешь использовать @Relation для Room, чтобы получить
// вложенные объекты, или вручную доставать их из DAO.

/*
Пример сложного маппинга, который МОЖЕТ потребоваться, но обычно делается иначе (например, через @Relation в Room):
Если ты хочешь получить LessonModel из LessonEntity, тебе придется:
1. Получить LessonEntity.
2. Используя kindofnagr_id из LessonEntity, получить KindOfNagrEntity из KindOfNagrDao.
3. Используя disc_id из LessonEntity, получить DiscEntity из DiscDao.
4. Затем создать KindOfNagrModel и DiscModel из полученных Entity.
5. Наконец, создать LessonModel.

fun LessonEntity.toLessonModel(
    kindOfNagrDao: KindOfNagrDao, // Предполагаем, что у тебя есть DAO для KindOfNagrEntity
    discDao: DiscDao // Предполагаем, что у тебя есть DAO для DiscEntity
): LessonModel {
    val kindOfNagrEntity = kindOfNagrDao.getKindOfNagrById(this.kindofnagr_id)
        ?: throw IllegalStateException("KindOfNagr not found for ID: ${this.kindofnagr_id}")
    val discEntity = discDao.getDiscById(this.disc_id)
        ?: throw IllegalStateException("Disc not found for ID: ${this.disc_id}")

    return LessonModel(
        date = this.date,
        pair = this.pair,
        kindofnagr = kindOfNagrEntity.toKindOfNagrModel(), // Используем маппер для KindOfNagrEntity
        disc = discEntity.toDiscModel(), // Используем маппер для DiscEntity
        classroom = this.classroom,
        teacher = this.teacher
    )
}
*/