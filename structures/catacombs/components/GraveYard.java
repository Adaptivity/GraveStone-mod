package GraveStone.structures.catacombs.components;

import java.util.Random;
import GraveStone.block.BlockGSGraveStone;
import GraveStone.structures.GraveGenerationHelper;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class GraveYard extends CatacombsBaseComponent {

    public GraveYard(int direction, Random random, StructureBoundingBox structureBoundingBox) {
        super(direction);
        boundingBox = structureBoundingBox;
    }

    /**
     * Build component
     */
    @Override
    public boolean addComponentParts(World world, Random random) {
        int graveMeta = BlockGSGraveStone.getMetaDirection(this.coordBaseMode);
        int positionX, positionZ, y;

        for (int x = 0; x < 11; x += 2) {
            for (int z = 0; z < 11; z += 2) {
                if (random.nextDouble() < 0.05) {
                    positionX = getXWithOffset(x + 1, z + 1);
                    positionZ = getZWithOffset(x + 1, z + 1);
                    y = world.getTopSolidOrLiquidBlock(positionX, positionZ) - boundingBox.minY;
                    this.placeBlockAtCurrentPosition(world, Block.deadBush.blockID, 0, x + 1, y, z + 1, boundingBox);
                }

                if (random.nextInt(5) < 2) {
                    positionX = getXWithOffset(x, z);
                    positionZ = getZWithOffset(x, z);
                    y = world.getTopSolidOrLiquidBlock(positionX, positionZ) - boundingBox.minY;
                    if (GraveGenerationHelper.canPlaceGrave(world, positionX, boundingBox.minY + y, positionZ, boundingBox.maxY)) {
                        GraveGenerationHelper.placeGrave(this, world, random, x, y, z, graveMeta, BlockGSGraveStone.getGraveType(random, BlockGSGraveStone.EnumGraveType.PLAYER_GRAVES), false);
                    }
                }
            }
        }

        return true;
    }
}
