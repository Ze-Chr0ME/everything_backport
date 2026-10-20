package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsEntities;
import backport.everything_backport.mountsofmayhem.MountsTags;
import backport.everything_backport.mountsofmayhem.entity.ZombieNautilus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 1.21.11: half of the trident drowned outside river biomes ride a zombie nautilus. */
@Mixin(Drowned.class)
public abstract class DrownedMixin extends Zombie {
	private DrownedMixin(EntityType<? extends Zombie> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "finalizeSpawn", at = @At("RETURN"))
	private void everythingBackport$zombieNautilusJockey(
		ServerLevelAccessor serverLevelAccessor,
		DifficultyInstance difficultyInstance,
		EntitySpawnReason entitySpawnReason,
		@Nullable SpawnGroupData spawnGroupData,
		CallbackInfoReturnable<SpawnGroupData> cir
	) {
		if ((entitySpawnReason == EntitySpawnReason.NATURAL || entitySpawnReason == EntitySpawnReason.STRUCTURE)
			&& this.getMainHandItem().is(Items.TRIDENT)
			&& serverLevelAccessor.getRandom().nextFloat() < 0.5F
			&& !this.isBaby()
			&& !serverLevelAccessor.getBiome(this.blockPosition()).is(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
			ZombieNautilus zombieNautilus = MountsEntities.ZOMBIE_NAUTILUS.create(this.level(), EntitySpawnReason.JOCKEY);
			if (zombieNautilus != null) {
				if (entitySpawnReason == EntitySpawnReason.STRUCTURE) {
					zombieNautilus.setPersistenceRequired();
				}

				zombieNautilus.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
				zombieNautilus.finalizeSpawn(serverLevelAccessor, difficultyInstance, entitySpawnReason, null);
				this.startRiding(zombieNautilus, false);
				serverLevelAccessor.addFreshEntity(zombieNautilus);
			}
		}
	}

	@Inject(method = "isVisuallySwimming", at = @At("RETURN"), cancellable = true)
	private void everythingBackport$notSwimmingWhileRiding(CallbackInfoReturnable<Boolean> cir) {
		if (this.isPassenger()) {
			cir.setReturnValue(false);
		}
	}

	@Override
	public void rideTick() {
		super.rideTick();
		if (this.getControlledVehicle() instanceof PathfinderMob vehicle) {
			this.yBodyRot = vehicle.yBodyRot;
		}
	}

	@Override
	public boolean wantsToPickUp(ServerLevel serverLevel, ItemStack itemStack) {
		return itemStack.is(MountsTags.Items.SPEARS) ? false : super.wantsToPickUp(serverLevel, itemStack);
	}
}
