package ru.will0376.OpenBlocker.server.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IC2Checker extends TEBase {
	@Override
	public boolean handler(String inputReg, World world, BlockPos pos) {
		try {
			TileEntity entity = world.getTileEntity(pos);
			NBTTagCompound nbt = new NBTTagCompound();
			try {
				entity.writeToNBT(nbt);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			if (nbt.hasKey("id")) {
				String id = nbt.getString("id");
				world.getBlockState(pos).getProperties().forEach((iProperty, comparable) -> {
					System.out.println("i:" + iProperty + " c:" + comparable);
				});

//				System.out.println("state:"+world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)));
//				System.out.println("id:"+id);
//				Items.DYE.getDamage(new ItemStack(world.getBlockState(pos).getBlock()));			//ResourceLocation resourceLocation = new ResourceLocation(id);
//				if (TileEntity.REGISTRY.getKeys().contains(resourceLocation)) {
//					TileEntity.REGISTRY.getKeys().forEach(e -> {
//						//e.toString();
//						if(e.toString().equals(id)) {
//							Class<? extends TileEntity> clazz = TileEntity.REGISTRY.getObject(e);
//							//System.out.println(clazz.);
//						}
//					});
//				}//Class<? extends TileEntity> entity1 = TileEntity.REGISTRY.getObject(new ResourceLocation(id));

				//Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
				//System.out.println(nbt.toString());
				//Item item = Item.getByNameOrId(id);
				//Block block = Block.getBlockFromName(id);


				//if (JsonHelper.containsItemServer(JsonHelper.BLOCKER, world.getBlockState(pos).getBlock().getRegistryName().toString(), block.getMetaFromState(block.getDefaultState())))
				//	return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
