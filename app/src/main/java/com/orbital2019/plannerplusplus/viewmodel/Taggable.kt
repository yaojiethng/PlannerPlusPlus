package com.orbital2019.plannerplusplus.viewmodel

// support for PlannerItems which allow tags to be added and removed
interface Taggable {
    companion object {
        fun concatTag(tags: List<String>): String {
            return tags.joinToString { it }
        }


        fun splitTag(string: String?): List<String> {
            return string?.split(", ") ?: listOf()
        }
    }
}