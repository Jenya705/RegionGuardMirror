package com.github.jenya705;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.Selection;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.World;

@Data
@AllArgsConstructor
public class RGCreatingInfo {

    private BlockVector selectionMin;
    private BlockVector selectionMax;
    private String regionName;
    private int regionPrice;
    private World world;

    public boolean isSelection(Selection selection){
        return selection != null &&
                isVectorEqualsBlockVector(selection.getNativeMinimumPoint(), selectionMin) &&
                isVectorEqualsBlockVector(selection.getNativeMaximumPoint(), selectionMax);
    }

    public boolean isVectorEqualsBlockVector(Vector vector, BlockVector blockVector){
        return vector.getBlockX() == blockVector.getBlockX() &&
                vector.getBlockY() == blockVector.getBlockY() &&
                vector.getBlockZ() == blockVector.getBlockZ();
    }

}
