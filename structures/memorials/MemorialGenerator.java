package gravestone.structures.memorials;

import java.util.LinkedList;
import java.util.Random;
import gravestone.GraveStoneLogger;
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
public class MemorialGenerator implements GSStructureGenerator {
    
    private static MemorialGenerator instance;
    private MemorialGenerator() {
        instance = this;
    }
    
    public static MemorialGenerator getInstance() {
        if (instance == null) {
            return new MemorialGenerator();
        } else {
            return instance;
        }
    }

    public static final double CHANCE = 0.05D;
    public static final short RANGE = 400;
    private static LinkedList<ChunkCoordIntPair> structuresList = new LinkedList();

    @Override
    public boolean generate(World world, Random rand, int x, int z, double chance, boolean isCommand) {
        if (isCommand || (GraveStoneConfig.generateMemorials && canSpawnStructureAtCoords(world, x, z, chance))) {
            new ComponentGSMemorial(rand.nextInt(4), rand, x, z).addComponentParts(world, rand);
            GraveStoneLogger.logInfo("Generate memorial at " + x + "x" + z);
            structuresList.add(new ChunkCoordIntPair(x, z));
            return true;
        }

        return false;
    }

    protected static boolean canSpawnStructureAtCoords(World world, int x, int z, double chance) {
        return chance < CHANCE && !GSBiomes.getMemorialBiomes().contains(world.getBiomeGenForCoords(x, z).biomeID) && noAnyInRange(x, z);
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
