package com.github.jenya705;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@UtilityClass
public class RGUtils {

    @Getter
    public static class VectorPair {
        private final BlockVector min;
        private final BlockVector max;
        public VectorPair(BlockVector min, BlockVector max){
            this.min = min;
            this.max = max;
        }
    }

    public static int getRegionSquare(BlockVector vMin, BlockVector vMax){
        return (vMax.getBlockX() - vMin.getBlockX() + 1) * (vMax.getBlockZ() - vMin.getBlockZ() + 1);
    }

    public static boolean isRegionFullyIn(BlockVector cMin, BlockVector cMax, BlockVector bMin, BlockVector bMax){

        // left & right it is some abstract thing
        VectorPair rightPair = new VectorPair(cMin, cMax);
        VectorPair leftPair = new VectorPair(bMin, bMax);

        return  isPointInRegion(leftPair, rightPair.getMin()) &&
                isPointInRegion(leftPair, rightPair.getMax()) &&
                isPointInRegion(leftPair, new BlockVector(rightPair.getMin().getBlockX(), rightPair.getMin().getBlockY(), rightPair.getMax().getBlockZ())) &&
                isPointInRegion(leftPair, new BlockVector(rightPair.getMin().getBlockX(), rightPair.getMax().getBlockY(), rightPair.getMax().getBlockZ())) &&
                isPointInRegion(leftPair, new BlockVector(rightPair.getMax().getBlockX(), rightPair.getMax().getBlockY(), rightPair.getMin().getBlockZ())) &&
                isPointInRegion(leftPair, new BlockVector(rightPair.getMax().getBlockX(), rightPair.getMin().getBlockY(), rightPair.getMax().getBlockZ())) &&
                isPointInRegion(leftPair, new BlockVector(rightPair.getMin().getBlockX(), rightPair.getMax().getBlockY(), rightPair.getMin().getBlockZ())) &&
                isPointInRegion(leftPair, new BlockVector(rightPair.getMax().getBlockX(), rightPair.getMin().getBlockY(), rightPair.getMin().getBlockZ()));

    }

    public static boolean isRegionsIntersect(BlockVector v1min, BlockVector v1max, BlockVector v2min, BlockVector v2max){

        // left & right it is some abstract thing
        VectorPair leftPair = new VectorPair(v1min, v1max);
        VectorPair rightPair = new VectorPair(v2min, v2max);

        return isRegionIntersect(leftPair, rightPair) || isRegionIntersect(rightPair, leftPair);

    }

    private static boolean isRegionIntersect(VectorPair leftPair, VectorPair rightPair){

        /*
        return  isPointInRegion(leftPair, rightPair.getMin()) ||
                isPointInRegion(leftPair, rightPair.getMax()) ||
                isPointInRegion(leftPair, new BlockVector(rightPair.getMin().getBlockX(), rightPair.getMin().getBlockY(), rightPair.getMax().getBlockZ())) ||
                isPointInRegion(leftPair, new BlockVector(rightPair.getMin().getBlockX(), rightPair.getMax().getBlockY(), rightPair.getMax().getBlockZ())) ||
                isPointInRegion(leftPair, new BlockVector(rightPair.getMax().getBlockX(), rightPair.getMax().getBlockY(), rightPair.getMin().getBlockZ())) ||
                isPointInRegion(leftPair, new BlockVector(rightPair.getMax().getBlockX(), rightPair.getMin().getBlockY(), rightPair.getMax().getBlockZ())) ||
                isPointInRegion(leftPair, new BlockVector(rightPair.getMin().getBlockX(), rightPair.getMax().getBlockY(), rightPair.getMin().getBlockZ())) ||
                isPointInRegion(leftPair, new BlockVector(rightPair.getMax().getBlockX(), rightPair.getMin().getBlockY(), rightPair.getMin().getBlockZ()));
        */

        return  leftPair.getMax().getBlockX() >= rightPair.getMin().getBlockX() &&
                leftPair.getMin().getBlockX() <= rightPair.getMax().getBlockX() &&
                leftPair.getMin().getBlockY() <= rightPair.getMax().getBlockY() &&
                leftPair.getMax().getBlockY() >= rightPair.getMin().getBlockY() &&
                leftPair.getMin().getBlockZ() <= rightPair.getMax().getBlockZ() &&
                leftPair.getMax().getBlockZ() >= rightPair.getMin().getBlockZ();

    }

    private static boolean isPointInRegion(VectorPair region, BlockVector point) {

        return  region.getMin().getBlockX() <= point.getBlockX() &&
                region.getMin().getBlockY() <= point.getBlockY() &&
                region.getMin().getBlockZ() <= point.getBlockZ() &&
                region.getMax().getBlockX() >= point.getBlockX() &&
                region.getMax().getBlockY() >= point.getBlockY() &&
                region.getMax().getBlockZ() >= point.getBlockZ();

    }

    public static int getRegionPrice(Selection selection, Player player){
        return getRegionPrice(selection, player, null, null, null, null);
    }

