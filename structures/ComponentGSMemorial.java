package GraveStone.structures;

import java.util.Random;
import GraveStone.GraveStoneConfig;
import GraveStone.block.BlockGSMemorial;
import GraveStone.tileentity.TileEntityGSMemorial;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 *
 */
public class ComponentGSMemorial extends ComponentGSCemeteryCatacombs {

    public static final int X_LENGTH = 3;
    public static final int HEIGHT = 7;
    public static final int Z_LENGTH = 3;

    public ComponentGSMemorial(int direction, Random random, int x, int z) {
        super(direction);
        boundingBox = getCorrectBox(direction, x + (16 - X_LENGTH) / 2, 64, z + (16 - Z_LENGTH) / 2, X_LENGTH, HEIGHT, Z_LENGTH);
    }

    @Override
    public boolean addComponentParts(World world, Random random) {
        int averageGroundLevel = this.getAverageGroundLevel(world, boundingBox);
        if (averageGroundLevel < 0) {
            return true;
        }

        this.boundingBox.offset(0, averageGroundLevel - boundingBox.maxY + HEIGHT - 1, 0);

        int groundID;
        BiomeGenBase biom = world.getBiomeGenForCoords(getXWithOffset(0, 0), getZWithOffset(0, 0));
        if (biom.equals(BiomeGenBase.desert) || biom.equals(BiomeGenBase.desertHills)) {
            groundID = Block.sand.blockID;
        } else {
            groundID = Block.grass.blockID;
        }
        
        this.fillWithAir(world, boundingBox, 0, 0, 2, 0, 6, 2);
        this.fillWithBlocks(world, boundingBox, 0, 0, 0, 2, 0, 2, groundID, Block.grass.blockID, false);
        
        byte memorialType = BlockGSMemorial.getMemorialType(random, 0);
        placeMemorial(world, random, 1, 1, 1, BlockGSMemorial.getMetaDirection(coordBaseMode), memorialType);
        
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                this.fillCurrentPositionBlocksDownwards(world, groundID, 0, x, -1, z, boundingBox);
            }
        }
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                this.clearCurrentPositionBlocksUpwards(world, x, HEIGHT, z, boundingBox);
            }
        }
        
        return true;
    }
    
    

    protected void placeMemorial(World world, Random random, int x, int y, int z, int memorialMeta, byte memorialType) {
        this.placeBlockAtCurrentPosition(world, GraveStoneConfig.memorialID, memorialMeta, x, y, z, boundingBox);
        TileEntityGSMemorial tileEntity = (TileEntityGSMemorial) world.getBlockTileEntity(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (tileEntity != null) {
            tileEntity.setGraveType(memorialType);
            tileEntity.setMemorialContent(random);
        }
    }
}
