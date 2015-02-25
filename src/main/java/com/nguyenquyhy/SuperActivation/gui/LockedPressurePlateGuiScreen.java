package com.nguyenquyhy.SuperActivation.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.nguyenquyhy.SuperActivation.SuperActivationMod;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class LockedPressurePlateGuiScreen extends GuiScreen {
	private String unlocalizedBlockName;
	private String localizedBlockName;
	private String localizedTitle;

	protected final int xSize;
	protected final int ySize;
	protected final EntityPlayer player;
	protected final World world;
	protected final int x;
	protected final int y;
	protected final int z;

	private GuiTextField blockTextField;
	
	private boolean isShowingError = false;
	
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

		// this.unlocalizedBlockName = "tile.grass.name";
	}

	@Override
	public void initGui() {
		int posX = (this.width - xSize) / 2;
		int posY = (this.height - ySize) / 2;

		this.buttonList.clear();
		int closeButtonSize = 20;
		GuiButton closeButton = new GuiButton(0, posX + xSize - 5
				- closeButtonSize, posY + 10, closeButtonSize, closeButtonSize,
				"X");
		this.buttonList.add(closeButton);

		LanguageRegistry languageRegistry = LanguageRegistry.instance();
		this.localizedTitle = languageRegistry
				.getStringLocalization("tile.lockedPressurePlate.name");

		if (this.unlocalizedBlockName == null) {
			this.blockTextField = new GuiTextField(this.fontRendererObj,
					posX + 15, posY + 80, 200, 20);
			this.blockTextField.setTextColor(-1);
			this.blockTextField.setDisabledTextColour(0xAAAAAA);
			this.blockTextField.setMaxStringLength(30);
			this.blockTextField.setEnableBackgroundDrawing(true);
			this.blockTextField.setFocused(true);

			GuiButton lockButton = new GuiButton(1, posX + 15, posY + 110, 100,
					20, "Lock");
			this.buttonList.add(lockButton);
		} else {
			this.localizedBlockName = languageRegistry
					.getStringLocalization(this.unlocalizedBlockName);
		}
	}

	@Override
	protected void keyTyped(char key, int code) {
		if (blockTextField.isFocused()) {
			isShowingError = false;
			blockTextField.textboxKeyTyped(key, code);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			this.player.closeScreen();
			break;
		case 1:
			String text = this.blockTextField.getText();
			System.out.println(text);

			isShowingError = true;
			//this.player.closeScreen();
			break;
		default:

		}
	}

	@Override
	protected void mouseClicked(int x, int y, int z) {
		blockTextField.mouseClicked(x, y, z);
		super.mouseClicked(x, y, z);
	}

	@Override
	public void updateScreen() {
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

		if (unlocalizedBlockName == null) {
			this.drawString(this.fontRendererObj,
					"Please type in the name/ID of a block", posX + 15,
					posY + 40, 0xFFFFFF);
			this.drawString(this.fontRendererObj,
					"that is used to lock this plate!", posX + 15, posY + 55,
					0xFFFFFF);
			this.blockTextField.drawTextBox();
			
			if (isShowingError)
				this.drawString(this.fontRendererObj,
						"Incorrect name/ID!", posX + 120, posY + 116,
						0xFF0000);
		} else {
			this.drawString(this.fontRendererObj, "You must be holding a "
					+ this.localizedBlockName, posX + 15, posY + 40, 0xFFFFFF);

			this.drawString(this.fontRendererObj, "to activate this!",
					posX + 15, posY + 60, 0xFFFFFF);
		}
		// RenderHelper.enableStandardItemLighting();

		super.drawScreen(i, j, f);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
