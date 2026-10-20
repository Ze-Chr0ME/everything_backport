package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsEntities;
import backport.everything_backport.mountsofmayhem.MountsItems;
import backport.everything_backport.mountsofmayhem.entity.CamelHusk;
import backport.everything_backport.mountsofmayhem.entity.Parched;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

/** 1.21.11: one natural husk in ten rides a camel husk, carrying an iron spear, with a parched behind it. */
@Mixin(Husk.class)
public abstract class HuskMixin extends Zombie {
	private HuskMixin(EntityType<? extends Zombie> entityType, Level level) {
		super(entityType, level);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(
		ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnGroupData
	) {
		boolean natural = entitySpawnReason == EntitySpawnReason.NATURAL && spawnGroupData == null;
		spawnGroupData = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, entitySpawnReason, spawnGroupData);
		RandomSource random = serverLevelAccessor.getRandom();
		BlockPos pos = this.blockPosition();
		if (natural
			&& !this.isBaby()
			&& !this.isPassenger()
			&& serverLevelAccessor.noCollision(MountsEntities.CAMEL_HUSK.getSpawnAABB(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5))
			&& random.nextFloat() < 0.1F) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(MountsItems.IRON_SPEAR));
			CamelHusk camelHusk = MountsEntities.CAMEL_HUSK.create(this.level(), EntitySpawnReason.NATURAL);
			if (camelHusk != null) {
				camelHusk.setPos(this.getX(), this.getY(), this.getZ());
				camelHusk.finalizeSpawn(serverLevelAccessor, difficultyInstance, entitySpawnReason, null);
				this.startRiding(camelHusk, true);
				serverLevelAccessor.addFreshEntity(camelHusk);
				Parched parched = MountsEntities.PARCHED.create(this.level(), EntitySpawnReason.NATURAL);
				if (parched != null) {
					parched.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
					parched.finalizeSpawn(serverLevelAccessor, difficultyInstance, entitySpawnReason, null);
					parched.startRiding(camelHusk, false);
					serverLevelAccessor.addFreshEntityWithPassengers(parched);
				}
			}
		}

		return spawnGroupData;
	}
}
