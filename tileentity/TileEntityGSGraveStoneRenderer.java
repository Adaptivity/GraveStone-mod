package net.minecraft.GraveStone.tileentity;

import net.minecraft.GraveStone.models.ModelCrossGraveStone;
import net.minecraft.GraveStone.models.ModelGraveStone;
import net.minecraft.GraveStone.models.ModelVerticalPlateGraveStone;
import net.minecraft.GraveStone.models.ModelHorisontalPlateGraveStone;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class TileEntityGSGraveStoneRenderer extends TileEntitySpecialRenderer {

    private static ModelGraveStone verticalPlate = new ModelVerticalPlateGraveStone();
    private static ModelGraveStone cross = new ModelCrossGraveStone();
    private static ModelGraveStone horisontalPlate = new ModelHorisontalPlateGraveStone();

    public TileEntityGSGraveStoneRenderer() {
        System.out.println("TileEntityGSGraveStoneRenderer");
    }

    public void renderAModelAt(TileEntityGSGraveStone tile, double d, double d1, double d2, float f) {
        byte graveType = tile.getGraveType();
        int meta;
        if (tile.worldObj != null) {
            meta = tile.getBlockMetadata();
        } else {
            meta = tile.blockMetadata;
        }
        getGraveTexture(graveType);

        //texture
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);

        if (tile.worldObj != null) {
            switch (getGraveDirection(meta)) {
                case 0:
                    GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
                    break;
                case 1:
                    GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
                    break;
                case 2:
                    GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                    break;
                case 3:
                    GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
                    break;
            }
        } else {
                    GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
        }
        getGraveModel(graveType).renderAll();
        GL11.glPopMatrix(); //end
    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
        this.renderAModelAt((TileEntityGSGraveStone) par1TileEntity, par2, par4, par6, par8);
    }

    private ModelGraveStone getGraveModel(byte graveType) {
        switch (graveType) {
            case 0:
                return verticalPlate;
            case 1:
                return cross;
            case 2:
                return horisontalPlate;
            default:
                return verticalPlate;
        }
    }

    private void getGraveTexture(byte graveType) {
        switch (graveType) {
            case 0: // STONE_VERTICAL_PLATE
                bindTextureByName("/GraveStone/resources/textures/ModelVerticalPlateGraveStone.png");
                break;
            case 1: // STONE_CROSS
                bindTextureByName("/GraveStone/resources/textures/ModelCrossGraveStone.png");
                break;
            case 2: // STONE_HORISONTAL_PLATE
                bindTextureByName("/GraveStone/resources/textures/ModelHorisontalPlateGraveStone.png");
                break;
        }
    }

    /*
     * Return grave direction by metadata
     */
    private static int getGraveDirection(int meta) {
        switch (meta) {
            case 0: // S
                return 0;
            case 1: // N
                return 2;
            case 2: // E
                return 3;
            case 3: // W
                return 1;
            default:
                return 2;
        }
    }
}
