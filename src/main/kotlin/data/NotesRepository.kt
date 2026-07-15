package data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import db.NoteDb
import domain.NoteItem
import java.io.File

/** 노트를 로컬 SQLite에 저장/로드. DB 파일: ~/.local-ai-note/notes.db. */
class NotesRepo {
    private val db: NoteDb

    init {
        val dir = File(System.getProperty("user.home"), ".local-ai-note").apply { mkdirs() }
        val dbFile = File(dir, "notes.db")
        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
        NoteDb.Schema.create(driver)
        db = NoteDb(driver)
    }

    fun all(): List<NoteItem> =
        db.noteQueries.selectAll().executeAsList().map { NoteItem(it.id, it.title, it.body, it.updatedAt) }

    fun add(title: String, body: String): NoteItem {
        val now = System.currentTimeMillis()
        db.noteQueries.insert(title, body, now)
        val id = db.noteQueries.lastInsertId().executeAsOne()
        return NoteItem(id, title, body, now)
    }

    fun update(n: NoteItem) = db.noteQueries.update(n.title, n.body, n.updatedAt, n.id)

    fun delete(id: Long) = db.noteQueries.delete(id)
}
