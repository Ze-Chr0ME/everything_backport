package backport.everything_backport.registry;

import backport.everything_backport.EverythingBackport;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

/** Registration helpers mirroring vanilla's, always under the {@code minecraft} namespace. */
public final class Reg {
	private Reg() {
	}

	public static ResourceKey<Block> blockKey(String name) {
		return ResourceKey.create(Registries.BLOCK, EverythingBackport.vanilla(name));
	}

	public static ResourceKey<Item> itemKey(String name) {
		return ResourceKey.create(Registries.ITEM, EverythingBackport.vanilla(name));
	}

	public static Block block(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
		return Blocks.register(blockKey(name), factory, properties);
	}

	public static Block block(String name, BlockBehaviour.Properties properties) {
		return block(name, Block::new, properties);
	}

	public static Item item(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
		return Items.registerItem(itemKey(name), factory, properties);
	}

	public static Item item(String name, Function<Item.Properties, Item> factory) {
		return item(name, factory, new Item.Properties());
	}

	public static Item item(String name, Item.Properties properties) {
		return item(name, Item::new, properties);
	}

	public static Item item(String name) {
		return item(name, Item::new, new Item.Properties());
	}

	public static Item blockItem(Block block) {
		return Items.registerBlock(block);
	}

	public static Item blockItem(Block block, Item.Properties properties) {
		return Items.registerBlock(block, BlockItem::new, properties);
	}

	public static Item blockItem(Block block, BiFunction<Block, Item.Properties, Item> factory) {
		return Items.registerBlock(block, factory);
	}

	public static SoundEvent sound(String name) {
		var id = EverythingBackport.vanilla(name);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}

	public static Holder<SoundEvent> soundHolder(String name) {
		var id = EverythingBackport.vanilla(name);
		return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
	}

	public static SimpleParticleType particle(String name, boolean alwaysShow) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, EverythingBackport.vanilla(name), FabricParticleTypes.simple(alwaysShow));
	}

	public static <T extends BlockEntity> BlockEntityType<T> blockEntity(
		String name, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks
	) {
		return Registry.register(
			BuiltInRegistries.BLOCK_ENTITY_TYPE, EverythingBackport.vanilla(name), FabricBlockEntityTypeBuilder.create(factory, blocks).build()
		);
	}

	public static <T extends Entity> EntityType<T> entity(String name, EntityType.Builder<T> builder) {
		ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, EverythingBackport.vanilla(name));
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
	}
}
