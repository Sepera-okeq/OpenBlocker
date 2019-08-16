package ru.will0376.xBlocker.client.network;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import ru.will0376.xBlocker.Main;

import java.util.ArrayList;

public class MessageHandler_list implements IMessageHandler<MessageHandler_list, IMessage>, IMessage {
	String text;

	public MessageHandler_list() {
	}

	public MessageHandler_list(String text) {
		this.text = text;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.text = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.text);
	}

	@Override
	public IMessage onMessage(MessageHandler_list message, MessageContext ctx) {
		if (Main.debug) System.out.println("list: " + message.text);
		try {
			handleClientSide(Integer.valueOf(message.text.split(";")[0]), message.text.split(";")[1], Integer.valueOf(message.text.split(";")[2]), message.text.split(";")[3]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void handleClientSide(int act, String name, int meta, String res) {
		/***
		 * act = 0 - remove from list;
		 * act = 1 - add to list;
		 * act = 2 - clear list;
		 * act = 3 - remove recipe from jei
		 */
		switch (act) {
			case 0:
				Main.getInstance().listItemsClient.remove(name + ":" + meta + "@" + res);
				break;

			case 1:
				Main.getInstance().listItemsClient.add(name + ":" + meta + "@" + res);
				break;

			case 2:
				Main.getInstance().listItemsClient.clear();
				break;

			case 3:
				ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
				ArrayList<IRecipe> recipes = Lists.newArrayList(recipeRegistry.getValues());
				for (IRecipe iRecipe : recipes) {
					if (iRecipe.getRecipeOutput().getItem() == new ItemStack(Item.getByNameOrId(name), 1, meta).getItem()) {
						Minecraft.getMinecraft().addScheduledTask(() -> mezz.jei.plugins.jei.JEIInternalPlugin.jeiRuntime.getRecipeRegistry().removeRecipe(iRecipe));
						//Minecraft.getMinecraft().addScheduledTask(() -> mezz.jei.api.BlankModPlugin);
						break;
					}
				}
				break;
		/*	case 0:
				ConfigUtils.deleteFromFile("list", id + ":" + meta + ":" + res); break;
			case 1: ConfigUtils.addToFile("list", id + ":" + meta + ":" + res); break;
			case 2:	ConfigUtils.readFromFile("list"); break;
			case 3: ConfigUtils.writeToFile("list", res);
				ConfigUtils.readFromFile("list");
				break;
			case 4:
				ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
				ArrayList<IRecipe> recipes = Lists.newArrayList(recipeRegistry.getValues());
				//for (IRecipe iRecipe : recipes) {
					//if(iRecipe.getRecipeOutput() == new ItemStack(Item.getItemById(id),1,meta)){
					//	if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
						//			Minecraft.getMinecraft().addScheduledTask(() -> JEIInternalPlugin.jeiRuntime.getRecipeRegistry().removeRecipe(iRecipe));
					//	break;
				//	}
				//}
				//break;*/
		}
	}

}

