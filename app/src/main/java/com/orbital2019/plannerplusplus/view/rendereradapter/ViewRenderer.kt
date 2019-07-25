package com.orbital2019.plannerplusplus.view.rendereradapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * abstract class that will be able to handle only one cell type and a specific ViewHolder.
 * Generates ViewHolder corresponding to the cell type. Generics are used in order to avoid type casts.
 * @param M the type of the cell rendered
 * @param VH the type of the ViewHolder
 */
abstract class ViewRenderer<in M : ItemModel, VH : RecyclerView.ViewHolder> {

    /**
     * Type of the Renderer is based on the type M.
     * Package constants will be used to ensure that these are consistent
     */
    abstract val type: Int

    abstract fun bindView(model: M, holder: VH)
    abstract fun createViewHolder(parent: ViewGroup): VH
}