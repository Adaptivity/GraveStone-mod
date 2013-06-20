package GraveStone.structures;

import GraveStone.structures.memorials.MemorialGenerator;
import GraveStone.structures.graves.SingleGraveGenerator;
import GraveStone.structures.catacombs.CatacombsGenerator;
import cpw.mods.fml.common.IWorldGenerator;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class GraveStoneWorldGenerator implements IWorldGenerator {

    protected static CatacombsGenerator catacombsGen;
    protected static MemorialGenerator memorialGen;
    protected static SingleGraveGenerator graveGen;
    public static final byte CATACOMBS_RANGE = 100;

    public GraveStoneWorldGenerator() {
        catacombsGen = new CatacombsGenerator();
        memorialGen = new MemorialGenerator();
        graveGen = new SingleGraveGenerator();
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.dimensionId == 0) {
            generateSurface(world, random, chunkX * 16, chunkZ * 16);
        }
    }

    public void generateSurface(World world, Random rand, int x, int z) {
        double chance = rand.nextDouble();
        if (!catacombsGen.generate(world, rand, x, z, chance)) {
            if (!memorialGen.generate(world, rand, x, z, chance)) {
                graveGen.generate(world, rand, x, z, chance);
            }
        }
    }
}
