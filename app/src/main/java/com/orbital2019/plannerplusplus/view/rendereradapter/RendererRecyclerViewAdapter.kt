/**
 * Adapters provide a binding from an app-specific data set to views that are displayed within a RecyclerView.
 * This interface is aimed at creating one adapter which is able to support multiple RecyclerView and ViewItems
 */

package com.orbital2019.plannerplusplus.view.rendereradapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 * TestAdapter aims at creating an adapter that:
 *     works with RecyclerView without implementing a new adapter;
 *     allows us to reuse cells in another RecyclerView;
 *     allows us to easily add other cell types into RecyclerView.
 */
class RendererRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Adapter must be able to render different views according to different Renderers supplied
     */
    private val mRenderers = SparseArray<ViewRenderer<ItemModel, RecyclerView.ViewHolder>>()

    var mItems = ArrayList<ItemModel>()
        /**
         * set must be able to accept different model lists
         */
        internal set(items) {
            mItems.clear()
            mItems.addAll(items)
        }

    override fun getItemCount(): Int {
        return mItems.size
    }

    /**
     * Inflate the view that we will be using to hold each list item.
     * This method will be called when RecyclerView needs a new ViewHolder of the given type to represent
     * Allows different views (and corresponding ViewHolders) to be created according to the type of mRenderer
     * @param parent: ViewGroup into which the new View will be added after it is bound to an adapter position
     * @param viewType: the type of the new View.
     * @return a new ViewHolder that holds a View with the layout for the ItemModel of that type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val renderer = mRenderers.get(viewType)
        if (renderer != null) {
            return renderer.createViewHolder(parent)
        }
        throw RuntimeException("Not supported Item View Type: $viewType")
    }

    /**
     * To allow this Adapter to bind different viewers, it should be able to delegate the binding.
     * This will allow us to reuse the implementation afterwards.
     * This method will be called by RecyclerView to display the item at the given position into ViewHolder.
     * onBindViewHolder binds data from ItemModel entities to the view of the ViewHolder, then displays the completed
     * view at the given position.
     * @Param holder: the holder object containing the different component views of the item
     * @Param position: the index of the current item being bound
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: ItemModel = mItems[position]
        val renderer = mRenderers.get(item.getType())
        if (renderer != null) {
            renderer.bindView(item, holder)
        } else {
            throw RuntimeException("Not supported View Holder: $holder")
        }
    }

    /**
     * Adds a new Renderer to the list of Renderers supported in this Adapter
     * Renderers are stored in a SparseArray with package level constants used as keys for each type.
     */
    fun registerRenderer(renderer: ViewRenderer<ItemModel, RecyclerView.ViewHolder>) {
        val type = renderer.type
        if (mRenderers.get(type) == null) {
            mRenderers.put(type, renderer)
        } else {
            throw RuntimeException("ViewRenderer already exist with this type: $type")
        }
    }

    /**
     * adapter must be able to work with multiple view types.
     * getItemViewType allows us to check the type of the view item.
     */
    override fun getItemViewType(position: Int): Int {
        val item = mItems[position]
        return item.getType()
    }

    fun getItems(): ArrayList<ItemModel> {
        val items = ArrayList<ItemModel>()
        items.addAll(mItems)
        return items
    }
}