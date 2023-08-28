package com.salmakhd.dao

import com.salmakhd.model.Article

interface DAOFacade {
    // define as suspending functions due to performing database queries
    suspend fun allArticles(): List<Article>
    suspend fun article(id: Int): Article?
    suspend fun addNewArticle(title: String, body: String): Article?
    suspend fun editArticle(id: Int, title: String, body: String): Boolean
    suspend fun deleteArticle(id: Int): Boolean

}