    public static int getRegionPrice(Selection selection, Player player,
                                     ValueContainer<Boolean> intersects,
                                     ValueContainer<Boolean> fully,
                                     ValueContainer<List<ProtectedRegion>> whatIntersects,
                                     ValueContainer<List<ProtectedRegion>> playerRegions){
        RGPlugin plugin = RGPlugin.getInstance();
        Map<String, ProtectedRegion> regions = Objects.requireNonNull(
                plugin.getWorldGuard().getRegionContainer().get(player.getWorld()))
                .getRegions();
        BlockVector selectionMin = new BlockVector(new Vector(selection.getMinimumPoint().getBlockX(),
                selection.getMinimumPoint().getBlockY(), selection.getMinimumPoint().getBlockZ()));
        BlockVector selectionMax = new BlockVector(new Vector(selection.getMaximumPoint().getBlockX(),
                selection.getMaximumPoint().getBlockY(), selection.getMaximumPoint().getBlockZ()));
        boolean regionIntersect = false;
        boolean regionFully = false;
        for (Map.Entry<String, ProtectedRegion> regionEntry: regions.entrySet()){
            ProtectedRegion region = regionEntry.getValue();
            if (region.getOwners().contains(player.getUniqueId())) {
                if (playerRegions != null) playerRegions.getValue().add(region);
                if (regionFully && whatIntersects == null) continue;
                if (RGUtils.isRegionFullyIn(selectionMin, selectionMax,
                        region.getMinimumPoint(), region.getMaximumPoint())) {
                    regionFully = true;
                    if (whatIntersects != null) {
                        whatIntersects.getValue().add(region);
                    }
                    continue;
                }
                if (regionIntersect && whatIntersects == null) continue;;
                if (RGUtils.isRegionsIntersect(region.getMinimumPoint(), region.getMaximumPoint(),
                        selectionMin, selectionMax)) {
                    regionIntersect = true;
                    if (whatIntersects != null) {
                        whatIntersects.getValue().add(region);
                    }
                }
            }
            else if (RGUtils.isRegionsIntersect(region.getMinimumPoint(), region.getMaximumPoint(),
                    selectionMin, selectionMax)){
                return -1;
            }
        }
        if (intersects != null) intersects.setValue(regionIntersect);
        if (fully != null) fully.setValue(regionFully);
        return RGUtils.getRegionSquare(selectionMin, selectionMax) * (regionFully ?
                plugin.getConfiguration().getFullyRegionCoefficient() : (regionIntersect ?
                plugin.getConfiguration().getIntersectRegionCoefficient() :
                plugin.getConfiguration().getOutRegionCoefficient()));
    }

    public static boolean canCreateRegionWithMessage(Player player, boolean fully,
                                                     boolean intersects, Selection selection){
        RGPlugin plugin = RGPlugin.getInstance();
        if (fully){
            if (!(selection.getNativeMaximumPoint().getBlockX() - selection.getNativeMinimumPoint().getBlockX() + 1 >= plugin.getConfiguration().getInRegionMaxX() &&
                    selection.getNativeMaximumPoint().getBlockY() - selection.getNativeMinimumPoint().getBlockY() + 1 >= plugin.getConfiguration().getInRegionMaxY() &&
                    selection.getNativeMaximumPoint().getBlockZ() - selection.getNativeMinimumPoint().getBlockZ() + 1 >= plugin.getConfiguration().getInRegionMaxZ())){
                player.sendMessage(MessageFormat.format(plugin.getConfiguration().getRegionNeedToBeMinimum(),
                        MessageFormat.format(plugin.getConfiguration().getRegionSizeFormat(),
                                Integer.toString(plugin.getConfiguration().getInRegionMaxX()),
                                Integer.toString(plugin.getConfiguration().getInRegionMaxY()),
                                Integer.toString(plugin.getConfiguration().getInRegionMaxZ()))));
                return false;
            }
        }
        else {
            if (!(selection.getNativeMaximumPoint().getBlockX() - selection.getNativeMinimumPoint().getBlockX() + 1 >= plugin.getConfiguration().getOutRegionMaxX() &&
                    selection.getNativeMaximumPoint().getBlockY() - selection.getNativeMinimumPoint().getBlockY() + 1 >= plugin.getConfiguration().getOutRegionMaxY() &&
                    selection.getNativeMaximumPoint().getBlockZ() - selection.getNativeMinimumPoint().getBlockZ() + 1 >= plugin.getConfiguration().getOutRegionMaxZ())){
                player.sendMessage(MessageFormat.format(plugin.getConfiguration().getRegionNeedToBeMinimum(),
                        MessageFormat.format(plugin.getConfiguration().getRegionSizeFormat(),
                                Integer.toString(plugin.getConfiguration().getOutRegionMaxX()),
                                Integer.toString(plugin.getConfiguration().getOutRegionMaxY()),
                                Integer.toString(plugin.getConfiguration().getOutRegionMaxZ()))));
                return false;
            }
        }
        return true;
    }

    public static boolean isRegionFree(Selection selection){
        RGPlugin plugin = RGPlugin.getInstance();
        return selection.getNativeMaximumPoint().getBlockX() - selection.getNativeMinimumPoint().getBlockX() < plugin.getConfiguration().getFreeRegionSize() &&
                selection.getNativeMaximumPoint().getBlockZ() - selection.getNativeMinimumPoint().getBlockZ() < plugin.getConfiguration().getFreeRegionSize();
    }

    public static Map<String, ProtectedRegion> getPlayerRegions(Player player, boolean member) {
        RGPlugin plugin = RGPlugin.getInstance();
        Map<String, ProtectedRegion> worldRegions = plugin.getWorldGuard()
                .getRegionManager(player.getWorld()).getRegions();
        Map<String, ProtectedRegion> result = new HashMap<>();
        for (Map.Entry<String, ProtectedRegion> regionEntry: worldRegions.entrySet()) {
            String name = regionEntry.getKey();
            ProtectedRegion region = regionEntry.getValue();
            if (region.getOwners().contains(player.getUniqueId()) || region.getOwners().contains(player.getName()) || (member &&
                    (region.getMembers().contains(player.getUniqueId()) || region.getMembers().contains(player.getName())))) {
                result.put(name, region);
            }
        }
        return result;
    }

}
