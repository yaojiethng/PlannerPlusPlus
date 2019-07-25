package com.orbital2019.plannerplusplus.view.rendereradapter

/**
 * CompositeItemModel that includes child models, inheriting from the base ItemModel class that works with the modified
 * RendererAdapter
 */
interface CompositeItemModel : ItemModel {
    /**
     * List of the child items contained in this ItemModel
     */
    val items: List<ItemModel>
}