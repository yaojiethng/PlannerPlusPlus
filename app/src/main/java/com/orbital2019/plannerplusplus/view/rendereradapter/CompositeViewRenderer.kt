package com.orbital2019.plannerplusplus.view.rendereradapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * composite ViewRenderer must know about child renderers. ViewRenderer will have a list of child renderers
 */
abstract class CompositeViewRenderer<in M : CompositeItemModel, VH : CompositeViewHolder>(
    val context: Context
) :
    ViewRenderer<M, VH>() {

    /**
     * List of child Renderers
     */
    private val mRenderers = ArrayList<ViewRenderer<ItemModel, RecyclerView.ViewHolder>>()
    /**
     * Reference to the Child Adapter which renders child Views
     */
    private lateinit var mAdapter: RendererRecyclerViewAdapter

    constructor(
        context: Context,
        vararg renderers: ViewRenderer<ItemModel, RecyclerView.ViewHolder>
    ) : this(context) {
        mRenderers.addAll(renderers)
    }

    /**
     * Registers a child renderer
     */
    fun registerRenderer(r: ViewRenderer<ItemModel, RecyclerView.ViewHolder>): CompositeViewRenderer<M, VH> {
        mRenderers.add(r)
        return this
    }

    /**
     * First Initializes child adapter and renderers, then
     * Initialize the parent view in createCompositeViewHolder() and then familiarize it with the renderers.
     */
    override fun createViewHolder(parent: ViewGroup): VH {
        // Initialize child renderer
        mAdapter = RendererRecyclerViewAdapter()

        for (renderer: ViewRenderer<ItemModel, RecyclerView.ViewHolder> in mRenderers) {
            mAdapter.registerRenderer(renderer)
        }

        val viewHolder: VH = createCompositeViewHolder(parent)
        // Defaults to LinearLayoutManager. If this has to be changed, method has to be overloaded
        viewHolder.mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        viewHolder.mRecyclerView!!.adapter = mAdapter

        return viewHolder
    }

    /**
     * Creates a ViewHolder which binds data from CompositeItemModel entity to the view of the CompositeViewHolder
     * (Creates and binds parent view)
     */
    abstract fun createCompositeViewHolder(parent: ViewGroup): VH


}