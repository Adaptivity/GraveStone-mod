package gravestone.structures.graves;

import java.util.LinkedList;
import java.util.Random;
import gravestone.core.GSBiomes;
import gravestone.config.GraveStoneConfig;
import gravestone.structures.GSStructureGenerator;
import gravestone.structures.catacombs.CatacombsGenerator;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class SingleGraveGenerator implements GSStructureGenerator {
    private static SingleGraveGenerator instance;
    private SingleGraveGenerator() {
        instance = this;
    }
    
    public static SingleGraveGenerator getInstance() {
        if (instance == null) {
            return new SingleGraveGenerator();
        } else {
            return instance;
        }
    }
    
    // chance to generate a structure
    public static final double CHANCE = 0.1D;
    public static final byte RANGE = 100;
    private static LinkedList<ChunkCoordIntPair> structuresList = new LinkedList();
    
    @Override
    public boolean generate(World world, Random rand, int x, int z, double chance, boolean isCommand) {
        if (isCommand || (GraveStoneConfig.generateSingleGraves && canSpawnStructureAtCoords(world, x, z, chance))) {
            new ComponentGSSingleGrave(rand.nextInt(4), rand, x, z).addComponentParts(world, rand);
            structuresList.add(new ChunkCoordIntPair(x, z));
            return true;
        }

        return false;
    }

    protected static boolean canSpawnStructureAtCoords(World world, int x, int z, double chance) {
        return chance < CHANCE && !GSBiomes.getGravesBiomes().contains(world.getBiomeGenForCoords(x, z).biomeID) && noAnyInRange(x, z);
    }

    protected static boolean noAnyInRange(int x, int z) {
        for (ChunkCoordIntPair position : structuresList) {
            if (position.chunkXPos > x - RANGE && position.chunkXPos < x + RANGE
                    && position.chunkZPos > z - RANGE && position.chunkZPos < z + RANGE) {
                return false;
            }
        }

        for (ChunkCoordIntPair position : CatacombsGenerator.getStructuresList()) {
            if (position.chunkXPos > x - CatacombsGenerator.CATACOMBS_RANGE && position.chunkXPos < x + CatacombsGenerator.CATACOMBS_RANGE
                    && position.chunkZPos > z - CatacombsGenerator.CATACOMBS_RANGE && position.chunkZPos < z + CatacombsGenerator.CATACOMBS_RANGE) {
                return false;
            }
        }

        return true;
    }

    public static LinkedList<ChunkCoordIntPair> getStructuresList() {
        return structuresList;
    }
}
