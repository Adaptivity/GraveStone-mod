package gravestone.gui;

import gravestone.ModGraveStone;
import gravestone.core.Resources;
import gravestone.gui.container.AltarContainer;
import gravestone.item.corpse.CorpseHelper;
import gravestone.tileentity.TileEntityGSAltar;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class GSAltarGui extends GuiContainer {

    private final String resurrectionButtonStr = "Resurrect";//ModGraveStone.proxy.getLocalizedString("gui.edit_grave.close");
    private GuiButton resurrectionButton;
    private TileEntityGSAltar tileEntity = null;
    private EntityPlayer player = null;

    public GSAltarGui(InventoryPlayer inventoryPlayer, TileEntityGSAltar tileEntity) {
        super(new AltarContainer(inventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
        this.player = inventoryPlayer.player;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(resurrectionButton = new GuiButton(0, (width - xSize) / 2 + 100, (height - ySize) / 2 + 25, 70, 20, resurrectionButtonStr));
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                if (tileEntity != null && tileEntity.hasCorpse()) {
                    ItemStack corpse = tileEntity.getCorpse();
                    if (corpse != null && CorpseHelper.canSpawnMob(player, corpse.getItemDamage())) {
                        CorpseHelper.spawnMob(corpse.getItemDamage(), tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, corpse.stackTagCompound, player);
                        CorpseHelper.getExperience(player, corpse.getItemDamage());
                        if (!player.capabilities.isCreativeMode) {
                            corpse.stackSize--;
                        }
                    }
                    mc.displayGuiScreen((GuiScreen) null);
                }
                break;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(Resources.ALTAR);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
