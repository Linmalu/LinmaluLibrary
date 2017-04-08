/*
 * Copyright (c) 2015 Jerrell Fang
 *
 * This project is Open Source and distributed under The MIT License (MIT)
 * (http://opensource.org/licenses/MIT)
 *
 * You should have received a copy of the The MIT License along with
 * this project.   If not, see <http://opensource.org/licenses/MIT>.
 */

package com.meowj.langutils.lang.convert;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Meow J on 6/20/2015.
 * <p>
 * Used for search
 *
 * @author Meow J
 */
public class ItemEntry {
    private Material material;
    private int metadata;

    public ItemEntry(Material material, int meta) {
        this.material = material;
        this.metadata = meta;
    }

    public ItemEntry(Material material) {
        this(material, 0);
    }

    /**
     * Create an {@link ItemEntry} with {@link ItemStack}
     *
     * @param itemStack The ItemStack that is based on.
     */
    public ItemEntry(ItemStack itemStack) {
        this.material = itemStack.getType();
        this.metadata = itemStack.getDurability();
    }

    /**
     * @return The {@link Material} of the item.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @return The metadata(damage value,durability) of the item.
     */
    public int getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemEntry)) return false;

        ItemEntry itemEntry = (ItemEntry) o;

        return metadata == itemEntry.metadata && material == itemEntry.material;
    }

    @Override
    public int hashCode() {
        int result = material.hashCode();
        result = 31 * result + metadata;
        return result;
    }

    @Override
    public String toString() {
        return this.material.toString() + " " + this.metadata;
    }
}
