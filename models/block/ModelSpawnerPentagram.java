package gravestone.models.block;

import gravestone.core.Resources;
import gravestone.renderer.tileentity.TileEntityGSMemorialRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class ModelSpawnerPentagram extends ModelBase {

    private ModelRenderer pentagram;
    private ModelSkullCandle candle1;
    private ModelSkullCandle candle2;
    private ModelSkullCandle candle3;
    private ModelSkullCandle candle4;
    private ModelSkullCandle candle5;

    public ModelSpawnerPentagram() {
        textureWidth = 32;
        textureHeight = 32;

        pentagram = new ModelRenderer(this, -32, -32);
        pentagram.addBox(0F, 0F, 0F, 32, 0, 32);
        pentagram.setRotationPoint(-16F, 24F, -16F);
        
        candle1 = new ModelSkullCandle();
        candle2 = new ModelSkullCandle();
        candle3 = new ModelSkullCandle();
        candle4 = new ModelSkullCandle();
        candle5 = new ModelSkullCandle();
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
    
    public void renderAll() {
        pentagram.render(0.0625F);
        
        TileEntityGSMemorialRenderer.instance.bindTextureByName(Resources.WITHER_SKULL_CANDLE);
        
        GL11.glTranslated(0, 0, 1);
        GL11.glRotated(180, 0, 1, 0);
        candle1.renderAll();
        GL11.glRotated(-180, 0, 1, 0);
        
        GL11.glTranslated(0.95, 0, -0.7);
        GL11.glRotated(252, 0, 1, 0);
        candle2.renderAll();
        GL11.glRotated(-252, 0, 1, 0);
        
        GL11.glTranslated(-1.9, 0, 0);
        GL11.glRotated(108, 0, 1, 0);
        candle3.renderAll();
        GL11.glRotated(-108, 0, 1, 0);
        
        GL11.glTranslated(0.36, 0, -1.1);
        GL11.glRotated(36, 0, 1, 0);
        candle4.renderAll();
        GL11.glRotated(-36, 0, 1, 0);
        
        GL11.glTranslated(1.18, 0, 0);
        GL11.glRotated(-36, 0, 1, 0);
        candle5.renderAll();
        GL11.glRotated(36, 0, 1, 0);
    }
}
