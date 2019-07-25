package com.orbital2019.plannerplusplus.view.rendereradapter

/**
 * ItemModel represents an Item which can be passed to a ViewRenderer to fill a RecyclerView
 */
interface ItemModel {
    /**
     * Type of the ItemModel passed to ViewHolder.
     * Package constants will be used to ensure that these are consistent
     */
    fun getType(): Int
}