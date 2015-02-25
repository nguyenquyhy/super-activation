package com.nguyenquyhy.SuperActivation.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.nguyenquyhy.SuperActivation.SuperActivationMod;
import com.nguyenquyhy.SuperActivation.blocks.ItemStackHelper;
import com.nguyenquyhy.SuperActivation.packets.LockActivatorMessage;
import com.nguyenquyhy.SuperActivation.tileentities.LockedActivatorTileEntity;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class LockedPressurePlateGuiScreen extends GuiScreen {
	private String itemUnlocalizedName;
	private String itemLocalizedName;
	private String localizedTitle;

	protected final int xSize;
	protected final int ySize;
	protected final EntityPlayer player;
	protected final World world;
	protected final int x;
	protected final int y;
	protected final int z;

	protected final RenderItem renderItem = RenderItem.getInstance();
	protected final LanguageRegistry languageRegistry = LanguageRegistry
			.instance();

	private GuiTextField blockTextField;
	private GuiButton lockButton;
	private ItemStack blockItemStack;
	private LockedActivatorTileEntity tileEntity;

	private boolean isShowingError = true;

	private ResourceLocation backgroundTexure = new ResourceLocation(
			SuperActivationMod.MODID, "textures/gui/background.png");

	public LockedPressurePlateGuiScreen(EntityPlayer player, World world,
			int x, int y, int z) {

		this.player = player;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;

		this.xSize = 248;
		this.ySize = 166;

		tileEntity = (LockedActivatorTileEntity) world.getTileEntity(x, y, z);
		if (tileEntity != null && tileEntity.itemUnlocalizedName != null
				&& !tileEntity.itemUnlocalizedName.isEmpty()) {
			this.itemUnlocalizedName = tileEntity.itemUnlocalizedName;
			this.itemLocalizedName = languageRegistry
					.getStringLocalization(this.itemUnlocalizedName + ".name");
		}
	}

	@Override
	public void initGui() {
		// ItemStackHelper.initialize();

		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;

		this.buttonList.clear();
		int closeButtonSize = 20;
		GuiButton closeButton = new GuiButton(0, posX + xSize - 5
				- closeButtonSize, posY + 10, closeButtonSize, closeButtonSize,
				"X");
		this.buttonList.add(closeButton);

		this.localizedTitle = languageRegistry
				.getStringLocalization("tile.lockedPressurePlate.name");

		if (this.itemUnlocalizedName == null) {
			// Not locked yet
			this.blockTextField = new GuiTextField(this.fontRendererObj,
					posX + 15, posY + 80, 200, 20);
			this.blockTextField.setTextColor(-1);
			this.blockTextField.setDisabledTextColour(0xAAAAAA);
			this.blockTextField.setMaxStringLength(30);
			this.blockTextField.setEnableBackgroundDrawing(true);
			this.blockTextField.setFocused(true);

			this.lockButton = new GuiButton(1, posX + 15, posY + 110, 100, 20,
					"Lock this plate");
			this.lockButton.enabled = false;
			this.buttonList.add(lockButton);
		} else {
			// Locked
			this.blockItemStack = ItemStackHelper
					.getByUnlocalizedName(this.itemUnlocalizedName);
		}
	}

	@Override
	protected void keyTyped(char key, int code) {
		if (blockTextField != null && blockTextField.isFocused()) {
			blockTextField.textboxKeyTyped(key, code);

			String text = blockTextField.getText();

			this.blockItemStack = ItemStackHelper.findItemStackByInput(text);

			if (this.blockItemStack != null) {
				this.itemLocalizedName = languageRegistry
						.getStringLocalization(blockItemStack
								.getUnlocalizedName() + ".name");
				if (blockItemStack.hasDisplayName()) {
					// Overwrite itemLocalizedName if displayName is provided
					this.itemLocalizedName = blockItemStack.getDisplayName();
				}
				lockButton.enabled = true;
				isShowingError = false;
			} else {
				lockButton.enabled = false;
				isShowingError = true;
			}
		}
		super.keyTyped(key, code);
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
			String unlocalizedName = this.blockItemStack.getUnlocalizedName();
			SuperActivationMod.channel.sendToServer(new LockActivatorMessage(x,
					y, z, unlocalizedName));
			// world.markBlockForUpdate(x, y, z);
			this.player.closeScreen();
			break;
		default:

		}
	}

	@Override
	protected void mouseClicked(int x, int y, int z) {
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

		// RenderHelper.enableGUIStandardItemLighting();
		this.drawDefaultBackground();
		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;
		this.drawTexturedModalRect(posX, posY, 0, 0, xSize, ySize);

		this.drawCenteredString(this.fontRendererObj, this.localizedTitle, posX
				+ xSize / 2, posY + 20, 0xFFFF00);

		if (itemUnlocalizedName == null) {
			this.drawString(this.fontRendererObj,
					"Please type in the name/ID of a block", posX + 15,
					posY + 40, 0xFFFFFF);
			this.drawString(this.fontRendererObj,
					"that is used to lock this plate!", posX + 15, posY + 55,
					0xFFFFFF);
			this.blockTextField.drawTextBox();

			if (!isShowingError) {
				try {
					this.drawString(this.fontRendererObj,
							this.itemLocalizedName, posX + 140, posY + 116,
							0x00FF00);
					renderItem.renderItemIntoGUI(this.fontRendererObj,
							mc.renderEngine, this.blockItemStack, posX + 120,
							posY + 112);
				} catch (Exception ex) {

				}
			} else {
				this.drawString(this.fontRendererObj, "Incorrect name/ID!",
						posX + 120, posY + 116, 0xFF0000);
			}
		} else {
			this.drawCenteredString(this.fontRendererObj,
					"You must be holding a " + this.itemLocalizedName, posX
							+ xSize / 2, posY + 60, 0xFFFFFF);

			this.drawCenteredString(this.fontRendererObj,
					"to activate this plate!", posX + xSize / 2, posY + 80,
					0xFFFFFF);

			try {
				renderItem.renderItemIntoGUI(this.fontRendererObj,
						mc.renderEngine, this.blockItemStack, posX + xSize / 2
								- 8, posY + 100);
			} catch (Exception ex) {
				System.out.println("Error");
			}
		}
		// RenderHelper.enableStandardItemLighting();

		super.drawScreen(i, j, f);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
