package com.nguyenquyhy.SuperActivation.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class ItemStackHelper {
	private static class CachedItem {
		public final int id;
		public final int meta;
		public final String stringId;
		public final String unlocalizedName;
		public final String localizedName;
		public final ItemStack itemStack;

		public CachedItem(int id, int meta, String stringId, String unlocalizedName, String localizedName, ItemStack itemStack) {
			this.id = id;
			this.meta = meta;
			this.stringId = stringId;
			this.unlocalizedName = unlocalizedName;
			this.localizedName = localizedName;
			this.itemStack = itemStack;
		}
	}

	private static final LanguageRegistry languageRegistry = LanguageRegistry.instance();
	private static List<CachedItem> cachedItems = null;

	public static void initialize() {
		if (cachedItems == null) {
			cachedItems = new ArrayList<CachedItem>();
			Minecraft mc = Minecraft.getMinecraft();
			Set keys = Block.blockRegistry.getKeys();
			
			for (Object key : keys) {
				if (key instanceof ResourceLocation) {
					ResourceLocation resourceLocation = (ResourceLocation) key;
					String resourceName = resourceLocation.getResourceDomain() + ":" + resourceLocation.getResourcePath();
					Block block = Block.blockRegistry.getObject(resourceLocation);
					if (block != null && !(block instanceof BlockAir)) {
						// System.out.println(stringId);

						Item item = Item.getItemFromBlock(block);
						if (item != null) {
							// Obtainable blocks
							ItemStack itemStack = new ItemStack(block);
							int id = Block.getIdFromBlock(block);
							Boolean hasSubType = item.getHasSubtypes();
							if (hasSubType) {
								List list = new ArrayList();
								item.getSubItems(item, item.getCreativeTab(),
										list);
								for (Object obj : list) {
									itemStack = (ItemStack) obj;
									String unlocalizedName = itemStack.getUnlocalizedName();
									String localizedName = itemStack.getDisplayName();
									cachedItems.add(new CachedItem(id, itemStack.getItemDamage(),
											resourceName, unlocalizedName, localizedName, itemStack));
								}
							} else {
								String localizedName = block.getLocalizedName();
								String unlocalizedName = block.getUnlocalizedName();
								cachedItems.add(new CachedItem(id, 0, resourceName,
										unlocalizedName, localizedName, itemStack));
							}
						} else {
							// Unobtainable blocks
						}
					}
				}
			}
		}
	}

	public static ItemStack findItemStackByInput(String input) {
		if (input != null && !input.isEmpty()) {
			// Full match
			for (CachedItem cachedItem : cachedItems) {
				if (input.equals(Integer.toString(cachedItem.id))
						|| input.equalsIgnoreCase(cachedItem.stringId)
						|| input.equalsIgnoreCase(cachedItem.unlocalizedName)
						|| input.equalsIgnoreCase(cachedItem.localizedName)) {
					return cachedItem.itemStack;
				}
			}

			// Partial match
			for (CachedItem cachedItem : cachedItems) {
				if (cachedItem.stringId.contains(input)
						|| cachedItem.unlocalizedName.contains(input)
						|| cachedItem.localizedName.toLowerCase().contains(
								input.toLowerCase())) {
					return cachedItem.itemStack;
				}
			}

			Item item = GameRegistry.findItem("pixelmon", input);
			if (item != null) {
				return new ItemStack(item, 1);
			}
		}
		return null;
	}

	public static ItemStack getByUnlocalizedName(String unlocalizedName) {
		if (unlocalizedName != null && !unlocalizedName.isEmpty()) {
			for (CachedItem cachedItem : cachedItems) {
				if (unlocalizedName.equals(cachedItem.unlocalizedName)) {
					return cachedItem.itemStack;
				}
			}
		}
		return null;
	}
}
