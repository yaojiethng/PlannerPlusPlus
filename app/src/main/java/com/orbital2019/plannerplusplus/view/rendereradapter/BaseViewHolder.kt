/** Following guide at https://medium.com/@gilbertchristopher/a-recyclerview-with-multiple-view-type-22619a5ad365
 * BaseViewHolder is meant as a base class to be inherited for different ViewHolders nested in Tasks RecyclerView
 *
 */
package com.orbital2019.plannerplusplus.view.rendereradapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * BaseViewHolder is a parent class which extends RecyclerView.ViewHolder,
 * ViewHolder holds references and binds view components within each item View.
 * Generic type parameters are passed to allow reusing the same code with different inputs.
 * @param T  generic type parameter representing the item type bound in respective ViewHolder
 */
abstract class BaseViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * Abstract method bind() needs to be overridden by child classes.
     */
    abstract fun bind(item: T)

}