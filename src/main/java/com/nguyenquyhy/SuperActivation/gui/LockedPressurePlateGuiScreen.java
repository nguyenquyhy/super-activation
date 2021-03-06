package com.nguyenquyhy.SuperActivation.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.opengl.GL11;

import com.nguyenquyhy.SuperActivation.SuperActivationMod;
import com.nguyenquyhy.SuperActivation.blocks.ItemStackHelper;
import com.nguyenquyhy.SuperActivation.packets.LockActivatorMessage;
import com.nguyenquyhy.SuperActivation.tileentities.LockedActivatorTileEntity;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.io.IOException;

public class LockedPressurePlateGuiScreen extends GuiScreen {
	private String itemDelegateName;
	private String itemLocalizedName;
	private String localizedTitle;

	protected final int xSize;
	protected final int ySize;
	protected final EntityPlayer player;
	protected final World world;
	protected final BlockPos blockPos;

	protected final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

	private GuiTextField blockTextField;
	private GuiButton lockButton;
	private ItemStack blockItemStack;
	private LockedActivatorTileEntity tileEntity;

	private boolean isShowingError = true;

	private ResourceLocation backgroundTexure = new ResourceLocation(SuperActivationMod.MODID, "textures/gui/background.png");

	public LockedPressurePlateGuiScreen(EntityPlayer player, World world, int x, int y, int z) {
		this.player = player;
		this.world = world;
		this.blockPos = new BlockPos(x, y, z);

		this.xSize = 248;
		this.ySize = 166;

		tileEntity = (LockedActivatorTileEntity) world.getTileEntity(blockPos);
		if (tileEntity != null && tileEntity.itemDelegateName != null && !tileEntity.itemDelegateName.isEmpty()) {
			this.itemDelegateName = tileEntity.itemDelegateName;
		} else {
			this.itemDelegateName = null;
		}
	}

	@Override
	public void initGui() {
		ItemStackHelper.initialize();

		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;

		this.buttonList.clear();
		int closeButtonSize = 20;
		GuiButton closeButton = new GuiButton(0, posX + xSize - 5 - closeButtonSize, posY + 10, closeButtonSize, closeButtonSize, "X");
		this.buttonList.add(closeButton);

		this.localizedTitle = I18n.translateToLocal("tile.lockedPressurePlate.name");

		if (this.itemDelegateName == null) {
			// Not locked yet
			this.blockTextField = new GuiTextField(100, this.fontRenderer, posX + 15, posY + 80, 150, 20);
			this.blockTextField.setTextColor(-1);
			this.blockTextField.setDisabledTextColour(0xAAAAAA);
			this.blockTextField.setMaxStringLength(30);
			this.blockTextField.setEnableBackgroundDrawing(true);
			this.blockTextField.setFocused(true);

			GuiButton pickButton = new GuiButton(2, posX + 165, posY + 80, 70, 20, "Pick current");
			this.buttonList.add(pickButton);

			this.lockButton = new GuiButton(1, posX + 15, posY + 110, 100, 20, "Lock this plate");
			this.lockButton.enabled = false;
			this.buttonList.add(lockButton);
		} else {
			// Locked
			String[] tokens = this.itemDelegateName.split(":", 2);
			Item item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(tokens[0], tokens[1]));
			this.blockItemStack = new ItemStack(item);
			this.itemLocalizedName = I18n.translateToLocal(blockItemStack.getUnlocalizedName() + ".name");
		}
	}

	@Override
	protected void keyTyped(char key, int code) throws IOException {
		if (blockTextField != null && blockTextField.isFocused()) {
			blockTextField.textboxKeyTyped(key, code);
			String text = blockTextField.getText();
			updateBlockItemStack(ItemStackHelper.findItemStackByInput(text));
		}
		super.keyTyped(key, code);
	}

	private void updateBlockItemStack(ItemStack itemStack) {
		this.blockItemStack = itemStack;
		if (this.blockItemStack != null) {
			this.itemLocalizedName = I18n.translateToLocal(blockItemStack.getUnlocalizedName() + ".name");
			if (blockItemStack.hasDisplayName()) {
				// Overwrite itemLocalizedName if displayName is provided
				this.itemLocalizedName = blockItemStack.getDisplayName();
			}
			lockButton.enabled = true;
			isShowingError = false;
		} else {
			this.itemDelegateName = null;
			lockButton.enabled = false;
			isShowingError = true;
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			// Close button is pressed
			this.player.closeScreen();
			break;
		case 1:
			// Lock button is pressed
			String delegateName = this.blockItemStack.getItem().delegate.name().toString();
			SuperActivationMod.channel.sendToServer(new LockActivatorMessage(blockPos, delegateName));
			// world.markBlockForUpdate(x, y, z);
			this.player.closeScreen();
			break;
		case 2:
			// Pick current button is pressed
			updateBlockItemStack(this.player.inventory.getCurrentItem());
			if (this.blockItemStack != null) {
				this.blockTextField.setText(this.blockItemStack.getDisplayName());
			} else {
				this.blockTextField.setText("");
			}
			break;
		default:

		}
	}

	@Override
	protected void mouseClicked(int x, int y, int z) throws IOException {
		if (blockTextField != null)
			blockTextField.mouseClicked(x, y, z);
		super.mouseClicked(x, y, z);
	}

	@Override
	public void updateScreen() {
		if (blockTextField != null)
			blockTextField.updateCursorCounter();
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(backgroundTexure);

		this.drawDefaultBackground();
		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;
		this.drawTexturedModalRect(posX, posY, 0, 0, xSize, ySize);

		this.drawCenteredString(this.fontRenderer, this.localizedTitle, posX + xSize / 2, posY + 20, 0xFFFF00);

		if (this.itemDelegateName == null) {
			this.drawString(this.fontRenderer, "Please type in the name/ID of a block", posX + 15, posY + 40, 0xFFFFFF);
			this.drawString(this.fontRenderer, "that is used to lock this plate!", posX + 15, posY + 55, 0xFFFFFF);
			this.blockTextField.drawTextBox();

			if (!isShowingError) {
				try {
					this.drawString(this.fontRenderer, this.itemLocalizedName, posX + 140, posY + 116, 0x00FF00);
					RenderHelper.enableStandardItemLighting();
					renderItem.renderItemIntoGUI(this.blockItemStack, posX + 120, posY + 112);
					RenderHelper.enableGUIStandardItemLighting();
				} catch (Exception ex) {

				}
			} else {
				this.drawString(this.fontRenderer, "Incorrect name/ID!", posX + 120, posY + 116, 0xFF0000);
			}
		} else {
			this.drawCenteredString(this.fontRenderer, "You must be holding a " + this.itemLocalizedName, posX + xSize / 2, posY + 60, 0xFFFFFF);
			this.drawCenteredString(this.fontRenderer, "to activate this plate!", posX + xSize / 2, posY + 80, 0xFFFFFF);

			try {
				RenderHelper.enableStandardItemLighting();
				renderItem.renderItemIntoGUI(this.blockItemStack, posX + xSize / 2 - 8, posY + 100);
				RenderHelper.enableGUIStandardItemLighting();
			} catch (Exception ex) {
				// System.out.println("Error");
			}
		}

		super.drawScreen(i, j, f);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
